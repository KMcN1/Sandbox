package com.github.sparsick.testcontainerspringboot.hero.universum;

import com.github.sparsick.testcontainerspringboot.hero.containers.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {"classpath:db/clear_data.sql"})
class HeroSpringDataJpaRepositoryIT extends IntegrationTestBase {

    @Autowired
    private HeroSpringDataJpaRepository repositoryUnderTest;


    @Test
    void findHerosBySearchCriteria5() {
        repositoryUnderTest.save(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.findHerosBySearchCriteria("Batman");

        assertThat(heros).hasSize(1).contains(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
    }

}