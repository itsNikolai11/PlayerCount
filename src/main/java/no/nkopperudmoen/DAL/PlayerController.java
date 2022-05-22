package no.nkopperudmoen.DAL;

import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class PlayerController {
    private final PlayerRepository repo;

    public PlayerController(PlayerRepository repo) {
        this.repo = repo;
    }

    public void updateOnJoin(Player p) {
        if (!p.hasPlayedBefore() || repo.getFirstJoined(p.getUniqueId()) == 0) {
            repo.savePlayerOnFirstJoin(p);
            return;
        }
        if (repo.getName(p.getUniqueId()).isEmpty()) {
            repo.insertPlayerName(p.getUniqueId(), p.getName());
        } else {
            repo.updatePlayerName(p.getName(), p.getUniqueId());
        }
        repo.updateLastOnline(p);
    }

    public void updateOnQuit(Player p) {
        repo.updateLastOnline(p);
    }

    public String getFirstJoined(String player) {
        UUID playerUUID = repo.getUUID(player);
        long firstJoined = repo.getFirstJoined(playerUUID);
        return convertToDateString(firstJoined);
    }

    public String getLastOnline(String player) {
        UUID playerUUID = repo.getUUID(player);
        long lastOnline = repo.getLastOnline(playerUUID);
        return convertToDateString(lastOnline);
    }

    public int getJoinedAs(String player) {
        UUID playerUUID = repo.getUUID(player);
        return repo.getPlayerNumber(playerUUID);
    }

    private String convertToDateString(long dateMillis) {
        Date date = new Date(dateMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MMMM/yyyy HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }
}
