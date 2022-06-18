package com.mcn.integration.base;

import com.amazonaws.auth.AWSCredentials;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.retry.backoff.BackoffStrategy;
import software.amazon.awssdk.core.waiters.WaiterOverrideConfiguration;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.net.URI;
import java.time.Duration;

public class DynamoDbUtils {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbClient dynamoDbClient;

    public DynamoDbUtils(String serviceEndpoint, String accessKey, String secretKey) {
        this.dynamoDbClient = createClient(serviceEndpoint, accessKey, secretKey);
        this.dynamoDbEnhancedClient = createEnhancedClient(dynamoDbClient);
    }

    public <T> DynamoDbTable<T> getDynamoDbTable(Class<T> clazz) {
        return getDynamoDbTable(clazz.getSimpleName(), clazz);
    }

    public <T> DynamoDbTable<T> getDynamoDbTable(String tableName, Class<T> clazz) {
        return dynamoDbEnhancedClient.table(tableName, TableSchema.fromClass(clazz));
    }

    public <T> boolean createTable(DynamoDbTable<T> table) {
        String tableName = table.tableName();
        if (dynamoDbClient.listTables().tableNames().contains(table.tableName())) {
            return true;
        }

        table.createTable(CreateTableEnhancedRequest.builder()
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build())
                .build());

        WaiterResponse<DescribeTableResponse> result = DynamoDbWaiter.builder()
                .overrideConfiguration(WaiterOverrideConfiguration.builder()
                        .waitTimeout(Duration.ofMillis(1000))
                        .backoffStrategy(BackoffStrategy.defaultThrottlingStrategy())
                        .maxAttempts(50)
                        .build())
                .client(dynamoDbClient).build()
                .waitUntilTableExists(describeTableRequest(tableName));

        return result.matched().response().isPresent();
    }

    private <T> DescribeTableRequest describeTableRequest(String tableName) {
        return DescribeTableRequest.builder()
                .tableName(tableName)
                .build();
    }

    private DynamoDbClient createClient(String serviceEndpoint, String accessKey, String secretKey) {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(serviceEndpoint))
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey,secretKey))
                .build();
    }

    private DynamoDbEnhancedClient createEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

}
