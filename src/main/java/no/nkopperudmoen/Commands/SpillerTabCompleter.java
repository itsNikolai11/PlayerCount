package no.nkopperudmoen.Commands;

import no.nkopperudmoen.Constants.PERMISSIONS;
import no.nkopperudmoen.DAL.PlayerController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SpillerTabCompleter implements TabCompleter {
    private final PlayerController controller = PlayerController.getInstance();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        ArrayList<String> names = controller.getAllPlayerNames();
        if (sender.hasPermission(PERMISSIONS.COMMAND_SPILLER_OTHERS)) {
            return names
                    .stream()
                    .filter(n -> n.toLowerCase(Locale.ROOT)
                            .startsWith(strings[0].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
