package ru.patsiorin.otus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.patsiorin.otus.orm.cache.CacheEngineSoftReference;
import ru.patsiorin.otus.orm.model.UserDataSet;
import ru.patsiorin.otus.orm.service.DBService;
import ru.patsiorin.otus.orm.service.DBServiceCachedImpl;
import ru.patsiorin.otus.orm.service.DBServiceHibernateImpl;

public class DBServiceCachedTest {
    private DBService<UserDataSet> dbService;
    @Before
    public void setUp() {
        dbService = new DBServiceCachedImpl<>(new DBServiceHibernateImpl<>(), new CacheEngineSoftReference<>(10, 30, 40, false));
    }

    @Test
    public void testSaveAndLoadUserDataSet() throws InterruptedException {
        UserDataSet userDataSetExpected = new UserDataSet("John Doe", 33);
        dbService.save(userDataSetExpected);
        for (int i = 0; i < 3; i++) {
            dbService.load(1, UserDataSet.class);
            Thread.sleep(1000);
        }
    }


    @After
    public void close() {
        dbService.shutdown();
    }
}
