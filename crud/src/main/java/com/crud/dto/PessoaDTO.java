package com.crud.dto;

import com.pessoa.resources.avro.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDTO {
    private String id;
    private String nome;
    private String cpf;
    private String s3Key;
    private String eventId;
    private EventType eventType;
    private long timestamp;
}
