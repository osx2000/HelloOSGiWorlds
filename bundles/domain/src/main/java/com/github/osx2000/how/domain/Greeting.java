package com.github.osx2000.how.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Greeting {

    @Id
    @Column(length = 20)
    private String language;

    @Id
    @Column(length = 20)
    private String greeting;

    @Column(length = 40)
    private String translation;

    public String getLanguage() {
        return language;
    }

    public String getGreeting() {
        return greeting;
    }

    public String getTranslation() {
        return translation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Greeting greeting1 = (Greeting) o;
        return Objects.equals(getLanguage(), greeting1.getLanguage()) &&
                Objects.equals(getGreeting(), greeting1.getGreeting()) &&
                Objects.equals(getTranslation(), greeting1.getTranslation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLanguage(), getGreeting(), getTranslation());
    }
}
