package com.github.sparsick.testcontainerspringboot.hero.containers;

import com.github.sparsick.testcontainerspringboot.hero.universum.ComicUniversum;
import com.github.sparsick.testcontainerspringboot.hero.universum.Hero;
import com.github.sparsick.testcontainerspringboot.hero.universum.HeroClassicJpaRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class HeroContainersJpaRepositoryIT extends TestContainersBaseTest {

    @Autowired
    private HeroClassicJpaRepository repositoryUnderTest;

    @Test
    void findAllHero(){
        int numberHeroes = repositoryUnderTest.allHeroes().size();

        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
        repositoryUnderTest.addHero(new Hero("Superman", "Metropolis", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.allHeroes();

        assertThat(heros).hasSize(numberHeroes + 2);
    }

    @Test
    void findHeroByCriteria(){
        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.findHerosBySearchCriteria("Batman");

        assertThat(heros).contains(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
    }

}