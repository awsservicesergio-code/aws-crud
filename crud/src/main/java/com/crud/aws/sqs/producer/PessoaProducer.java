package com.crud.aws.sqs.producer;

import com.crud.aws.avro.deserialize.AvroDeserializer;
import com.crud.aws.avro.serialize.SerializerAvro;
import com.crud.aws.dynamoDB.entity.Pessoa;
import com.crud.aws.glue.service.GlueSchemaService;
import com.crud.aws.payload.AvroEnvelope;
import com.crud.aws.s3.service.IS3StorageService;
import com.crud.dto.PessoaDTO;
import com.crud.mapper.PessoaMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pessoa.resources.avro.EventType;
import com.pessoa.resources.avro.PessoaAvro;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PessoaProducer implements IPessoaProducer {

    private final GlueSchemaService glueSchemaService;
    private final AvroDeserializer avroDeserializer;
    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;
    private final IS3StorageService s3StorageService;

    /**
     * Método responsável por enviar o payload para a fila pessoa-read-back aws sqs.
     * @param pessoaDTO
     * @throws JsonProcessingException
     */
    @Override
    public void enviarToSQS(PessoaDTO pessoaDTO, Pessoa pessoa) throws JsonProcessingException {
        String idempotencyKey = pessoaDTO.getCpf();

        PessoaAvro pessoaAvro = PessoaMapper.toAvro(pessoaDTO);
        byte[] avroBytes = SerializerAvro.serialize(pessoaAvro);

        String schemaVersionId = glueSchemaService.obterSchemaVersionId(pessoaAvro.getSchema().toString());
        AvroEnvelope envelope = AvroEnvelope.builder().schemaVersionId(schemaVersionId)
                .payload(Base64.getEncoder().encodeToString(avroBytes)).build();

        String body = objectMapper.writeValueAsString(envelope);

        if (pessoa.getEventType() != EventType.READ) {
            sqsTemplate.send(sqsSendOptions -> sqsSendOptions
                    .queue("pessoa-read-back")
                    .payload(body)
                    .header("message-group-id", "pessoa")
                    .header("message-deduplication-id", idempotencyKey)
            );
        } else {
            sqsTemplate.send(sqsSendOptions -> sqsSendOptions
                    .queue("pessoa-read-back")
                    .payload(body)
            );
        }

    }

    /**
     * Método responsável por enviar o payload para a fila pessoa-dlq.fifo do aws sqs.
     * @param envelope
     * @throws JsonProcessingException
     */
    @Override
    public void enviarAvroEnvelopeToSqs(AvroEnvelope envelope, Exception ex) throws JsonProcessingException {
        Pessoa pessoa = desserializacao(envelope);
        String body = objectMapper.writeValueAsString(envelope);

        String idempotencyKey = UUID.randomUUID().toString();

        s3StorageService.deleteFromS3(pessoa);

        sqsTemplate.send(options -> options
                .queue("pessoa-dlq.fifo")
                .payload(body)
                .header("message-group-id", "pessoa")
                .header("message-deduplication-id", idempotencyKey)
                .header("exception", ex.getClass().getSimpleName())
                .header("error-message", ex.getMessage())
                .header("consumer", "PessoaConsumer")
        );
    }

    /**
     * Método responsável pela desserialização do payload.
     * @param envelope
     * @return Pessoa
     */
    private Pessoa desserializacao(AvroEnvelope envelope) {
        Schema schema = glueSchemaService.buscarSchema(envelope.getSchemaVersionId());
        byte[] bytes = Base64.getDecoder().decode(envelope.getPayload());
        GenericRecord record = avroDeserializer.deserialize(bytes, schema);
        return PessoaMapper.fromAvroToPessoa(record);
    }

    /**
     * Método responsável por criar mensagem para a fila pessoa-dql.fifo
     * @param queue
     * @param payload
     * @param ex
     * @return String
     */
    private String criarMensagemErro(String queue, Object payload, Exception ex) throws JsonProcessingException {
        return """
            {
              "sourceQueue": %s,
              "payload": %s,
              "error": %s
            }
            """.formatted(
                objectMapper.writeValueAsString(queue),
                objectMapper.writeValueAsString(payload),
                objectMapper.writeValueAsString(ex.getMessage())
        );
    }


}
