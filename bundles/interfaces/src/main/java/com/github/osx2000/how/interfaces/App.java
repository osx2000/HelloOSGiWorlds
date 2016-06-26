package com.github.osx2000.how.interfaces;

import org.osgi.service.metatype.annotations.Designate;

public interface App {
    String getGreeting(String language, String greeting);

    String postGreeting(String language, String greeting, String translation);
}
