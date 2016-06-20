package com.github.osx2000.how.io;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

public class PersistenceUnit implements PersistenceUnitInfo {

    private Bundle bundle;
    private ClassLoader classLoader;
    private Set<String> classNames;
    private boolean excludeUnlisted;
    private DataSource jtaDataSource;
    private String jtaDataSourceName;
    private DataSource nonJtaDataSource;
    private String nonJtaDataSourceName;
    private String persistenceProviderClassName;
    private String persistenceUnitName;
    private String persistenceXMLSchemaVersion;
    private Properties props;
    private SharedCacheMode sharedCacheMode = SharedCacheMode.UNSPECIFIED;
    private PersistenceUnitTransactionType transactionType;
    private ValidationMode validationMode = ValidationMode.NONE;

    public PersistenceUnit(Bundle bundle, String persistenceUnitName,
                           PersistenceUnitTransactionType transactionType) {
        this.bundle = bundle;
        this.persistenceUnitName = persistenceUnitName;
        this.transactionType = transactionType;
        this.props = new Properties();
        this.classLoader = bundle.adapt(BundleWiring.class).getClassLoader();
        this.classNames = new HashSet<String>();
    }

    public void addClassName(String className) {
        this.classNames.add(className);
    }

    public void addProperty(String name, String value) {
        props.put(name, value);
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return this.excludeUnlisted;
    }

    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override
    public List<URL> getJarFileUrls() {
        return Collections.emptyList();
    }

    @Override
    public DataSource getJtaDataSource() {
        return this.jtaDataSource;
    }

    public String getJtaDataSourceName() {
        return jtaDataSourceName;
    }

    @Override
    public List<String> getManagedClassNames() {
        return new ArrayList<String>(classNames);
    }

    @Override
    public List<String> getMappingFileNames() {
        return Collections.emptyList();
    }

    public String getName() {
        return persistenceUnitName;
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return new TempBundleDelegatingClassLoader(bundle, classLoader);
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return this.nonJtaDataSource;
    }

    public String getNonJtaDataSourceName() {
        return nonJtaDataSourceName;
    }

    @Override
    public String getPersistenceProviderClassName() {
        return this.persistenceProviderClassName;
    }

    @Override
    public String getPersistenceUnitName() {
        return this.persistenceUnitName;
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return bundle.getResource("/");
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return this.persistenceXMLSchemaVersion;
    }

    @Override
    public Properties getProperties() {
        return this.props;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return this.sharedCacheMode;
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return transactionType;
    }

    @Override
    public ValidationMode getValidationMode() {
        return this.validationMode;
    }

    public boolean isExcludeUnlisted() {
        return excludeUnlisted;
    }

    public void setExcludeUnlisted(boolean excludeUnlisted) {
        this.excludeUnlisted = excludeUnlisted;
    }

    public void setJtaDataSource(DataSource jtaDataSource) {
        this.jtaDataSource = jtaDataSource;
    }

    public void setJtaDataSourceName(String jtaDataSourceName) {
        this.jtaDataSourceName = jtaDataSourceName;
    }

    public void setNonJtaDataSource(DataSource nonJtaDataSource) {
        this.nonJtaDataSource = nonJtaDataSource;
    }

    public void setNonJtaDataSourceName(String nonJtaDataSourceName) {
        this.nonJtaDataSourceName = nonJtaDataSourceName;
    }

    public void setProviderClassName(String providerClassName) {
        this.persistenceProviderClassName = providerClassName;
    }

    public void setSharedCacheMode(SharedCacheMode sharedCacheMode) {
        this.sharedCacheMode = sharedCacheMode;
    }

    public void setValidationMode(ValidationMode validationMode) {
        this.validationMode = validationMode;
    }

    public void addAnnotated() {
        if (!excludeUnlistedClasses()) {
            Collection<String> detected = JPAAnnotationScanner.findJPAAnnotatedClasses(bundle);
            for (String name : detected) {
                addClassName(name);
            }
        }
    }

    public void setTransactionType(PersistenceUnitTransactionType transactionType) {
        this.transactionType = transactionType;
    }
}