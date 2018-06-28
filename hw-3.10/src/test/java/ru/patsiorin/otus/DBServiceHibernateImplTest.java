package ru.patsiorin.otus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.patsiorin.otus.orm.model.AddressDataSet;
import ru.patsiorin.otus.orm.model.PhoneDataSet;
import ru.patsiorin.otus.orm.model.UserDataSet;
import ru.patsiorin.otus.orm.service.DBService;
import ru.patsiorin.otus.orm.service.DBServiceHibernateImpl;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DBServiceHibernateImplTest {
    private DBService dbService;
    @Before
    public void setUp() {
        dbService = new DBServiceHibernateImpl();
    }

    @Test
    public void testSaveAndLoadUserDataSet() {
        UserDataSet userDataSetExpected = new UserDataSet("John Doe", 33);
        dbService.save(userDataSetExpected);

        UserDataSet userDataSetResult = dbService.load(1, UserDataSet.class);

        // hibernate returns a proxy object, so I can't use equals on objects
        assertEquals(userDataSetExpected.getId(), userDataSetResult.getId());
        assertEquals(userDataSetExpected.getAge(), userDataSetResult.getAge());
        assertEquals(userDataSetExpected.getName(), userDataSetResult.getName());

    }

    @Test
    public void testSaveAndLoadUserWithPhones() {
        UserDataSet expected = new UserDataSet("Vasia", 43);
        expected.setPhones(Arrays.asList(new PhoneDataSet("123"), new PhoneDataSet("321")));
        expected.setAddress(new AddressDataSet("Maple street"));
        dbService.save(expected);

        UserDataSet result = dbService.load(1, UserDataSet.class);

        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getPhones().size(), result.getPhones().size());
        assertEquals("123", result.getPhones().get(0).getNumber());
        assertEquals("321", result.getPhones().get(1).getNumber());
        assertEquals(expected.getAddress(), result.getAddress());
    }

    @Test
    public void testSaveAndLoadWithEmptyPhoneAndAddress() {
        UserDataSet expected = new UserDataSet("Vasia", 43);
        dbService.save(expected);

        UserDataSet result = dbService.load(1, UserDataSet.class);

        assertEquals(expected.getName(), result.getName());
        assertEquals(0, result.getPhones().size());
        assertNull(result.getAddress());
    }

    @Test
    public void testUpdateUser() {
        UserDataSet user = new UserDataSet("Vavasia", 35);
        dbService.save(user);
        user = dbService.load(1, UserDataSet.class);
        user.setAddress(new AddressDataSet("Podmostom"));
        user.setPhones(Arrays.asList(new PhoneDataSet("111")));
        dbService.save(user);

        UserDataSet result = dbService.load(1, UserDataSet.class);
        assertEquals("Vavasia", result.getName());
        assertEquals(1, result.getId());
        assertEquals("Podmostom", result.getAddress().getStreet());
        assertEquals("111", result.getPhones().get(0).getNumber());
    }

    @After
    public void close() {
        dbService.shutdown();
    }
}
