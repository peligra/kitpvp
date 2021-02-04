package me.finn.kitpvp.input;

import me.finn.kitpvp.guisystem.GUI;
import me.finn.kitpvp.guisystem.PlayerMenuUtility;
import me.finn.kitpvp.utils.Colorize;
import me.finn.kitpvp.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ConfirmGUI extends GUI {

    private Consumer<Boolean> closeListener;

    public ConfirmGUI(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Click To Confirm";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setMenuItems() {
        inv.setItem(12, new ItemBuilder(Material.RED_CONCRETE)
                .setName(Colorize.color("&c&lCancel"))
                .toItemStack());
        inv.setItem(14, new ItemBuilder(Material.LIME_CONCRETE)
                .setName(Colorize.color("&a&lConfirm"))
                .toItemStack());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        closeListener.accept(item.getType() == Material.LIME_CONCRETE);
    }

    public ConfirmGUI onComplete(Consumer<Boolean> closeListener) {
        this.closeListener = closeListener;
        return this;
    }

}
