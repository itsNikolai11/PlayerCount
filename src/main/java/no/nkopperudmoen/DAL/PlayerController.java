package no.nkopperudmoen.DAL;

import no.nkopperudmoen.PlayerCount;
import no.nkopperudmoen.UTIL.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerController {
    private final PlayerRepository repo;
    private static PlayerController instance = null;

    private PlayerController(PlayerRepository repo) {
        this.repo = repo;
    }

    public static PlayerController getInstance() {
        if (instance == null) {
            try {
                instance = new PlayerController(PlayerRepository.getInstance());
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Databasetilkobling feilet! Disabler plugin..");
                Bukkit.getServer().getPluginManager().disablePlugin(PlayerCount.getPlugin(PlayerCount.class));
                return null;
            }
        }
        return instance;
    }

    public void updateOnJoin(Player p) {
        if (!p.hasPlayedBefore() || repo.getFirstJoined(p.getUniqueId()) == 0) {
            repo.savePlayerOnFirstJoin(p);
            repo.populateOntime(p.getUniqueId());
            return;
        }
        if (repo.getName(p.getUniqueId()).isEmpty()) {
            repo.insertPlayerName(p.getName(), p.getUniqueId());
        } else {
            repo.updatePlayerName(p.getName(), p.getUniqueId());
        }
        if (repo.getOntime(p.getUniqueId()) == -1) {
            repo.populateOntime(p.getUniqueId());
        }
        repo.updateLastOnline(p);
    }

    public void updateUUIDMap(Player p) {
        UUID fromUUIDMap = repo.getUUID(p.getName());
        if (fromUUIDMap != null && fromUUIDMap.compareTo(p.getUniqueId()) != 0) {
            repo.updatePlayerName(UUIDFetcher.getName(fromUUIDMap), fromUUIDMap);
        }
        if (repo.existsInPlayerMap(p.getUniqueId())) {
            repo.updatePlayerName(p.getName(), p.getUniqueId());
        } else {
            repo.insertPlayerName(p.getName(), p.getUniqueId());

        }
    }

    public void updateOnQuit(Player p) {
        repo.updateLastOnline(p);
    }

    public String getFirstJoined(String player) {
        UUID playerUUID = repo.getUUID(player);
        if (playerUUID == null) {
            return null;
        }
        long firstJoined = repo.getFirstJoined(playerUUID);
        if (firstJoined == 0) {
            return null;
        }
        return convertToDateString(firstJoined);
    }

    /**
     * @param player Player
     * @return The last time the player was online as a Date-String
     */
    public String getLastOnline(String player) {
        UUID playerUUID = repo.getUUID(player);
        long lastOnline = repo.getLastOnline(playerUUID);
        if (lastOnline == 0) {
            return null;
        }
        return convertToDateString(lastOnline);
    }

    /**
     * @param uuid The UUID of the player whose ontime shall be retrieved
     * @return Total ontime in minutes
     */
    public int getTotalOntime(UUID uuid) {
        int ontime = repo.getOntime(uuid);
        if (ontime == -1) {
            return 0;
            //Noe er galt
        }
        return ontime;
    }

    /**
     * @param uuid        Player whose ontime shall be increased
     * @param totalOntime The total, increased ontime of the player
     */
    public void setTotalOntime(UUID uuid, int totalOntime) {
        repo.updateOntime(uuid, totalOntime);
    }

    /**
     * @param player Player
     * @return Which number the player joined as
     */
    public int getJoinedAsNumber(String player) {
        UUID playerUUID = repo.getUUID(player);
        return repo.getPlayerNumber(playerUUID);
    }

    /**
     * @return ArrayList with names of everyone that has played on the server
     */
    public ArrayList<String> getAllPlayerNames() {
        return repo.getMapPlayerNames();
    }

    /**
     * @param name Player to retrieve exact name for
     * @return Exact, case-sensitive name for the player
     */
    public String getNameExact(String name) {
        UUID playerUUID = repo.getUUID(name);
        return repo.getName(playerUUID);
    }

    private String convertToDateString(long dateMillis) {
        Date date = new Date(dateMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MMMM/yyyy HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }
}
