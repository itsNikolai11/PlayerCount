package no.nkopperudmoen.Commands;

import no.nkopperudmoen.Constants.PERMISSIONS;
import no.nkopperudmoen.DAL.PlayerController;
import no.nkopperudmoen.UTIL.MESSAGES;
import no.nkopperudmoen.UTIL.MessagePreProcessor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spiller implements CommandExecutor {
    private final PlayerController controller = PlayerController.getInstance();

    public Spiller() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (p.hasPermission(PERMISSIONS.COMMAND_SPILLER)) {
            if (args.length > 0) {
                if (p.hasPermission(PERMISSIONS.COMMAND_SPILLER_OTHERS)) {
                    String target = args[0];
                    String firstJoined = controller.getFirstJoined(target);
                    if (firstJoined == null) {
                        p.sendMessage(MESSAGES.PLAYER_NOT_FOUND.replaceAll("%spiller%", target));
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
                        lastJoined = MESSAGES.NOW;
                    }
                    int joinedAs = controller.getJoinedAsNumber(target);
                    String msg = MESSAGES.PLAYERINFO;
                    msg = msg.replaceAll("%spiller%", controller.getNameExact(target));
                    msg = msg.replaceAll("%firstJoined%", firstJoined);
                    msg = msg.replaceAll("%lastSeen%", lastJoined);
                    msg = msg.replaceAll("%number%", joinedAs + "");
                    p.sendMessage(msg);
                    return true;
                } else {
                    p.sendMessage(MESSAGES.NO_PERMISSION);
                }
            }
            String firstJoined = controller.getFirstJoined(p.getName());
            if (firstJoined == null) {
                p.sendMessage(MESSAGES.PLAYER_NOT_FOUND);
                return true;
            }
            int joinedAs = controller.getJoinedAsNumber(p.getName());
            String msg = MESSAGES.PLAYERINFO;
            msg = msg.replaceAll("%spiller%", p.getName());
            msg = msg.replaceAll("%firstJoined%", firstJoined);
            msg = msg.replaceAll("%lastSeen%", MESSAGES.NOW);
            msg = msg.replaceAll("%number%", joinedAs + "");
            p.sendMessage(msg);
        } else {
            p.sendMessage(MESSAGES.NO_PERMISSION);
        }


        return true;
    }


}
