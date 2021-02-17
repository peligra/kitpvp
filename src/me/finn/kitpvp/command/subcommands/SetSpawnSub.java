package me.finn.kitpvp.command.subcommands;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class SetSpawnSub extends SubCommand {

    @Override
    public String getName() {
        return "setspawn";
    }

    @Override
    public String getPermission() {
        return "kitpvp.setspawn";
    }

    @Override
    public void execute(Player p, List<String> args, KitPVP pl) {
        pl.dc.setSpawn(p.getLocation());
        p.sendMessage("Set spawn location to current position!");
    }

}
