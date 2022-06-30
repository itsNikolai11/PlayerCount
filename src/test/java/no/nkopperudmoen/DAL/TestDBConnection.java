package no.nkopperudmoen.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    private Connection connection;
    private static TestDBConnection instance = null;

    private TestDBConnection() throws SQLException {
        connection = connect();
    }

    public static TestDBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new TestDBConnection();
        }
        return instance;
    }

    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite::memory:";
        Connection conn;

        conn = DriverManager.getConnection(url);

        return conn;
    }
}
