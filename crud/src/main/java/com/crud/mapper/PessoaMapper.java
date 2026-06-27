package com.crud.mapper;

import com.crud.dto.PessoaDTO;
import com.crud.entity.Pessoa;
import com.crud.resources.avro.PessoaAvro;
import org.apache.avro.generic.GenericRecord;

public class PessoaMapper {

    public static PessoaAvro toAvro(PessoaDTO dto) {
        return PessoaAvro.newBuilder()
                .setId(dto.getId())
                .setNome(dto.getNome())
                .setCpf(dto.getCpf())
                .setS3Key(dto.getS3Key())
                .build();
    }

    public static PessoaDTO fromAvro(PessoaAvro avro) {
        PessoaDTO dto = new PessoaDTO();
        dto.setId(avro.getId());
        dto.setNome(avro.getNome());
        dto.setCpf(avro.getCpf());
        dto.setS3Key(avro.getS3Key());
        return dto;
    }

    public static Pessoa fromAvroToPessoa(GenericRecord record) {
        return Pessoa.builder()
                .id(record.get("id") != null ? record.get("id").toString() : null)
                .nome(record.get("nome") != null ? record.get("nome").toString() : null)
                .cpf(record.get("cpf") != null ? record.get("cpf").toString() : null)
                .s3Key(record.get("s3Key") != null ? record.get("s3Key").toString() : null)
                .build();

    }

    public static  Pessoa fromPessoaDTO(PessoaDTO pessoaDTO){
        return Pessoa.builder()
                .id(pessoaDTO.getId())
                .nome(pessoaDTO.getNome())
                .cpf(pessoaDTO.getCpf())
                .s3Key(pessoaDTO.getS3Key()).build();
    }

}
