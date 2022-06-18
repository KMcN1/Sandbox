package com.mcn.integration;

import com.mcn.customers.entity.Customer;
import com.mcn.integration.base.IntegrationTestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DynamoDbIT extends IntegrationTestBase {

    private static DynamoDbTable<Customer> customerTable;

    @Autowired
    private DynamoDbAsyncClient amazonDynamoDBClient;

    @BeforeAll
    static void setUp() {
        customerTable = dynamoDbUtils.getDynamoDbTable(Customer.class);
        boolean tableExists = dynamoDbUtils.createTable(customerTable);
        assertTrue(tableExists);
    }

    @Test
    void testDynamoDb() {
        List<Customer> customers = new ArrayList<>();
        for (Page<Customer> res : customerTable.scan()) {
            customers.addAll(res.items());
        }

        assertThat(customers, equalTo(Collections.emptyList()));
    }

}