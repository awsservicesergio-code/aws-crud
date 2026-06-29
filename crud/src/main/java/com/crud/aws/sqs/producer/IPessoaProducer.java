package com.crud.aws.sqs.producer;

import com.crud.aws.dynamoDB.entity.Pessoa;
import com.crud.aws.payload.AvroEnvelope;
import com.crud.dto.PessoaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface IPessoaProducer {
    void enviarToSQS(PessoaDTO pessoaDTO, Pessoa pessoa ) throws JsonProcessingException;
    void enviarAvroEnvelopeToSqs(AvroEnvelope avroEnvelope, Exception ex) throws JsonProcessingException;
}
