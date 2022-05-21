package no.nkopperudmoen.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerTimeListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(!p.hasPlayedBefore()){

            //Lagre i database
            //Annonser at det er første innlogging
            //Oppdater first join tidspunkt
            //Oppdater last online tidspunkt

        }
    }
    public void onPlayerQuit(PlayerQuitEvent e){
        //Oppdater last online tidspunkt
    }
}
