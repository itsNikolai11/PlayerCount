package no.nkopperudmoen;

import no.nkopperudmoen.Commands.Spiller;
import no.nkopperudmoen.DAL.DatabaseConnection;
import no.nkopperudmoen.DAL.PlayerController;
import no.nkopperudmoen.DAL.PlayerRepository;
import no.nkopperudmoen.Listeners.PlayerTimeListener;
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
    private PlayerController controller;
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
            PlayerRepository repo = new PlayerRepository(databaseConnection.getConnection());
            logger.info("Database koblet til!");
            controller = new PlayerController(repo);
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Databasefeil! Innlasting av plugin avbrytes..");
            this.onDisable();
        }

    }

    private void registerCommands() {
        getCommand("spiller").setExecutor(new Spiller(controller));

    }

    private void registerEvents() {
        pm.registerEvents(new PlayerTimeListener(controller), this);
    }

    private void loadLang() {

    }

}
