package ru.patsiorin.otus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.patsiorin.otus.orm.db.ConnectionSingleton;
import ru.patsiorin.otus.orm.service.DBService;
import ru.patsiorin.otus.orm.service.DBServiceReflectiveImpl;
import ru.patsiorin.otus.orm.model.UserDataSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ExecutorTest {
    private DBService dbService;

    @Before
    public void startUp() {
        dbService = new DBServiceReflectiveImpl();
        createUserTable();

    }

    private void dropUserTable() {
        try (Connection connection = ConnectionSingleton.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE `user`");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUserTable() {
        try (Connection connection = ConnectionSingleton.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS `user` (\n" +
                    "  id bigint(20) NOT NULL primary key auto_increment,\n" +
                    "  name varchar(255),\n" +
                    "  age int(3)\n" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveUserDataSet() {
        UserDataSet userDataSetExpected = new UserDataSet("John Doe", 33);
        dbService.save(userDataSetExpected);
        UserDataSet userDataSetResult = dbService.load(1, UserDataSet.class);
        assertEquals(userDataSetExpected, userDataSetResult);

    }

    @Test
    public void testLoadUserDataSet() {
        UserDataSet userDataSet1 = new UserDataSet("user1", 11);
        UserDataSet userDataSet2 = new UserDataSet("user2", 22);
        dbService.save(userDataSet1);
        dbService.save(userDataSet2);

        assertEquals(userDataSet1, dbService.load(1, UserDataSet.class));
        assertEquals(userDataSet2, dbService.load(2, UserDataSet.class));
    }

    @Test
    public void testUpdateUserDataSet() {
        UserDataSet userDataSet = new UserDataSet("user1", 11);
        dbService.save(userDataSet);
        UserDataSet loadedUser = dbService.load(1, UserDataSet.class);
        loadedUser.setName("updated");
        assertNotEquals(loadedUser, dbService.load(1, UserDataSet.class));
        dbService.save(loadedUser);
        assertEquals(loadedUser, dbService.load(1, UserDataSet.class));
    }

    @After
    public void close() {
        dropUserTable();
    }
}
