package com.github.sparsick.testcontainerspringboot.hero.containers;

import com.github.sparsick.testcontainerspringboot.hero.containers.config.DynamoDbTestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
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
@DirtiesContext
@Testcontainers
public abstract class TestContainersBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestContainersBaseTest.class);

    @Autowired
    protected TestRestTemplate testRestTemplate;

    private static Network network = Network.newNetwork();

    @Container
    public static MySQLContainer database = new MySQLContainer<>("mysql:8.0.16")
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("pass");

    @Container
    public static GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
            .withExposedPorts(6379);

    /* Don't use @Container, as it will be re-started before every test class, and it takes ages.  */
    public static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.11.3"))
            .withNetwork(network)
            .withNetworkAliases("something", "awslocal")
            .withServices(SQS, DYNAMODB);

    static {
        /* Using static block instead, as it will only be started once, thereby speeding up time for multiple tests. */
        localStack.start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.port", redis::getFirstMappedPort);
        registry.add("spring.redis.host", redis::getHost);
        registry.add("aws.sqs.endpoint", () -> localStack.getEndpointOverride(SQS));
        registry.add("aws.dynamodb.endpoint", () -> localStack.getEndpointOverride(DYNAMODB));
        registry.add("aws.credentials.access-key", localStack::getAccessKey);
        registry.add("aws.credentials.secret-key", localStack::getSecretKey);
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

}
