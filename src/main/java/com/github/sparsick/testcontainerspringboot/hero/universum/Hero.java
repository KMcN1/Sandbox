package com.github.sparsick.testcontainerspringboot.hero.universum;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "hero")
@Entity
public class Hero {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String city;

    @Enumerated(EnumType.STRING)
    private ComicUniversum universum;

    public Hero(String name, String city, ComicUniversum universum) {
        this.name = name;
        this.city = city;
        this.universum = universum;
    }

    public Hero() {

    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public ComicUniversum getUniversum() {
        return universum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return Objects.equals(name, hero.name) &&
                Objects.equals(city, hero.city) &&
                universum == hero.universum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, universum);
    }

    @Override
    public String toString() {
        return "Hero{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", universum=" + universum.getDisplayName() +
                '}';
    }
}
