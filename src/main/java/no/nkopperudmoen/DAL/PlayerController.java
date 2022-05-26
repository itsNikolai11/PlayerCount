package no.nkopperudmoen.DAL;

import no.nkopperudmoen.UTIL.UUIDFetcher;
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
            repo.insertPlayerName(p.getName(), p.getUniqueId());
        } else {
            repo.updatePlayerName(p.getName(), p.getUniqueId());
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
        long firstJoined = repo.getFirstJoined(playerUUID);
        if (firstJoined == 0) {
            return null;
        }
        return convertToDateString(firstJoined);
    }

    public String getLastOnline(String player) {
        UUID playerUUID = repo.getUUID(player);
        long lastOnline = repo.getLastOnline(playerUUID);
        if (lastOnline == 0) {
            return null;
        }
        return convertToDateString(lastOnline);
    }

    public int getJoinedAsNumber(String player) {
        UUID playerUUID = repo.getUUID(player);
        return repo.getPlayerNumber(playerUUID);
    }

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
