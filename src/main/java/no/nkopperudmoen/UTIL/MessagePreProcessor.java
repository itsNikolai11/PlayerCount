package no.nkopperudmoen.UTIL;

import net.md_5.bungee.api.ChatColor;
import no.nkopperudmoen.DAL.PlayerController;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessagePreProcessor {
    private static final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String colorize(String message) {
        Matcher match = hexPattern.matcher(message);
        while (match.find()) {
            String color = message.substring(match.start(), match.end());
            message = message.replaceAll(color, ChatColor.of(color) + "");
            match = hexPattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String populatePlaceholders(String msg, Player p) {
        PlayerController controller = PlayerController.getInstance();
        if (controller == null) {
            return msg;
        }
        msg = msg.replaceAll("%spiller%", p.getName());
        msg = msg.replaceAll("%firstJoined%", controller.getFirstJoined(p.getName()));
        msg = msg.replaceAll("%lastSeen%", controller.getLastOnline(p.getName()));
        msg = msg.replaceAll("%number%", controller.getJoinedAsNumber(p.getName()) + "");
        return msg;
    }

    public static String formatTimeFromMinutes(int min) {
        int day = (min / 60) / 24;
        int hour = (min / 60) % 24;
        int minutes = min % 60;
        StringBuilder bobTheBuilder = new StringBuilder();
        if (day > 0) {
            if (day > 1) {
                bobTheBuilder.append(day).append(" dager");
            } else {
                bobTheBuilder.append(day).append(" dag");
            }
        }
        if (hour > 0) {
            if (hour > 1) {
                bobTheBuilder.append(" ").append(hour).append(" timer");
            } else {
                bobTheBuilder.append(" ").append(hour).append(" time");
            }
        }
        if (minutes >= 0) {

            bobTheBuilder.append(" ").append(minutes).append(" min");

        }
        return bobTheBuilder.toString();
    }
}
