package com.github.osx2000.how.application;

import com.github.osx2000.how.domain.Greeting;
import com.github.osx2000.how.io.EntityManagerSupplier;
import com.google.gson.Gson;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.sql.SQLException;


@Component(service=App.class)
public class AppImpl implements App {

    @Reference
    EntityManagerSupplier ems;

    EntityManagerFactory emf;

    static Gson gson = new Gson();

    @Activate
    private void startup() throws SQLException {
        Class<?>[] entities = new Class[]{com.github.osx2000.how.domain.Greeting.class};
        emf = ems.buildEntityManagerFactory("PUName",entities,"jdbc:h2:mem:test","sa","");
    }

    @Override
    public String getGreeting(String language, String greeting) {
        EntityManager em = emf.createEntityManager();
        final Query query = em.createQuery("SELECT c FROM com.github.osx2000.how.domain.Greeting c WHERE language=:language AND greeting=:greeting");
        query.setParameter("language",language);
        query.setParameter("greeting",greeting);
        final Object o = query.getResultList().get(0);
        return gson.toJson(o);
    }

    @Override
    public String postGreeting(String language, String greeting, String translation) {
        Greeting g = new Greeting();
        g.setGreeting(greeting);
        g.setLanguage(language);
        g.setTranslation(translation);
        EntityManager em = emf.createEntityManager();
        em.persist(g);
        return gson.toJson(g);
    }
}
