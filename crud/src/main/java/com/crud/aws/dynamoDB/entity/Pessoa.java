package com.crud.aws.dynamoDB.entity;

import com.pessoa.resources.avro.EventType;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamoDbBean
public class Pessoa {

    private String id;
    private String cpf;
    @Getter
    private String nome;
    @Getter
    private String s3Key;
    @Getter
    private String eventId;
    @Getter
    private EventType eventType;
    @Getter
    private String timestamp;

    @DynamoDbPartitionKey
    public String getCpf() {
        return cpf;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "id-index")
    public String getId() {
        return id;
    }

}