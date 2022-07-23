package no.nkopperudmoen.Commands;

import no.nkopperudmoen.DAL.PlayerController;
import no.nkopperudmoen.UTIL.MESSAGES;
import no.nkopperudmoen.UTIL.MessagePreProcessor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ontime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        PlayerController controller = PlayerController.getInstance();
        Player p = (Player) sender;
        if (args.length == 0) {
            int totalTime = controller.getTotalOntime(p.getUniqueId());
            String ontimeString = MessagePreProcessor.formatTimeFromMinutes(totalTime);
            p.sendMessage(MESSAGES.ONTIME.replaceAll("%ontime%", ontimeString));
        }
        return true;
    }
}
