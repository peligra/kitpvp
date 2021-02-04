package me.finn.kitpvp.command.subcommands;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.command.SubCommand;
import me.finn.kitpvp.kit.Kit;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateSub extends SubCommand {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getPermission() {
        return "kitpvp.kits.create";
    }

    @Override
    public void execute(Player p, List<String> args, KitPVP pl) {
        if (args.size() == 0) {
            p.sendMessage("Please specify a name for the kit!");
            return;
        }

        String name = args.get(0);

        if (pl.km.getKit(name, true) != null) {
            p.sendMessage("That kit name is already in use.");
            return;
        }

        Kit kit = pl.km.newKit(name);
        kit.setInv(p.getInventory());
        pl.kits.add(kit);
    }

}
