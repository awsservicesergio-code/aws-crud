package com.crud.aws.glue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.glue.GlueClient;

@Configuration
public class AwsGlueConfig {

    /**
     * Método responsável por obter o GlueClient na aws.
     * @return GlueClient
     */
    @Bean
    public GlueClient glueClient() {
        return GlueClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
