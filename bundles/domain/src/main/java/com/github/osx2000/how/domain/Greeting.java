package com.github.osx2000.how.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="Greeting")
public class Greeting implements java.io.Serializable {

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

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
