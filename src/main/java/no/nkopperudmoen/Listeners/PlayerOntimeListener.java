package no.nkopperudmoen.Listeners;

import no.nkopperudmoen.Tasks.OntimeTaskManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerOntimeListener implements Listener {
    /*
     *     Start a new task for tracking ontime when the player joins
     */
    @EventHandler
    public void startTaskOnPlayerJoin(PlayerJoinEvent e) {
        OntimeTaskManager.createTask(e.getPlayer().getUniqueId());
    }

    /*
     *     Cancel the task for tracking players ontime on quitting
     */
    @EventHandler
    public void cancelTaskOnQuit(PlayerQuitEvent e) {
        OntimeTaskManager.cancelTask(e.getPlayer().getUniqueId());
    }
}
