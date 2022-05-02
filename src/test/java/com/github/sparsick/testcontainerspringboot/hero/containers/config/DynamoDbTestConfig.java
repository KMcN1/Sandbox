package com.github.sparsick.testcontainerspringboot.hero.containers.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@TestConfiguration
public class DynamoDbTestConfig {

    /* Used for setting up tables in test */
    @Bean
    public DynamoDbTestUtils dynamoDbTestUtils(DynamoDbClient dynamoDbClient, DynamoDbEnhancedClient enhancedClient) {
        return new DynamoDbTestUtils(dynamoDbClient, enhancedClient);
    }
}
