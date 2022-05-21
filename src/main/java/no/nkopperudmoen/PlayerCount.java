package no.nkopperudmoen;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PlayerCount extends JavaPlugin {
    public static void main(String[] args) {
    }

    private final PluginManager pm = Bukkit.getPluginManager();
    private Logger logger;

    @Override
    public void onEnable() {
        System.out.println("PlayerCount loaded!");
        logger = getLogger();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        logger.info("PlayerCount disabled.");
        super.onDisable();
    }

}
