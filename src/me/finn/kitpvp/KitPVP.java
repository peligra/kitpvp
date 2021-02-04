package me.finn.kitpvp;

import me.finn.kitpvp.command.CommandHandler;
import me.finn.kitpvp.config.ConfigManager;
import me.finn.kitpvp.guisystem.GUIListener;
import me.finn.kitpvp.guisystem.PlayerMenuUtility;
import me.finn.kitpvp.kit.Kit;
import me.finn.kitpvp.kit.KitManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitPVP extends JavaPlugin {

    public List<Kit> kits = new ArrayList<>();
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    public ConfigManager cm = new ConfigManager(this);
    public KitManager km = new KitManager(this);

    @Override
    public void onEnable() {
        // enable
        cm.loadKits();

        getCommand("kitpvp").setExecutor(new CommandHandler(this));
        getCommand("kitpvp").setTabCompleter(new CommandHandler(this));
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
    }

    @Override
    public void onDisable() {
        // disable
        cm.saveConfig();
        cm.saveKits();
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        if (playerMenuUtilityMap.containsKey(p)) { return playerMenuUtilityMap.get(p); }

        PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(p);
        playerMenuUtilityMap.put(p, playerMenuUtility);
        return playerMenuUtility;
    }

}
