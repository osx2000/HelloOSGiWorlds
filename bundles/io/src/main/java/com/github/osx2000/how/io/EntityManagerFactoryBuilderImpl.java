package com.github.osx2000.how.io;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.util.*;

@Component(service=EntityManagerFactoryBuilder.class)
public class EntityManagerFactoryBuilderImpl implements EntityManagerFactoryBuilder {
    private static final String JAVAX_PERSISTENCE_JDBC_DRIVER = "javax.persistence.jdbc.driver";
    private static final String JAVAX_PERSISTENCE_JTA_DATASOURCE = "javax.persistence.jtaDataSource";
    private static final String JAVAX_PERSISTENCE_NON_JTA_DATASOURCE = "javax.persistence.nonJtaDataSource";
    private static final String JAVAX_PERSISTENCE_TX_TYPE = "javax.persistence.transactionType";

    public static final String HELLO_persistenceUnitName = "persistenceUnitName";
    public static final String HELLO_managedClassNames = "managedClassNames";
    public static final String HELLO_persistenceUnitProperties = "persistenceUnitProperties";


    private PersistenceProvider provider;

    @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC)
    protected void setPersistenceProvider(PersistenceProvider pp) {
        this.provider = pp;
    }


    @Override
    public EntityManagerFactory createEntityManagerFactory(Map<String, Object> props) {
        props = new HashMap<String, Object>(props);

        String persistenceUnitName;
        List<String> managedClassNames;
        Properties persistenceUnitProperties;

        Object o = props.get(HELLO_persistenceUnitName);
        if(o instanceof String) {
            persistenceUnitName = (String) o;
            props.remove(HELLO_persistenceUnitName);
        } else {
            throw new IllegalArgumentException("No " + HELLO_persistenceUnitName + " found");
        }

        o = props.get(HELLO_managedClassNames);
        if(o instanceof List<?>) {
            managedClassNames = (List<String>) o;
            props.remove(HELLO_managedClassNames);
        } else {
            managedClassNames = new ArrayList<String>();
        }

        o = props.get(HELLO_persistenceUnitProperties);
        if(o instanceof Properties) {
            persistenceUnitProperties = (Properties) o;
            props.remove(HELLO_persistenceUnitProperties);
        } else {
            persistenceUnitProperties = new Properties();
        }

        PersistenceUnit persistenceUnit = null;

        // we are in OSGi conmtainer
        Bundle bundle =
                BundleReference.class.cast(EntityManagerFactoryBuilderImpl.class.getClassLoader())
                        .getBundle();

        persistenceUnit = new PersistenceUnit(bundle,persistenceUnitName,PersistenceUnitTransactionType.JTA);



        persistenceUnit.setExcludeUnlisted(true);
        for(String m : managedClassNames) {
            persistenceUnit.addClassName(m);
        }

        for(Object e : persistenceUnitProperties.keySet()) {
            persistenceUnit.addProperty((String)e,(String)persistenceUnitProperties.get(e));
        }

        //PersistenceUnitInfoImpl persistenceUnit = new PersistenceUnitInfoImpl(persistenceUnitName,managedClassNames,persistenceUnitProperties);

        String driver = (String)props.get(JAVAX_PERSISTENCE_JDBC_DRIVER);
        if (driver == null) {
            throw new IllegalArgumentException("No driver found");
        }

        // Handle overridden datasources in a provider agnostic way
        // This isn't necessary for EclipseLink, but Hibernate and
        // OpenJPA both need some extra help.
        o = props.get(JAVAX_PERSISTENCE_JTA_DATASOURCE);
        if(o instanceof DataSource) {
            persistenceUnit.setJtaDataSource((DataSource) o);
            props.remove(JAVAX_PERSISTENCE_JTA_DATASOURCE);
        }

        o = props.get(JAVAX_PERSISTENCE_NON_JTA_DATASOURCE);
        if(o instanceof DataSource) {
            persistenceUnit.setNonJtaDataSource((DataSource) o);
            props.remove(JAVAX_PERSISTENCE_NON_JTA_DATASOURCE);
        }

        o = props.get(JAVAX_PERSISTENCE_TX_TYPE);
        if(o instanceof PersistenceUnitTransactionType) {
            persistenceUnit.setTransactionType((PersistenceUnitTransactionType) o);
        } else if (o instanceof String) {
            persistenceUnit.setTransactionType(
                    PersistenceUnitTransactionType.valueOf((String) o));
        }

        return provider.createContainerEntityManagerFactory(persistenceUnit, props);
    }
}
