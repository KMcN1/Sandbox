package com.mcn.integration.config;

import com.amazonaws.services.sqs.AmazonSQSClient;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Properties;

@Profile("test")
@Configuration
public class SQSRestServerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQSRestServerConfig.class);

    private static final String SPRING_KEY_SQS_ENDPOINT = "aws.sqs.endpoint";

    /* This property is purely so that the Queue URL can be referenced in tests */
    private static final String SPRING_KEY_OPENBANKING_QUEUE_URL = "openbanking.queue.url";

    private SQSRestServer sqsRestServer;
    private AmazonSQSClient sqsClient;

    private String endpoint;
    private String queueUrl;

    // @Value("${openbanking.batch-transactions-refresh-queue}")
    private String queueName = "some-queue";

    @Autowired
    private ConfigurableEnvironment env;

    @PostConstruct
    public void setUp() {
        startSqsClientAndServer();
        LOGGER.info("SQS Proxy Server started: {}", LocalDateTime.now());
    }

    @PreDestroy
    public void tearDown() {
        LOGGER.info("SQS Proxy Server shutting down: {}", LocalDateTime.now());
        try {
            stopSqsClientAndServer();
        } catch (Throwable ex) {
            LOGGER.warn(ex.getMessage());
        }
    }

    protected void startSqsClientAndServer() {
        // Create an Elastic MQ Server on a Random Port
        sqsRestServer = SQSRestServerBuilder
                .withInterface("0.0.0.0")
                .withDynamicPort()
                .start();

        // SQS service endpoint equivalent
        int sqsPort = sqsRestServer.waitUntilStarted().localAddress().getPort();
        endpoint = "http://localhost:" + sqsPort;

        // SQS client for testing (ignore the deprecated part, as it's the only way to set our own URL)
        sqsClient = new AmazonSQSClient().withEndpoint(endpoint);

        // Create queue before the Application Context loads, and save the URL
        queueUrl = sqsClient.createQueue(queueName).getQueueUrl();

        setProperties();
    }

    protected void stopSqsClientAndServer() {
        sqsClient.shutdown();
        // NOTE: This can take around 10-20 seconds to stop, due to the message listener we use.
        // Need to investigate how to reduce that time.
        sqsRestServer.stopAndWait();
    }

    /*
     * I'm not a big fan of setting the properties this way but needs must.
     */
    private void setProperties() {
        Properties source = new Properties();
        source.put(SPRING_KEY_SQS_ENDPOINT, endpoint);
        source.put(SPRING_KEY_OPENBANKING_QUEUE_URL, queueUrl);
        env.getPropertySources().addFirst(new PropertiesPropertySource(this.getClass().getSimpleName(), source));
    }
}
