package com.github.sparsick.testcontainerspringboot.hero.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDBConfig {

    @Value("${aws.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${aws.credentials.access-key}")
    private String accessKey;

    @Value("${aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public DynamoDbAsyncClient amazonDynamoDBClient(AwsBasicCredentials awsBasicCredentials) {
        return DynamoDbAsyncClient
                .builder()
                .endpointOverride(URI.create(amazonDynamoDBEndpoint))
                .region(Region.EU_WEST_1)
                .credentialsProvider(() -> awsBasicCredentials)
                .build();
    }

    @Bean
    public AwsBasicCredentials awsBasicCredentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }


    /* Used for setting up tables in test */
    @Bean
    public DynamoDbEnhancedClient enhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    /* Used for setting up tables in test */
    @Bean
    public DynamoDbClient dynamoDbClient(AwsBasicCredentials awsBasicCredentials) {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(amazonDynamoDBEndpoint))
                .region(Region.EU_WEST_1)
                .credentialsProvider(() -> awsBasicCredentials)
                .build();
    }

}
