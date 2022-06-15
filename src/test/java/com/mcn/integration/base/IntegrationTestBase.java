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
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(DynamoDbTestConfig.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public abstract class IntegrationTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTestBase.class);
    public static final String PROFILE_LOCAL_CONTAINERS = "local-containers";

    @Autowired
    protected TestRestTemplate testRestTemplate;

    // This starts fairly quickly, so OK to use '@Container' to startup, which ensures a clean state before each test class.
    @Container
    public static final GenericContainer REDIS = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
            .withExposedPorts(6379);

    /* Don't use @Container annotation, as it will be re-started before every subclass (and it takes ages to start).
       So, need to use @Sql annotations in child classes to get a clean state before each test class  */
    public static MySQLContainer DATABASE;


    /* Don't use @Container, as it will be re-started before every test class, and it takes ages.
       So, need to find a way to clean SQS and DynamoDB before each test class to ensure a clean state. */
    public static LocalStackContainer LOCAL_STACK_CONTAINER;

    static {
        if (useLocalContainers()) {
            LOGGER.info("Attempting to use local MySQL, SQS and DynamoDB");
        } else {
            /* Using static block instead, so these are only started once, thereby speeding up time for multiple tests. */
            LOCAL_STACK_CONTAINER = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.11.3"))
                    .withNetwork(Network.newNetwork())
                    .withNetworkAliases("localhost", "awslocal")
                    .withServices(SQS, DYNAMODB);
            LOCAL_STACK_CONTAINER.start();

            DATABASE = new MySQLContainer<>("mysql:8.0.16")
                    .withDatabaseName("test")
                    .withUsername("user")
                    .withPassword("pass");
            DATABASE.start();
        }
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        if (REDIS != null) {
            registry.add("spring.redis.port", REDIS::getFirstMappedPort);
            registry.add("spring.redis.host", REDIS::getHost);
        }
        if (LOCAL_STACK_CONTAINER != null) {
            registry.add("aws.sqs.endpoint", () -> LOCAL_STACK_CONTAINER.getEndpointOverride(SQS));
            registry.add("aws.dynamodb.endpoint", () -> LOCAL_STACK_CONTAINER.getEndpointOverride(DYNAMODB));
            registry.add("aws.credentials.access-key", LOCAL_STACK_CONTAINER::getAccessKey);
            registry.add("aws.credentials.secret-key", LOCAL_STACK_CONTAINER::getSecretKey);
        }
        if (DATABASE != null) {
            registry.add("spring.datasource.url", DATABASE::getJdbcUrl);
            registry.add("spring.datasource.username", DATABASE::getUsername);
            registry.add("spring.datasource.password", DATABASE::getPassword);
        }
    }

    private static boolean useLocalContainers() {
        return System.getProperty("spring.profiles.active", "")
                .toLowerCase().contains(PROFILE_LOCAL_CONTAINERS);
    }

}
