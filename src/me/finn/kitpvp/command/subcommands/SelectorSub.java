package me.finn.kitpvp.command.subcommands;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.command.SubCommand;
import me.finn.kitpvp.guisystem.guis.KitSelectorGUI;
import org.bukkit.entity.Player;

import java.util.List;

public class SelectorSub extends SubCommand {

    @Override
    public String getName() {
        return "selector";
    }

    @Override
    public String getPermission() {
        return "kitpvp.selector";
    }

    @Override
    public void execute(Player p, List<String> args, KitPVP pl) {
        new KitSelectorGUI(KitPVP.getPlayerMenuUtility(p), pl).open();
    }

}
