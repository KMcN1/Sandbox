package com.github.sparsick.testcontainerspringboot.hero.containers;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.github.sparsick.testcontainerspringboot.hero.universum.ComicUniversum;
import com.github.sparsick.testcontainerspringboot.hero.universum.Hero;
import com.github.sparsick.testcontainerspringboot.hero.universum.HeroClassicJpaRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

class HeroContainersJpaRepositoryIT extends TestContainersBaseTest {

    @Autowired
    private HeroClassicJpaRepository repositoryUnderTest;

    @Value("${aws.sqs.endpoint}")
    private String endpoint;

    @Value("${aws.credentials.access-key}")
    String accessKey;

    @Value("${aws.credentials.secret-key}")
    String secretKey;

    private AmazonSQSAsync client;

    @BeforeEach
    void setUp() {
        client = getClient();
    }

    @Test
    void testCreateQueue() {
        CreateQueueResult result = client.createQueue("myqueue1");

        assertThat(result.getQueueUrl(), Matchers.endsWith("myqueue1"));
    }

    private AmazonSQSAsync getClient() {
        AmazonSQSAsync sqsAsync = AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                endpoint, Regions.US_EAST_2.getName()
                        )
                ).build();
        return sqsAsync;
    }

    private AWSStaticCredentialsProvider getCredentialsProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
    }

    @Test
    void findAllHero() {
        int numberHeroes = repositoryUnderTest.allHeroes().size();

        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
        repositoryUnderTest.addHero(new Hero("Superman", "Metropolis", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.allHeroes();

        assertThat(heros, hasSize(numberHeroes + 2));
    }

    @Test
    void findHeroByCriteria() {
        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.findHerosBySearchCriteria("Batman");

        assertThat(heros, contains(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS)));
    }

}