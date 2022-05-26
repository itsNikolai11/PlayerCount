package no.nkopperudmoen.Commands;

import no.nkopperudmoen.DAL.PlayerController;
import no.nkopperudmoen.UTIL.MESSAGES;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spiller implements CommandExecutor {
    private final PlayerController controller;

    public Spiller(PlayerController controller) {
        this.controller = controller;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (p.hasPermission("")) {
            if (args.length > 0) {
                //Spiller må ha perm
                String target = args[0];
                String firstJoined = controller.getFirstJoined(target);
                if (firstJoined == null) {
                    p.sendMessage("Player not found");
                    return true;
                }
                String lastJoined = controller.getLastOnline(target);
                boolean targetIsOnline = false;
                for (Player t : Bukkit.getOnlinePlayers()) {
                    if (t.getName().equalsIgnoreCase(target)) {
                        targetIsOnline = true;
                    }
                }
                if (targetIsOnline) {
                    lastJoined = "Nå";
                }
                int joinedAs = controller.getJoinedAsNumber(target);
                String msg = MESSAGES.PLAYERINFO;
                msg = msg.replaceAll("%name%", controller.getNameExact(target));
                msg = msg.replaceAll("%firstJoined%", firstJoined);
                msg = msg.replaceAll("%lastSeen%", lastJoined);
                msg = msg.replaceAll("%number%", joinedAs + "");
                p.sendMessage(msg);
                return true;
            }
            String firstJoined = controller.getFirstJoined(p.getName());
            if (firstJoined == null) {
                p.sendMessage("Player not found");
                return true;
            }
            int joinedAs = controller.getJoinedAsNumber(p.getName());
            String msg = MESSAGES.PLAYERINFO;
            msg = msg.replaceAll("%name%", p.getName());
            msg = msg.replaceAll("%firstJoined%", firstJoined);
            msg = msg.replaceAll("%lastSeen%", "Nå");
            msg = msg.replaceAll("%number%", joinedAs + "");
            p.sendMessage(msg);
        }


        return true;
    }


}
