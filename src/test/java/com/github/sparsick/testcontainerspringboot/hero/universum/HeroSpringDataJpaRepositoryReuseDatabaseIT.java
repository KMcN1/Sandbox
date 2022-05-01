package com.github.sparsick.testcontainerspringboot.hero.universum;

import com.github.sparsick.testcontainerspringboot.hero.containers.TestContainersBaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HeroSpringDataJpaRepositoryReuseDatabaseIT extends TestContainersBaseTest {

    @Autowired
    private HeroSpringDataJpaRepository repositoryUnderTest;

    @Test
    void findHerosBySearchCriteria7() {
        repositoryUnderTest.save(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.findHerosBySearchCriteria("Batman");

        assertThat(heros).contains(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
    }
}