package ru.patsiorin.otus.orm.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.patsiorin.otus.orm.model.AddressDataSet;
import ru.patsiorin.otus.orm.model.DataSet;
import ru.patsiorin.otus.orm.model.PhoneDataSet;
import ru.patsiorin.otus.orm.model.UserDataSet;
import ru.patsiorin.otus.orm.service.dao.UsersDAO;
import ru.patsiorin.otus.orm.service.dao.UsersDAOHibernateImpl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {
    private final SessionFactory sessionFactory;
    private static final String HIBERNATE_CONFIG = "HibernateConfig.properties";

    public DBServiceHibernateImpl() {
        try {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(UserDataSet.class);
            configuration.addAnnotatedClass(PhoneDataSet.class);
            configuration.addAnnotatedClass(AddressDataSet.class);
            configuration.addProperties(loadProperties());
            sessionFactory = createSessionFactory(configuration);
        } catch (IOException e) {
            throw new RuntimeException("Can't load HibernateConfig.properties: " + e.getMessage());
        }
    }

    @Override
    public <T extends DataSet> void save(T dataSet) {
        runInSession(session -> {
            UsersDAO dao = new UsersDAOHibernateImpl(session);
            dao.save(dataSet);
            return null;
        });
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> dataSetClass) {
        return runInSession(session -> {
            UsersDAO dao = new UsersDAOHibernateImpl(session);
            return dao.load(id, dataSetClass);
        });
    }

    @Override
    public void shutdown() {
        sessionFactory.close();
    }

    private SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        StandardServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        FileReader fr = new FileReader(new File(classLoader.getResource(HIBERNATE_CONFIG).getFile()));
        properties.load(fr);
        return properties;
    }

    private <R> R runInSession(Function<Session, R> fun) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = fun.apply(session);
            transaction.commit();
            return result;
        }
    }

}
