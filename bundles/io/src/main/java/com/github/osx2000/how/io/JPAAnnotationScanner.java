package com.github.osx2000.how.io;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.Collection;

class JPAAnnotationScanner {
    private static final Logger LOG = LoggerFactory.getLogger(JPAAnnotationScanner.class);

    private JPAAnnotationScanner() {
    }

    public static Collection<String> findJPAAnnotatedClasses(Bundle b) {
        BundleWiring bw = b.adapt(BundleWiring.class);
        Collection<String> resources = bw.listResources("/", "*.class",
                BundleWiring.LISTRESOURCES_LOCAL | BundleWiring.LISTRESOURCES_RECURSE);

        Collection<String> classes = new ArrayList<String>();
        ClassLoader cl = new TempBundleDelegatingClassLoader(b, JPAAnnotationScanner.class.getClassLoader());
        for(String s : resources) {
            s = s.replace('/', '.').substring(0, s.length() - 6);
            try {
                Class<?> clazz = Class.forName(s, false, cl);

                if(clazz.isAnnotationPresent(Entity.class) ||
                        clazz.isAnnotationPresent(MappedSuperclass.class) ||
                        clazz.isAnnotationPresent(Embeddable.class)) {
                    classes.add(s);
                }

            } catch (ClassNotFoundException e) {
                logEx(e);
            } catch (NoClassDefFoundError e) {
                logEx(e);
            }
        }
        return classes;
    }

    private static void logEx(Throwable e) {
        LOG.debug("Exception while scanning for JPA annotations", e);
    }
}
