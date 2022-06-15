package com.mcn.integration;

import com.mcn.customers.entity.Customer;
import com.mcn.integration.base.IntegrationTestBase;
import com.mcn.integration.config.DynamoDbTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DynamoDbIT extends IntegrationTestBase {


    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private DynamoDbTestUtils dynamoDbTestUtils;

    private DynamoDbTable<Customer> table;

    @BeforeEach
    void beforeEach() {
        dynamoDbTestUtils.createTable(Customer.class);
    }

    @Test
    void testDynamoDb() {
        table = enhancedClient.table("Customer", TableSchema.fromClass(Customer.class));

        List<Customer> customers = new ArrayList<>();
        Iterator<Page<Customer>> iterator = table.scan().iterator();
        while (iterator.hasNext()) {
            Page<Customer> res = iterator.next();
            customers.addAll(res.items());
        }

        assertThat(customers, equalTo(Collections.emptyList()));
    }
}