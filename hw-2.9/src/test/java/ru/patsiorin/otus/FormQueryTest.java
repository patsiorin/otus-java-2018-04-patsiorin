package ru.patsiorin.otus;

import org.junit.Test;
import ru.patsiorin.otus.orm.db.QueryWithData;
import ru.patsiorin.otus.orm.db.ReflectiveQueryBuilder;
import ru.patsiorin.otus.orm.model.UserDataSet;

import static org.junit.Assert.assertEquals;

public class FormQueryTest {

    @Test
    public void testInsertUserQuery() {
        UserDataSet user = new UserDataSet(0, "Alex", 42);
        String expected = "INSERT INTO `user` (name, age) VALUES (?, ?)";
        QueryWithData result = new ReflectiveQueryBuilder<>(user).buildInsertOrUpdateQuery();
        assertEquals(expected, result.getQuery());
        assertEquals(new Object[] {"Alex", 42}, result.getValues());
    }

    @Test
    public void testSelectQuery() {
        String expected = "SELECT * FROM `user` WHERE id = ?";
        QueryWithData result = new ReflectiveQueryBuilder(UserDataSet.class).formSelectQuery(2);
        assertEquals(expected, result.getQuery());
        assertEquals(new Object[] {2L}, result.getValues());
    }

    @Test
    public void testUpdateUserQuery() {
        UserDataSet user = new UserDataSet(2, "Julia", 26);
        String expected = "UPDATE `user` SET name = ?, age = ? WHERE id = ?";
        QueryWithData result = new ReflectiveQueryBuilder<>(user).buildInsertOrUpdateQuery();
        assertEquals(expected, result.getQuery());
        assertEquals(new Object[] {"Julia", 26, 2L}, result.getValues());
    }
}
