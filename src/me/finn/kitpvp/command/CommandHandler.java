package me.finn.kitpvp.command;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.command.subcommands.ClearSub;
import me.finn.kitpvp.command.subcommands.CreateSub;
import me.finn.kitpvp.guisystem.guis.KitsGUI;
import me.finn.kitpvp.guisystem.guis.OverviewGUI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private KitPVP pl;
    private List<SubCommand> subcommands = new ArrayList<>();

    public CommandHandler(KitPVP pl) {
        this.pl = pl;
        subcommands.add(new CreateSub());
        subcommands.add(new ClearSub());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The console is not supported by KitPVP!");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            new OverviewGUI(KitPVP.getPlayerMenuUtility(p), pl).open();
            return true;
        }

        String input = args[0];
        SubCommand sub = getSubCommand(input);

        if (sub == null) {
            p.sendMessage("That's not a valid subcommand!");
            return true;
        }

        if (!p.hasPermission(sub.getPermission())) {
            p.sendMessage("You don't have permission to use this function!");
        }

        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        arguments.remove(0);

        sub.execute(p, arguments, pl);

        return false;
    }

    private SubCommand getSubCommand(String string) {
        for (SubCommand sc : subcommands) {
            if (sc.getName().equalsIgnoreCase(string)) {
                return sc;
            }
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            for (SubCommand sc : subcommands) {
                suggestions.add(sc.getName());
            }
        }

        return suggestions;
    }
}
