package com.mcn.integration;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteQueueResult;
import com.mcn.integration.base.IntegrationTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class AmazonSqsIT extends IntegrationTestBase {

    @Autowired
    private AmazonSQSAsync sqsAsync;

    private CreateQueueResult result;

    @AfterEach
    void tearDown(){
        DeleteQueueResult deleteQueueResult = sqsAsync.deleteQueue(result.getQueueUrl());

        assertThat(deleteQueueResult.getSdkHttpMetadata().getHttpStatusCode(), equalTo(HttpStatus.OK.value()));
    }

    @Test
    void testCreateQueue() {
        result = sqsAsync.createQueue("myqueue1");


        assertThat(result.getSdkHttpMetadata().getHttpStatusCode(), equalTo(HttpStatus.OK.value()));
        assertThat(result.getQueueUrl(), Matchers.endsWith("myqueue1"));

    }


}