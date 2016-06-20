package com.github.osx2000.how.io;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

import static javax.persistence.spi.PersistenceUnitTransactionType.JTA;

@Component(service = EntityManagerSupplier.class )
public class EntityManagerSupplier {

    @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            target = "(osgi.jdbc.driver.name=H2 JDBC Driver)")
    DataSourceFactory dsf;

    @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC)
    EntityManagerFactoryBuilder emfb;

    private Map<String,Object> GetDBProps(String persistenceUnit,
                                          Class<?>[] entities,
                                          String jdbcUrl,
                                          String user,
                                          String password,
                                          String jtaPlatform,
                                          String dialect,
                                          String driver)
            throws SQLException {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(EntityManagerFactoryBuilderImpl.HELLO_persistenceUnitName,persistenceUnit);
        List<String> managedClasses = new ArrayList<>();
        for(Class<?> c : entities) {
            managedClasses.add(c.getCanonicalName());
        }
        props.put(EntityManagerFactoryBuilderImpl.HELLO_managedClassNames,managedClasses);
        Properties persistenceUnitProperties = new Properties();
        persistenceUnitProperties.put("hibernate.dialect",dialect);
        persistenceUnitProperties.put("hibernate.transaction.jta.platform",jtaPlatform);
        persistenceUnitProperties.put("hibernate.connection.url",jdbcUrl);
        persistenceUnitProperties.put("hibernate.connection.user",user);
        persistenceUnitProperties.put("hibernate.connection.password",password);
        persistenceUnitProperties.put("hibernate.connection.driver_class",driver);
        props.put(EntityManagerFactoryBuilderImpl.HELLO_persistenceUnitProperties, persistenceUnitProperties);
        props.put("javax.persistence.jdbc.driver",driver);
        props.put("javax.persistence.transactionType", JTA.name());

        return props;
    }

    private static Properties getJdbcProps() {
        Properties ret = new Properties();
        return ret;
    }


    public EntityManagerFactory buildEntityManagerFactory(String persistenceUnit,
                                                          Class<?>[] entities,
                                                          String url,
                                                          String user,
                                                          String password) throws SQLException {


        DataSource dataSource = null;

        String jtaPlatform = "org.hibernate.osgi.OsgiJtaPlatform";

        dataSource = dsf.createDataSource(getJdbcProps());


        Map<String,Object> props = GetDBProps(persistenceUnit,entities,url,user,password, jtaPlatform,"org.hibernate.dialect.H2Dialect","org.h2.Driver");

        props.put("javax.persistence.JtaDataSource",dataSource);
        return emfb.createEntityManagerFactory(props);

    }



    public Class<?>[] getEntities(String x) {
        switch (x) {
            case "a":
                return new Class<?>[]{};
            case "b":
                return new Class<?>[]{};
            default:
                throw new IllegalArgumentException("No Entities for target " + x);
        }
    }

}
