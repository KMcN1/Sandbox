package com.github.sparsick.testcontainerspringboot.hero.universum;

import com.github.sparsick.testcontainerspringboot.hero.containers.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class HeroClassicJpaRepositoryReuseDatabaseIT extends IntegrationTestBase {

    @Autowired
    private HeroClassicJpaRepository repositoryUnderTest;

    @Sql(scripts = {"classpath:db/clear_data.sql"})
    @Test
    void findAllHeroes3(){
        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
        repositoryUnderTest.addHero(new Hero("Superman", "Metropolis", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.allHeroes();

        assertThat(heros).hasSize(2);
    }

    @Sql(scripts = {"classpath:db/clear_data.sql"})
    @Test
    void findHeroByCriteria3(){
        repositoryUnderTest.addHero(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.findHerosBySearchCriteria("Batman");

        assertThat(heros).contains(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
    }
}