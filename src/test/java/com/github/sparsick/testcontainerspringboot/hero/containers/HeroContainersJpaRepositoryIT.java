package com.github.sparsick.testcontainerspringboot.hero.containers;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.github.sparsick.testcontainerspringboot.hero.containers.config.DynamoDbTestUtils;
import com.github.sparsick.testcontainerspringboot.hero.ddb.Customer;
import com.github.sparsick.testcontainerspringboot.hero.universum.ComicUniversum;
import com.github.sparsick.testcontainerspringboot.hero.universum.Hero;
import com.github.sparsick.testcontainerspringboot.hero.universum.HeroClassicJpaRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class HeroContainersJpaRepositoryIT extends IntegrationTestBase {

    @Autowired
    private HeroClassicJpaRepository repositoryUnderTest;

    @Autowired
    private AmazonSQSAsync sqsAsync;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private DynamoDbTestUtils dynamoDbTestUtils;

    private DynamoDbTable<Customer> table;

    @BeforeEach
    void beforeEach() {
        dynamoDbTestUtils.createTable(Customer.class);
    }

    @Sql(scripts = {"classpath:db/clear_data.sql"})
    @Test
    void findAllHeroes() {
        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
        repositoryUnderTest.addHero(new Hero("Superman", "Metropolis", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.allHeroes();

        assertThat(heros, hasSize(2));
    }

    @Sql(scripts = {"classpath:db/clear_data.sql"})
    @Test
    void findHeroByCriteria() {
        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.findHerosBySearchCriteria("Batman");

        assertThat(heros, contains(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS)));
    }

    @Test
    void testDynamoDb() {
        table = enhancedClient.table("Customer", TableSchema.fromClass(Customer.class));

        List<Customer> customers = new ArrayList<>();
        Iterator<Page<Customer>> iterator = table.scan().iterator();
        while (iterator.hasNext()) {
            Page<Customer> res = iterator.next();
            customers.addAll(res.items());
        }

        assertThat(customers, equalTo(Collections.emptyList()));
    }

    @Test
    void testCreateQueue() {
        CreateQueueResult result = sqsAsync.createQueue("myqueue1");

        assertThat(result.getQueueUrl(), Matchers.endsWith("myqueue1"));
    }


}