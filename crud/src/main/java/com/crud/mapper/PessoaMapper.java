package com.crud.mapper;

import com.crud.dto.PessoaDTO;
import com.crud.resources.avro.PessoaAvro;
import org.springframework.stereotype.Component;

@Component
public class PessoaMapper {

    public PessoaAvro toAvro(PessoaDTO dto) {
        return PessoaAvro.newBuilder()
                .setId(dto.getId())
                .setNome(dto.getNome())
                .setCpf(dto.getCpf())
                .build();
    }

    public PessoaDTO fromAvro(PessoaAvro avro) {
        PessoaDTO dto = new PessoaDTO();
        dto.setId(avro.getId());
        dto.setNome(avro.getNome());
        dto.setCpf(avro.getCpf());
        return dto;
    }

}
