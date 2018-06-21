package ru.patsiorin.otus;

import org.h2.engine.User;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.patsiorin.otus.orm.ConnectionHelper;
import ru.patsiorin.otus.orm.DBService;
import ru.patsiorin.otus.orm.DBServiceReflectiveImpl;
import ru.patsiorin.otus.orm.UserDataSet;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ExecutorTest {
    private Server server;
    private DBService dbService = new DBServiceReflectiveImpl();
    private Connection connection;
    @Before
    public void startUp() {
        try {
            server = Server.createTcpServer("-tcpPort", "9123", "-tcpAllowOthers").start();
            connection = DriverManager.getConnection("jdbc:h2:mem:test");
            createUserTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropUserTable() {
        Connection connection = ConnectionHelper.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE `user`");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUserTable() throws SQLException {
        Connection connection = ConnectionHelper.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS `user` (\n" +
                "  id bigint(20) NOT NULL primary key auto_increment,\n" +
                "  name varchar(255),\n" +
                "  age int(3)\n" +
                ");");
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
        server.stop();
    }

}
