package ru.patsiorin.otus;

import org.junit.Test;
import ru.patsiorin.otus.orm.ReflectiveQueryBuilder;
import ru.patsiorin.otus.orm.UserDataSet;

import static org.junit.Assert.assertEquals;

public class FormQueryTest {

    @Test
    public void testInsertUserQuery() {
        UserDataSet user = new UserDataSet(0, "Alex", 42);
        String expected = "INSERT INTO `user` (name, age) VALUES ('Alex', 42)";
        String result = new ReflectiveQueryBuilder<>(user).buildInsertOrUpdateQuery();
        assertEquals(expected, result);
    }

    @Test
    public void testSelectQuery() {
        String expected = "SELECT * FROM `user` WHERE id = 2";
        String result = new ReflectiveQueryBuilder(UserDataSet.class).formSelectQuery(2);
        assertEquals(expected, result);
    }

    @Test
    public void testUpdateUserQuery() {
        UserDataSet user = new UserDataSet(2, "Julia", 26);
        String expected = "UPDATE `user` SET name='Julia', age=26 WHERE id = 2";
        String result = new ReflectiveQueryBuilder<>(user).buildInsertOrUpdateQuery();
        assertEquals(expected, result);
    }
}
