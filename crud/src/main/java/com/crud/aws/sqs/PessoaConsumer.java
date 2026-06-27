package com.crud.aws.sqs;

import com.crud.aws.avro.AvroDeserializer;
import com.crud.aws.dynamoDB.entity.Pessoa;
import com.crud.aws.dynamoDB.service.IPessoaServiceDB;
import com.crud.aws.glue.service.GlueSchemaService;
import com.crud.aws.payload.AvroEnvelope;
import com.crud.mapper.PessoaMapper;
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

    /**
     * Método responsável por escutar o aws sqs e enviar o payload para save no DynamoDB.
     * @param envelope
     */
    @SqsListener("pessoa.fifo")
    public void consumir(AvroEnvelope envelope) {
        Schema schema = glueSchemaService.buscarSchema(envelope.getSchemaVersionId());
        byte[] bytes = Base64.getDecoder().decode(envelope.getPayload());
        GenericRecord record = avroDeserializer.deserialize(bytes, schema);
        System.out.println(record);
        Pessoa pessoa = PessoaMapper.fromAvroToPessoa(record);
        pessoaServiceDB.savePessoa(pessoa);
    }
}
