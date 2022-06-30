package no.nkopperudmoen.UTIL;

import no.nkopperudmoen.PlayerCount;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import static no.nkopperudmoen.UTIL.MessagePreProcessor.colorize;

public class MESSAGES {
    public static Plugin plugin = PlayerCount.getPlugin(PlayerCount.class);
    public static FileConfiguration config = plugin.getConfig();
    static FileConfiguration lang = PlayerCount.getLanguageFile();

    public static String PLAYERINFO = colorize(lang.getString("PlayerInfo"));
    public static String PLAYER_NOT_FOUND = colorize(lang.getString("PlayerNotFound"));
    public static String NO_PERMISSION = colorize(lang.getString("NoAccess"));
    public static String NOW = colorize(lang.getString("Now"));
    public static String JOINED_FOR_FIRST_TIME = colorize(lang.getString("FirstJoinMsg"));


}
