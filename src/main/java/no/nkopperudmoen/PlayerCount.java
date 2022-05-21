package no.nkopperudmoen;

import no.nkopperudmoen.DAL.DatabaseConnection;
import no.nkopperudmoen.DAL.PlayerRepository;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerCount extends JavaPlugin {
    public static void main(String[] args) {
    }

    private final PluginManager pm = Bukkit.getPluginManager();
    private final FileConfiguration config = getConfig();
    private PlayerRepository repo;
    private Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();
        saveConfig();
        setupDatabaseConnection();
        registerEvents();
        registerCommands();
        loadLang();
    }

    @Override
    public void onDisable() {
        logger.info("PlayerCount disabled.");
    }

    private void setupDatabaseConnection() {
        DatabaseConnection databaseConnection;

        try {
            databaseConnection = new DatabaseConnection();
            repo = new PlayerRepository(databaseConnection.getConnection());
            logger.info("Database koblet til!");
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Databasefeil! Innlasting av plugin avbrytes..");
            System.out.println(exception.getMessage());
            this.onDisable();
        }

    }

    private void registerCommands() {

    }

    private void registerEvents() {

    }

    private void loadLang() {

    }

}
