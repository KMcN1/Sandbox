package com.mcn.integration.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.dynamodb.DynaliteContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public abstract class IntegrationTestBase {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    // This starts fairly quickly, so OK to use '@Container' to startup, which ensures a clean state before each test class.
    @Container
    public static final GenericContainer REDIS = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
            .withExposedPorts(6379);

    private static final DynaliteContainer DYNAMO_DB;
    protected static final DynamoDbUtils dynamoDbUtils;

    static {
        DYNAMO_DB = new DynaliteContainer();
        DYNAMO_DB.start();

        dynamoDbUtils = new DynamoDbUtils(DYNAMO_DB.getEndpointConfiguration().getServiceEndpoint(),
                DYNAMO_DB.getCredentials().getCredentials().getAWSAccessKeyId(),
                DYNAMO_DB.getCredentials().getCredentials().getAWSSecretKey());
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        String accessKey = DYNAMO_DB.getCredentials().getCredentials().getAWSAccessKeyId();
        String secretKey = DYNAMO_DB.getCredentials().getCredentials().getAWSSecretKey();
        registry.add("spring.redis.port", REDIS::getFirstMappedPort);
        registry.add("spring.redis.host", REDIS::getHost);
        registry.add("aws.dynamodb.endpoint", () -> DYNAMO_DB.getEndpointConfiguration().getServiceEndpoint());
        registry.add("aws.credentials.access-key", () -> DYNAMO_DB.getCredentials().getCredentials().getAWSAccessKeyId());
        registry.add("aws.credentials.secret-key", () -> DYNAMO_DB.getCredentials().getCredentials().getAWSSecretKey());
    }

}
