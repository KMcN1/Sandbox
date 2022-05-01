package com.github.sparsick.testcontainerspringboot.hero.containers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Testcontainers
public abstract class TestContainersBaseTest {

//    private static Network network = Network.newNetwork();
//    @ClassRule
//    public static LocalStackContainer localstackInDockerNetwork = new LocalStackContainer()
//            .withNetwork(network)
//            .withNetworkAliases("notthis", "localstack")    // the last alias is used for HOSTNAME_EXTERNAL
//            .withServices(S3, SQS, CLOUDWATCHLOGS);

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Container
    public static MySQLContainer database =  new MySQLContainer<>("mysql:8.0.16")
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("pass");
            //.withInitScript("");


    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);

    }


}
