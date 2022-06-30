package no.nkopperudmoen.Listeners;

import no.nkopperudmoen.PlayerCount;
import no.nkopperudmoen.UTIL.MessagePreProcessor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class ExecuteCommandOnJoin implements Listener {
    private final PlayerCount plugin;

    public ExecuteCommandOnJoin(PlayerCount plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void executeCommandOnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        List<String> commands;
        if (!p.hasPlayedBefore()) {
            //Utfør firstJoin-kommandoer
            commands = (List<String>) plugin.getConfig().getList("FirstJoinCommands");
        } else {
            commands = (List<String>) plugin.getConfig().getList("JoinCommands");
        }
        if (commands == null) {
            return;
        }
        for (String command : commands) {
            command = MessagePreProcessor.populatePlaceholders(command, p);
            Bukkit.getServer().dispatchCommand(console, command);
        }
    }
}
