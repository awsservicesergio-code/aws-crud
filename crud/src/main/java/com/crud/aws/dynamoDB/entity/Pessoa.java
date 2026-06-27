package com.crud.aws.dynamoDB.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Pessoa {

    private String id;
    private String cpf;
    @Getter
    private String nome;
    @Getter
    private String s3Key;

    @DynamoDbPartitionKey
    public String getCpf() {
        return cpf;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "id-index")
    public String getId() {
        return id;
    }

}