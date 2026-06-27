package com.crud.aws.dynamoDB.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@Profile("aws")
public class AwsConfig {

    /**
     * Método responsável pelo bean DynamoDbClient na aws.
     * @return DynamoDbClient
     */
    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    /**
     * Método responsável pelo bean DynamoDbEnhancedClient na aws.
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
