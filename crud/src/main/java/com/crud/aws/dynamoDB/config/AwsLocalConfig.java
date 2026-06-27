package com.crud.aws.dynamoDB.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.net.URI;

@Configuration
@Profile("local")
public class AwsLocalConfig {

    private static final String LOCALSTACK_ENDPOINT = "http://localhost:4566";

    /**
     * Método responsável pelo bean DynamoDbClient no localstack.
     * @return DynamoDbClient
     */
    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("test", "test")
                        )
                )
                .build();
    }

    /**
     * Método responsável pelo bean DynamoDbEnhancedClient no localstack.
     * @param client
     * @return DynamoDbEnhancedClient
     */
    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient client) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();
    }

}
