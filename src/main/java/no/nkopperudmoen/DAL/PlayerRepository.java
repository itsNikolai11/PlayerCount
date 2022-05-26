package no.nkopperudmoen.DAL;

import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class PlayerRepository {
    private final Connection connection;

    public PlayerRepository(Connection connection) {
        this.connection = connection;
        createTables();
    }

    public void createTables() {
        String players = "CREATE TABLE IF NOT EXISTS pc_players (ID integer primary key autoincrement, UUID varchar, JOINED long, LASTSEEN long);";
        String uuidMap = "CREATE TABLE IF NOT EXISTS pc_uuidMap (UUID varchar primary key, NAME varchar);";
        try {
            Statement statement = connection.createStatement();
            statement.execute(players);
            statement.execute(uuidMap);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void savePlayerOnFirstJoin(Player p) {
        String sql = "INSERT INTO pc_players(UUID, JOINED, LASTSEEN) VALUES(?,?,?);";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, p.getUniqueId().toString());
            statement.setLong(2, System.currentTimeMillis());
            statement.setLong(3, System.currentTimeMillis());
            TransactionHandler.addToQueue(statement);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void updateLastOnline(Player p) {
        String sql = "UPDATE pc_players SET LASTSEEN = ? WHERE UUID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, System.currentTimeMillis());
            statement.setString(2, p.getUniqueId().toString());
            TransactionHandler.addToQueue(statement);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    public void insertPlayerName(String name, UUID uuid) {
        String sql = "INSERT INTO pc_uuidMap (UUID, NAME) VALUES(?,?);";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            TransactionHandler.addToQueue(statement);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void updatePlayerName(String name, UUID uuid) {
        String sql = "UPDATE pc_uuidMap SET NAME = ? WHERE UUID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, uuid.toString());
            TransactionHandler.addToQueue(statement);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    public UUID getUUID(String name) {
        String sql = "SELECT UUID FROM pc_uuidMap WHERE UPPER(NAME) = '" + name.toUpperCase() + "';";
        UUID retreivedUUID = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                retreivedUUID = UUID.fromString(result.getString("UUID"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return retreivedUUID;
    }

    public boolean existsInPlayerMap(UUID uuid) {
        String sql = "SELECT NAME  FROM pc_uuidMap WHERE UUID = '" + uuid.toString() + "';";
        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            return results.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public String getName(UUID uuid) {
        String sql = "SELECT NAME FROM pc_uuidMap WHERE UUID = '" + uuid.toString() + "';";
        String retreivedName = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                retreivedName = result.getString("NAME");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return retreivedName;
    }

    public long getFirstJoined(UUID uuid) {
        String sql = "SELECT JOINED FROM pc_players WHERE UUID = '" + uuid.toString() + "';";
        long firstJoined = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                firstJoined = result.getLong("JOINED");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return firstJoined;
    }

    public long getLastOnline(UUID uuid) {
        String sql = "SELECT LASTSEEN FROM pc_players WHERE UUID = '" + uuid.toString() + "';";
        long lastOnline = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                lastOnline = result.getLong("LASTSEEN");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return lastOnline;
    }

    public int getPlayerNumber(UUID uuid) {
        String sql = "SELECT ID FROM pc_players WHERE UUID = '" + uuid.toString() + "';";
        int joinedAs = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                joinedAs = result.getInt("ID");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return joinedAs;
    }

}
