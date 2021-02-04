package me.finn.kitpvp.guisystem.guis;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.input.ChatInput;
import me.finn.kitpvp.guisystem.GUI;
import me.finn.kitpvp.guisystem.PlayerMenuUtility;
import me.finn.kitpvp.input.ConfirmGUI;
import me.finn.kitpvp.kit.Kit;
import me.finn.kitpvp.utils.Colorize;
import me.finn.kitpvp.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;

public class KitEditGUI extends GUI {

    private KitPVP pl;
    private Kit kit;

    public KitEditGUI(PlayerMenuUtility playerMenuUtility, Kit kit, KitPVP pl) {
        super(playerMenuUtility);
        this.kit = kit;
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Kits > " + Colorize.removeColorNotation(kit.getDisplayName());
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems() {
        inv.setItem(20, new ItemBuilder(Material.NAME_TAG)
                .setName("&c&lSet Name")
                .setLore(Arrays.asList(
                        "&7&o" + kit.getDisplayName(),
                        "",
                        "&fClick here &cto set name."))
                .toItemStack());
        inv.setItem(21, new ItemBuilder(Material.BOOK)
                .setName("&b&lSet Description")
                .setLore(Arrays.asList(
                        "&7&o" + kit.getDescription() ,
                        "" ,
                        "&fClick here &bto set description."))
                .toItemStack());
        inv.setItem(22, new ItemBuilder(kit.getIcon())
                .setName("&a&lSet Icon")
                .setLore(Arrays.asList("&fClick here &ato set icon."))
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack());
        inv.setItem(23, new ItemBuilder(Material.GOLD_INGOT)
                .setName("&6&lSet Price")
                .setLore(Arrays.asList(
                        "&7&o" + kit.getPrice(),
                        "",
                        "&fClick here &6to set price."))
                .toItemStack());
        inv.setItem(24, new ItemBuilder(Material.POTION)
                .setName("&d&lSet Effects")
                .setLore(Arrays.asList("&fClick here &dto set effects."))
                .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                .toItemStack());

        inv.setItem(41, new ItemBuilder(Material.LAVA_BUCKET)
                .setName("&c&lDelete Kit")
                .setLore(Arrays.asList("&fClick here &cto delete kit."))
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

        if (mat == Material.NAME_TAG) {
            new ChatInput.Builder(p).plugin(pl)
                    .title("&c&lSet Display Name")
                    .subtitle("&7Type name in chat!")
                    .onComplete((player, s) -> {
                        kit.setDisplayName(s);
                        kit.setName(Colorize.removeColorNotation(s)
                                        .toLowerCase()
                                        .replace(" ", "_"));
                        new KitEditGUI(playerMenuUtility, kit, pl).open();
                        return ChatInput.Response.close();
                    }).run();

        } else if (mat == Material.BOOK) {
            new ChatInput.Builder(p).plugin(pl)
                    .title("&b&lSet Description")
                    .subtitle("&7Type name in chat!")
                    .onComplete((player, s) -> {
                        kit.setDescription(s);
                        new KitEditGUI(playerMenuUtility, kit, pl).open();
                        return ChatInput.Response.close();
                    }).run();

        } else if (e.getSlot() == 22) {
            new ChatInput.Builder(p).plugin(pl)
                    .title("&a&lSet Icon")
                    .subtitle("&7Type name in chat!")
                    .onComplete((player, s) -> {
                        Material icon = getMaterial(s);

                        if (s == null) {
                            return ChatInput.Response.retry("&c&lInvalid Icon!");
                        }

                        kit.setIcon(icon);
                        new KitEditGUI(playerMenuUtility, kit, pl).open();
                        return ChatInput.Response.close();
                    }).run();
        } else if (mat == Material.GOLD_INGOT) {
            p.sendMessage("jhi");
        } else if (mat == Material.POTION) {
            p.sendMessage("jhi");
        } else if (mat == Material.OAK_SIGN) {
            new KitsGUI(playerMenuUtility, pl).open();
        } else if (mat == Material.LAVA_BUCKET) {
            new ConfirmGUI(playerMenuUtility).onComplete((confirmed) -> {
                if (confirmed) {
                    pl.km.deleteKit(kit);
                    new KitsGUI(playerMenuUtility, pl).open();
                } else {
                    new KitEditGUI(playerMenuUtility, kit, pl).open();
                }
            }).open();
        }
    }

    public Material getMaterial(String string) {
        Material[] materials = Material.values();

        String input = string.replace(" ", "_");

        for (Material m : materials) {
            if (m.name().equalsIgnoreCase(input)) {
                return m;
            }
            if (m.name().replace("_", "").equalsIgnoreCase(input)) {
                return m;
            }
        }

        return null;
    }

}
