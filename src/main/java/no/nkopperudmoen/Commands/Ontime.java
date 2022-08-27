package no.nkopperudmoen.Commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import no.nkopperudmoen.Constants.PERMISSIONS;
import no.nkopperudmoen.DAL.PlayerController;
import no.nkopperudmoen.UTIL.MESSAGES;
import no.nkopperudmoen.UTIL.MessagePreProcessor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("NullableProblems")
public class Ontime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        PlayerController controller = PlayerController.getInstance();
        if (controller == null) {
            sender.sendMessage("En feil har oppstått!");
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            int totalTime = controller.getTotalOntime(p.getUniqueId());
            String ontimeString = MessagePreProcessor.formatTimeFromMinutes(totalTime);
            p.sendMessage(MESSAGES.ONTIME.replaceAll("%ontime%", ontimeString));
            return true;
        }
        String arg1 = args[0];
        if (arg1.equalsIgnoreCase("top")) {
            //Toppliste
            return true;
        }
        if (p.hasPermission(PERMISSIONS.COMMAND_ONTIME_OTHERS)) {
            String exactTargetName = controller.getNameExact(arg1);
            if (exactTargetName == null || exactTargetName.isBlank() || exactTargetName.isEmpty()) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(MESSAGES.PLAYER_NOT_FOUND.replaceAll("%spiller%", arg1)));
            } else {

                int targetTid = controller.getTotalOntime(controller.getUUID(arg1));
                String targetTidConverted = MessagePreProcessor.formatTimeFromMinutes(targetTid);
                p.sendMessage(MESSAGES.ONTIME_OTHERS.
                        replaceAll("%spiller%", exactTargetName)
                        .replaceAll("%ontime%", targetTidConverted));
            }
        } else {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(MESSAGES.NO_PERMISSION));
        }
        return true;
    }
}
