package com.crud.aws.sqs.consumer;

import com.crud.aws.avro.deserialize.AvroDeserializer;
import com.crud.aws.dynamoDB.entity.Pessoa;
import com.crud.aws.dynamoDB.service.IPessoaServiceDB;
import com.crud.aws.glue.service.GlueSchemaService;
import com.crud.aws.payload.AvroEnvelope;
import com.crud.aws.sqs.producer.IPessoaProducer;
import com.crud.dto.PessoaDTO;
import com.crud.mapper.PessoaMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PessoaConsumer {

    private final GlueSchemaService glueSchemaService;
    private final AvroDeserializer avroDeserializer;
    private final IPessoaServiceDB pessoaServiceDB;
    private final IPessoaProducer pessoaProducer;


    /**
     * Método responsável por escutar o aws sqs e enviar o payload para save no DynamoDB.
     * @param envelope
     */
    @SqsListener("pessoa-create.fifo")
    public void consumir(AvroEnvelope envelope) throws JsonProcessingException {
        try {
            Pessoa pessoa = desserializacao(envelope);
            pessoaServiceDB.savePessoa(pessoa);
        } catch (Exception ex) {
            pessoaProducer.enviarAvroEnvelopeToSqs(envelope, ex);
        }
    }

    /**
     * Método responsável por escutar o aws sqs e buscar pessoa pelo cpf no DynamoDB.
     * @param envelope
     */
    @SqsListener("pessoa-read")
    public void buscarPessoaPorCpf(AvroEnvelope envelope) throws JsonProcessingException {
        try {
            Pessoa pessoa = desserializacao(envelope);
            Pessoa resposta = pessoaServiceDB.buscarPessoaPorCpf(pessoa.getCpf());
            PessoaDTO pessoaDTO = PessoaMapper.converterPessoaDynamoDbParaPessoaDTO(resposta);
            pessoaProducer.enviarToSQS(pessoaDTO, pessoa);
        } catch (Exception ex) {
            pessoaProducer.enviarAvroEnvelopeToSqs(envelope, ex);
        }
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

}
