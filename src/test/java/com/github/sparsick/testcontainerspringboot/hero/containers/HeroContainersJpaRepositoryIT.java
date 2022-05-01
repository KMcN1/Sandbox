package com.github.sparsick.testcontainerspringboot.hero.containers;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.github.sparsick.testcontainerspringboot.hero.universum.ComicUniversum;
import com.github.sparsick.testcontainerspringboot.hero.universum.Hero;
import com.github.sparsick.testcontainerspringboot.hero.universum.HeroClassicJpaRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

class HeroContainersJpaRepositoryIT extends TestContainersBaseTest {

    @Autowired
    private HeroClassicJpaRepository repositoryUnderTest;

    @Autowired
    private AmazonSQSAsync sqsAsync;

    @Test
    void testCreateQueue() {
        CreateQueueResult result = sqsAsync.createQueue("myqueue1");

        assertThat(result.getQueueUrl(), Matchers.endsWith("myqueue1"));
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