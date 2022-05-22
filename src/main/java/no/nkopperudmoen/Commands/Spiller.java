package no.nkopperudmoen.Commands;

import no.nkopperudmoen.DAL.PlayerController;
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
        if (args.length > 0) {
            //Spiller må ha perm
            return true;
        }
        String lastOnline = controller.getLastOnline(p.getName());
        String firstJoined = controller.getFirstJoined(p.getName());
        int joinedAs = controller.getJoinedAs(p.getName());
        p.sendMessage("First joined: " + firstJoined +
                "\nLast online: " + lastOnline +
                "\nJoined as player number " + joinedAs);


        return true;
    }


}
