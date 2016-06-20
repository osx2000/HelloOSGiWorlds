package com.github.osx2000.how.io;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

@Component
public class EntityManagerSupplier {

    @Reference
    DataSourceFactory dsf;

    @Reference
    EntityManagerFactoryBuilder emfb;

}
