package com.github.osx2000.how.service.impl;

import com.github.osx2000.how.interfaces.Echo;
import org.osgi.service.component.annotations.Component;

@Component(service = Echo.class)
public class EchoImpl implements Echo {

    public String getEcho(String in) {
        return in;
    }
}
