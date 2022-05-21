package no.nkopperudmoen.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection() throws SQLException {
        connection = connect();
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
