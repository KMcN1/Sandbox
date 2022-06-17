package com.mcn.integration.base;

import com.mcn.integration.config.DynamoDbTestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.dynamodb.DynaliteContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({DynamoDbTestConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public abstract class IntegrationTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTestBase.class);

    @Autowired
    protected TestRestTemplate testRestTemplate;

    // This starts fairly quickly, so OK to use '@Container' to startup, which ensures a clean state before each test class.
    @Container
    public static final GenericContainer REDIS = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
            .withExposedPorts(6379);

    private static final DynaliteContainer DYNAMO_DB;

    static {
        DYNAMO_DB = new DynaliteContainer();
        DYNAMO_DB.start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        if (REDIS != null) {
            registry.add("spring.redis.port", REDIS::getFirstMappedPort);
            registry.add("spring.redis.host", REDIS::getHost);
            registry.add("aws.dynamodb.endpoint", () -> DYNAMO_DB.getEndpointConfiguration().getServiceEndpoint());
        }
    }

}
