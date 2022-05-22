package no.nkopperudmoen.Listeners;

import no.nkopperudmoen.DAL.PlayerController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerTimeListener implements Listener {
    private PlayerController controller;

    public PlayerTimeListener(PlayerController controller) {
        this.controller = controller;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        controller.updateOnJoin(p);
        if (!p.hasPlayedBefore()) {
            //Annonser at det er første innlogging
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        controller.updateOnQuit(e.getPlayer());
    }
}
