package me.finn.kitpvp.guisystem.guis;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.guisystem.GUI;
import me.finn.kitpvp.guisystem.PlayerMenuUtility;
import me.finn.kitpvp.kit.Kit;
import me.finn.kitpvp.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;

public class KitsGUI extends GUI {

    private KitPVP pl;

    public KitsGUI(PlayerMenuUtility playerMenuUtility, KitPVP pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Overview > Kits";
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems() {
        int i = 10;
        for (Kit kit : pl.kits) {
            inv.setItem(i, new ItemBuilder(kit.getIcon())
                    .setName(ChatColor.RESET + kit.getDisplayName())
                    .setLore(Arrays.asList("&f" + kit.getDescription(), "", "&fClick to &7edit kit!"))
                    .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .toItemStack());

            i++;
            if ((i + 1) % 9 == 0)
                i = i + 2;
        }
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }
}
