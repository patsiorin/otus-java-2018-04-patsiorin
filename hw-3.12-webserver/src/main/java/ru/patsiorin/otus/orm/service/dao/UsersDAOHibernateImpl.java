package ru.patsiorin.otus.orm.service.dao;

import org.hibernate.Session;
import ru.patsiorin.otus.orm.model.DataSet;


public class UsersDAOHibernateImpl implements UsersDAO {
    private final Session session;

    public UsersDAOHibernateImpl(Session session) {
        this.session = session;
    }

    @Override
    public <T extends DataSet> void save(T dataSet) {
        //session.saveOrUpdate(dataSet);
        if (dataSet.getId() < 0) {
            session.save(dataSet);
        } else {
            session.update(dataSet);
        }
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> cl) {
        // to avoid org.hibernate.LazyInitializationException
        // hibernate.enable_lazy_load_no_trans must be set to true
        // people on the internet say it's a bad practice though
        // https://stackoverflow.com/a/25367976
        return session.load(cl, id);
    }


}
