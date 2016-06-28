package com.github.osx2000.how.application.impl;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Hello World Configuration",
        description = "The configuration for the hello world component.")
@interface AppConfig {
    @AttributeDefinition(name="Welcome Message", description="This message is displayed on startup of the component.")
    String welcome_message() default "Hello World!";
}
