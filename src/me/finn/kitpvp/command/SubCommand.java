package me.finn.kitpvp.command;

import me.finn.kitpvp.KitPVP;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {

    public abstract String getName();
    public abstract String getPermission();
    public abstract void execute(Player p, List<String> args, KitPVP pl);

}
