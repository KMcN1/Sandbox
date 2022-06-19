package com.mcn.integration;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.mcn.integration.base.IntegrationTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AmazonSqsIT extends IntegrationTestBase {

    @Autowired
    private AmazonSQSAsync sqsAsync;

    @Value("${openbanking.batch-transactions-refresh-queue}")
    private String batchRefreshQueue;

    @AfterEach
    void tearDown() {
        sqsAsync.listQueues().getQueueUrls().stream()
                .filter(url -> !url.endsWith(batchRefreshQueue))
                .forEach(queue -> sqsAsync.deleteQueue(queue));
    }

    @Test
    void testFindQueue() {
        boolean queueExists = sqsAsync.listQueues().getQueueUrls().stream()
                .anyMatch(url -> url.endsWith(batchRefreshQueue));

        assertTrue(queueExists);
    }

    @Test
    void testCreateQueue() {
        CreateQueueResult result = sqsAsync.createQueue("myqueue1");

        assertThat(result.getSdkHttpMetadata().getHttpStatusCode(), equalTo(HttpStatus.OK.value()));
        assertThat(result.getQueueUrl(), Matchers.endsWith("myqueue1"));
    }

}