package no.nkopperudmoen.Listeners;

import no.nkopperudmoen.DAL.PlayerController;
import no.nkopperudmoen.UTIL.MESSAGES;
import no.nkopperudmoen.UTIL.MessagePreProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerTimeListener implements Listener {
    private final PlayerController controller = PlayerController.getInstance();

    @EventHandler
    public void updatePlayerDataOnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        controller.updateOnJoin(p);
        controller.updateUUIDMap(p);
        if (!p.hasPlayedBefore()) {
            String msg = MESSAGES.JOINED_FOR_FIRST_TIME;
            msg = MessagePreProcessor.populatePlaceholders(msg, p);
            Bukkit.broadcastMessage(msg);
        }
    }

    @EventHandler
    public void updatePlayerDataOnQuit(PlayerQuitEvent e) {
        controller.updateOnQuit(e.getPlayer());
    }
}
