package no.nkopperudmoen.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;
    private static DatabaseConnection instance = null;

    private DatabaseConnection() throws SQLException {
        connection = connect();
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:plugins/PlayerCount/players.db";
        Connection conn;

        conn = DriverManager.getConnection(url);

        return conn;
    }

    public Connection getConnection() {
        return connection;
    }
}
