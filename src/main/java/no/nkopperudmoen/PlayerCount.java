package no.nkopperudmoen;

import no.nkopperudmoen.Commands.Ontime;
import no.nkopperudmoen.Commands.Spiller;
import no.nkopperudmoen.Commands.TabCompleters.OntimeTabCompleter;
import no.nkopperudmoen.Commands.TabCompleters.PlayerNameTabCompleter;
import no.nkopperudmoen.DAL.DatabaseConnection;
import no.nkopperudmoen.DAL.PlayerRepository;
import no.nkopperudmoen.Listeners.ExecuteCommandOnJoin;
import no.nkopperudmoen.Listeners.PlayerOntimeListener;
import no.nkopperudmoen.Listeners.PlayerTimeListener;
import no.nkopperudmoen.Tasks.OntimeTaskManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerCount extends JavaPlugin {
    public static void main(String[] args) {
    }

    private final PluginManager pm = Bukkit.getPluginManager();
    private static FileConfiguration lang;
    private final PluginDescriptionFile pdf = getDescription();
    private final FileConfiguration config = getConfig();

    private Logger logger;

    @Override
    public void onEnable() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            restartOntimeTasksOnReload();
        }
        logger = getLogger();
        logger.log(Level.INFO, "Bruker API-versjon " + pdf.getAPIVersion());
        config.options().copyDefaults(true);
        saveConfig();
        ensureDatabaseConnectionOK();
        registerEvents();
        registerCommands();
        loadLang();
        logger.log(Level.INFO, "Plugin lastet inn suksessfullt! Versjon " + pdf.getVersion());
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            OntimeTaskManager.cancelTask(p.getUniqueId());
        }
        try {
            DatabaseConnection.getInstance().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("PlayerCount stoppet.");
    }

    private void ensureDatabaseConnectionOK() {
        try {
            PlayerRepository.getInstance();
            logger.log(Level.INFO, "Koblet til database");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Kunne ikke koble til database! Innlasting av plugin avbrytes..");
            this.onDisable();
        }

    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("spiller")).setExecutor(new Spiller());
        Objects.requireNonNull(getCommand("spiller")).setTabCompleter(new PlayerNameTabCompleter());
        Objects.requireNonNull(getCommand("ontime")).setExecutor(new Ontime());
        Objects.requireNonNull(getCommand("ontime")).setTabCompleter(new OntimeTabCompleter());
    }

    private void registerEvents() {
        pm.registerEvents(new PlayerTimeListener(), this);
        pm.registerEvents(new ExecuteCommandOnJoin(this), this);
        pm.registerEvents(new PlayerOntimeListener(), this);
    }

    public void loadLang() {
        File langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            langFile.getParentFile().mkdirs();
            saveResource("lang.yml", false);
        }
        lang = new YamlConfiguration();
        try {
            saveResource("lang.yml", false);
            lang.load(langFile);


        } catch (IOException | InvalidConfigurationException e) {
            logger.log(Level.SEVERE, "Kunne ikke laste språkfil! Sjekk at filen eksisterer og ikke er korrupt");
            this.onDisable();
        }
    }

    public static FileConfiguration getLanguageFile() {
        return lang;
    }

    public void restartOntimeTasksOnReload() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            OntimeTaskManager.createTask(p.getUniqueId());
        }
    }

}
