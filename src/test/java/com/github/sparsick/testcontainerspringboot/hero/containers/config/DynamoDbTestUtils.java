package com.github.sparsick.testcontainerspringboot.hero.containers.config;

import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.List;
import java.util.stream.Collectors;

public class DynamoDbTestUtils {

    private static final ProvisionedThroughput PROVISIONED_THROUGHPUT =
            ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build();

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbEnhancedClient enhancedClient;

    public DynamoDbTestUtils(DynamoDbClient dynamoDbClient, DynamoDbEnhancedClient enhancedClient) {
        this.dynamoDbClient = dynamoDbClient;
        this.enhancedClient = enhancedClient;
    }

    public <T> boolean createTable(Class<T> clazz) {
        CreateTableEnhancedRequest createTableRequest = CreateTableEnhancedRequest.builder()
                .provisionedThroughput(PROVISIONED_THROUGHPUT)
                .build();

        return createTable(clazz, createTableRequest);
    }

    public <T> boolean createTable(Class<T> clazz, List<String> indexNames) {
        CreateTableEnhancedRequest createTableRequest = CreateTableEnhancedRequest.builder()
                .globalSecondaryIndices(globalSecondaryIndexList(indexNames))
                .provisionedThroughput(PROVISIONED_THROUGHPUT)
                .build();

        return createTable(clazz, createTableRequest);
    }

    private <T> boolean createTable(Class<T> clazz, CreateTableEnhancedRequest createTableRequest) {
        String tableName = clazz.getSimpleName();
        if (dynamoDbClient.listTables().tableNames().contains(tableName)) {
            return true;
        }

        enhancedClient.table(tableName, TableSchema.fromClass(clazz))
                .createTable(createTableRequest);

        WaiterResponse<DescribeTableResponse> result = DynamoDbWaiter.builder().client(dynamoDbClient).build()
                .waitUntilTableExists(describeTableRequest(tableName));

        return result.matched().response().isPresent();
    }

    public void deleteTable(Class<?> clazz) {
        dynamoDbClient.deleteTable(DeleteTableRequest.builder()
                .tableName(clazz.getSimpleName())
                .build());
    }

    private List<EnhancedGlobalSecondaryIndex> globalSecondaryIndexList(List<String> indexNames) {
        return indexNames
                .stream()
                .map(this::enhancedGlobalSecondaryIndex)
                .collect(Collectors.toList());
    }

    private EnhancedGlobalSecondaryIndex enhancedGlobalSecondaryIndex(String indexName) {
        return EnhancedGlobalSecondaryIndex.builder()
                .indexName(indexName)
                .provisionedThroughput(PROVISIONED_THROUGHPUT)
                .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                .build();
    }

    private <T> DescribeTableRequest describeTableRequest(String tableName) {
        return DescribeTableRequest.builder()
                .tableName(tableName)
                .build();
    }

}
