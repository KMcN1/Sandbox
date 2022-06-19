package com.mcn.integration.config;

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

    private SQSRestServer sqsRestServer;

    private String endpoint;

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

        setProperties();
    }

    protected void stopSqsClientAndServer() {
        sqsRestServer.stopAndWait();
    }

    private void setProperties() {
        Properties source = new Properties();
        source.put("aws.sqs.endpoint", endpoint);
        env.getPropertySources().addFirst(
                new PropertiesPropertySource(this.getClass().getSimpleName(), source)
        );
    }
}
