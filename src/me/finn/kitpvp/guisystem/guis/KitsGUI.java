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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

        inv.setItem(41, new ItemBuilder(Material.FEATHER)
                .setName("&a&lNew Kit")
                .setLore(Arrays.asList("&fClick here &ato make a new kit."))
                .toItemStack());
        inv.setItem(40, new ItemBuilder(Material.OAK_SIGN)
                .setName("&7&lBack")
                .setLore(Arrays.asList("&fClick here &7to go back."))
                .toItemStack());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Material mat = e.getCurrentItem().getType();
        Player p = (Player) e.getWhoClicked();

        if (mat == Material.FEATHER) {
            int number = ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
            Kit kit = pl.km.newKit("Kit " + number);
            kit.setInv(p.getInventory());
            pl.kits.add(kit);
            new KitEditGUI(KitPVP.getPlayerMenuUtility(p), kit, pl).open();
        } else if (mat == Material.OAK_SIGN) {
            new OverviewGUI(playerMenuUtility, pl).open();
        } else {
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            if (meta != null) {
                String name = meta.getDisplayName();
                Kit kit = pl.km.getKit(Colorize.removeColorNotation(name), true);

                if (kit != null) {
                    new KitEditGUI(playerMenuUtility, kit, pl).open();
                }
            }
        }
    }

}
