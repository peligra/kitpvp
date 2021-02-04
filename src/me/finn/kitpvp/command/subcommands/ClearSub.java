package me.finn.kitpvp.command.subcommands;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.command.SubCommand;
import me.finn.kitpvp.kit.Kit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class ClearSub extends SubCommand {

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getPermission() {
        return "kitpvp.kits.clear";
    }

    @Override
    public void execute(Player p, List<String> args, KitPVP pl) {
        Iterator iterator = pl.kits.iterator();

        while (iterator.hasNext()) {
            Kit kit = (Kit) iterator.next();
            pl.kits.remove(kit);
            pl.km.deleteKit(kit);
        }
    }
}
