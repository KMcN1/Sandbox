package com.github.sparsick.testcontainerspringboot.hero.universum;

import com.github.sparsick.testcontainerspringboot.hero.containers.TestContainersBaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class HeroClassicJpaRepositoryReuseDatabaseIT extends TestContainersBaseTest {

    @Autowired
    private HeroClassicJpaRepository repositoryUnderTest;

    @Test
    void findAllHeroes3(){
        int numberHeroes = repositoryUnderTest.allHeroes().size();

        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
        repositoryUnderTest.addHero(new Hero("Superman", "Metropolis", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.allHeroes();

        assertThat(heros).hasSize(numberHeroes + 2);
    }

    @Test
    void findHeroByCriteria3(){
        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.findHerosBySearchCriteria("Batman");

        assertThat(heros).contains(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
    }
}