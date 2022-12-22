package no.nkopperudmoen.DAL;

import no.nkopperudmoen.DAL.Models.PlayerOntime;
import no.nkopperudmoen.PlayerCount;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerRepository {
    private final Connection connection;
    private static PlayerRepository repository = null;

    private PlayerRepository(Connection connection) {
        this.connection = connection;
        createTables();
    }

    /**
     * Standard-method to get the repository-instance with the default database-connection
     *
     * @return The repository-instance with the default database-connection
     */
    public static PlayerRepository getInstance() throws SQLException {
        if (repository == null) {
            repository = new PlayerRepository(DatabaseConnection.getInstance().getConnection());
        }
        return repository;
    }

    /***
     * Used to give the repository-instance a database-connection different from the standard one
     * (Used for unit-testing)
     * @param connection The database-connection to be used
     * @return Repository-instance with the specified connection
     */
    public static PlayerRepository getInstance(Connection connection) {
        repository = new PlayerRepository(connection);
        return repository;
    }

    public void createTables() {
        String players = "CREATE TABLE IF NOT EXISTS pc_players (ID integer primary key autoincrement, UUID varchar, JOINED long, LASTSEEN long);";
        String ontime = "CREATE TABLE IF NOT EXISTS pc_ontime (UUID varchar primary key, ONTIME integer);";
        String uuidMap = "CREATE TABLE IF NOT EXISTS pc_uuidMap (UUID varchar primary key, NAME varchar);";
        try {
            Statement statement = connection.createStatement();
            statement.execute(players);
            statement.execute(ontime);
            statement.execute(uuidMap);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void populateOntime(UUID uuid) {
        String sql = "INSERT INTO pc_ontime(UUID, ONTIME) VALUES(?,?);";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());
            statement.setInt(2, 0);
            TransactionHandler.addToQueue(statement);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void updateOntime(UUID uuid, int ontime) {
        String sql = "UPDATE pc_ontime SET ONTIME = ? WHERE UUID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ontime);
            statement.setString(2, uuid.toString());
            TransactionHandler.addToQueue(statement);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public int getOntime(UUID uuid) {
        int ontime = -1;
        String sql = "SELECT * FROM pc_ontime WHERE UUID = '" + uuid.toString() + "';";
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                ontime = result.getInt("ONTIME");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return ontime;
    }

    public List<PlayerOntime> getTopOntime() {
        List<PlayerOntime> topOntime = new ArrayList<>();
        String sql = "SELECT po.UUID, po.ONTIME, um.NAME FROM pc_ontime as po INNER JOIN pc_uuidMap as um ON po.UUID = um.UUID GROUP BY po.UUID, po.ONTIME, um.NAME ORDER BY po.ONTIME DESC LIMIT 10";
        try {
            ResultSet results = connection.createStatement().executeQuery(sql);
            while (results.next()) {
                topOntime.add(new PlayerOntime(results.getInt("ONTIME"), results.getString("NAME")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return topOntime;
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

    public ArrayList<String> getMapPlayerNames() {
        ArrayList<String> names = new ArrayList<>();
        String sql = "SELECT NAME FROM pc_uuidMap";
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                names.add(result.getString("NAME"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return names;
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
