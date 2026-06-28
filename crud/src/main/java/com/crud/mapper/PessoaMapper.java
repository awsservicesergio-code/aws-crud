package com.crud.mapper;

import com.crud.aws.dynamoDB.entity.Pessoa;
import com.crud.dto.PessoaDTO;
import com.pessoa.resources.avro.PessoaAvro;
import org.apache.avro.generic.GenericRecord;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PessoaMapper {

    /**
     * Método responsável por converter PessoaDTO em PessoaAvro.
     * @param dto
     * @return PessoaAvro
     */
    public static PessoaAvro toAvro(PessoaDTO dto) {
        return PessoaAvro.newBuilder()
                .setId(dto.getId())
                .setNome(dto.getNome())
                .setCpf(dto.getCpf())
                .setS3Key(dto.getS3Key())
                .setEventId(dto.getEventId())
                .setEventType(dto.getEventType())
                .setTimestamp(Instant.ofEpochSecond(dto.getTimestamp()))
                .build();
    }

    /**
     * Método responsável por converter PessoaAvro em PessoaDTO.
     * @param avro
     * @return PessoaDTO
     */
    public static PessoaDTO fromAvro(PessoaAvro avro) {
        PessoaDTO dto = new PessoaDTO();
        dto.setId(avro.getId());
        dto.setNome(avro.getNome());
        dto.setCpf(avro.getCpf());
        dto.setS3Key(avro.getS3Key());
        dto.setEventId(dto.getEventId());
        dto.setEventType(dto.getEventType());
        dto.setTimestamp(dto.getTimestamp());
        return dto;
    }

    /**
     * Método responsável por converter GenericRecord em Pessoa.
     * @param record
     * @return Pessoa
     */
    public static Pessoa fromAvroToPessoa(GenericRecord record) {
        return Pessoa.builder()
                .id(record.get("id") != null ? record.get("id").toString() : null)
                .nome(record.get("nome") != null ? record.get("nome").toString() : null)
                .cpf(record.get("cpf") != null ? record.get("cpf").toString() : null)
                .s3Key(record.get("s3Key") != null ? record.get("s3Key").toString() : null)
                .eventId(record.get("eventId") != null ? record.get("eventId").toString() : null)
                .eventType(record.get("eventType") != null ? (com.pessoa.resources.avro.EventType) record.get("eventType") : null)
                .timestamp(record.get("timestamp") != null ? converterLongTimestampToString(record.get("timestamp")) : null)
                .build();

    }

    /**
     * Método responsável por converter PessoaDTO em Pessoa.
     * @param pessoaDTO
     * @return Pessoa
     */
    public static Pessoa fromPessoaDTO(PessoaDTO pessoaDTO){
        return Pessoa.builder()
                .id(pessoaDTO.getId())
                .nome(pessoaDTO.getNome())
                .cpf(pessoaDTO.getCpf())
                .s3Key(pessoaDTO.getS3Key())
                .eventId(pessoaDTO.getEventId())
                .eventType(pessoaDTO.getEventType())
                .timestamp(String.valueOf(pessoaDTO.getTimestamp()))
                .build();
    }

    /**
     * Método reponsável por converter timestamp do tipo Long para data do tipo String.
     * @param timestamp
     * @return String
     */
    public static String converterLongTimestampToString(Object timestamp){
        Instant instant = (Instant) timestamp;
        LocalDateTime dataLocal = LocalDateTime.ofInstant(
                instant,
                ZoneId.of("America/Sao_Paulo")
        );
        return dataLocal.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    /**
     * Método reponsável por converter timestamp do tipo String para Long.
     * @param timestamp
     * @return Long
     */
    public static Long converterStringTimestampToLong(String timestamp){
        return Instant.parse(timestamp).toEpochMilli();
    }

}
