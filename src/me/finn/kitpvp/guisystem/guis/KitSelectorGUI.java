package me.finn.kitpvp.guisystem.guis;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.guisystem.GUI;
import me.finn.kitpvp.guisystem.PlayerMenuUtility;
import me.finn.kitpvp.kit.Kit;
import me.finn.kitpvp.utils.Colorize;
import me.finn.kitpvp.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class KitSelectorGUI extends GUI {

    private KitPVP pl;

    public KitSelectorGUI(PlayerMenuUtility playerMenuUtility, KitPVP pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Kit Selector";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setMenuItems() {
        int i = 10;
        for (Kit kit : pl.kits) {
            inv.setItem(i, new ItemBuilder(kit.getIcon())
                    .setName(ChatColor.RESET + kit.getDisplayName())
                    .setLore(Arrays.asList("&f" + kit.getDescription(), "", "&7&oClick to equip kit!"))
                    .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .toItemStack());

            i++;
            if ((i + 1) % 9 == 0)
                i = i + 2;
        }
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Material mat = e.getCurrentItem().getType();
        Player p = (Player) e.getWhoClicked();

        ItemMeta meta = e.getCurrentItem().getItemMeta();
        if (meta != null) {
            String name = meta.getDisplayName();
            Kit kit = pl.km.getKit(Colorize.removeColorNotation(name), true);

            if (kit != null) {
                p.closeInventory();
                pl.km.equipKit(p, kit);
            }
        }
    }

}
