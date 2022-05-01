package com.github.sparsick.testcontainerspringboot.hero.containers;

import org.junit.ClassRule;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
public class TestContainersBaseTest {

//    private static Network network = Network.newNetwork();
//    @ClassRule
//    public static LocalStackContainer localstackInDockerNetwork = new LocalStackContainer()
//            .withNetwork(network)
//            .withNetworkAliases("notthis", "localstack")    // the last alias is used for HOSTNAME_EXTERNAL
//            .withServices(S3, SQS, CLOUDWATCHLOGS);

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Container
    public static MySQLContainer mySql =  new MySQLContainer<>("")
            .withDatabaseName("affordiq")
            .withUsername("user")
            .withPassword("pass");
            //.withInitScript("");


    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySql::getJdbcUrl);
        registry.add("spring.datasource.username", mySql::getUsername);
        registry.add("spring.datasource.password", mySql::getPassword);

    }


}
