package no.nkopperudmoen.UTIL;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageStyler {
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
}
