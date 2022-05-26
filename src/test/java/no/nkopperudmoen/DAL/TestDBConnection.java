package no.nkopperudmoen.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    private final Connection connection;

    public TestDBConnection() throws SQLException {
        connection = connect();
    }

    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite::memory:";
        Connection conn;

        conn = DriverManager.getConnection(url);

        return conn;
    }

    public Connection getConnection() {
        return connection;
    }
}
