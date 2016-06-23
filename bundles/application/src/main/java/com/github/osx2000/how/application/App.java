package com.github.osx2000.how.application;

/**
 * Created by xne0133 on 23.06.2016.
 */
public interface App {
    String getGreeting(String language, String greeting);

    String postGreeting(String language, String greeting, String translation);
}
