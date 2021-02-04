package me.finn.kitpvp.guisystem.guis;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.guisystem.GUI;
import me.finn.kitpvp.guisystem.PlayerMenuUtility;
import me.finn.kitpvp.utils.Colorize;
import me.finn.kitpvp.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;

public class OverviewGUI extends GUI {

    private KitPVP pl;

    public OverviewGUI(PlayerMenuUtility playerMenuUtility, KitPVP pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Overview";
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems() {
        inv.setItem(21, new ItemBuilder(Material.EMERALD)
                .setName("&a&lKits")
                .setLore(Arrays.asList("&7Find all loaded kits here.", "", "&fClick here to &aview kits!")).toItemStack());
        inv.setItem(22, new ItemBuilder(Material.GOLD_INGOT)
                .setName("&6&lStats")
                .setLore(Arrays.asList("&7See all user statistics.", "", "&fClick here to &6view stats!")).toItemStack());
        inv.setItem(23, new ItemBuilder(Material.DIAMOND)
                .setName("&b&lSettings")
                .setLore(Arrays.asList("&7Modify plugin settings.", "", "&fClick here to &bview settings!")).toItemStack());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Material mat = e.getCurrentItem().getType();
        Player p = (Player) e.getWhoClicked();

        if (mat == Material.EMERALD) {
            new KitsGUI(KitPVP.getPlayerMenuUtility(p), pl).open();
        }
    }

}
