package me.finn.kitpvp.guisystem;

import me.finn.kitpvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public abstract class GUI implements InventoryHolder {

    protected Inventory inv;
    protected PlayerMenuUtility playerMenuUtility;

    public GUI(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
    }

    public abstract String getMenuName();
    public abstract int getSlots();
    public abstract void setMenuItems();
    public abstract void handleMenu(InventoryClickEvent e);

    public void open() {
        inv = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();

        if (isEmpty(inv)) {
            for (int i = 0; i < inv.getSize(); i++) {
                inv.setItem(i, new ItemBuilder(Material.GLASS, i).toItemStack());
            }
        }

        playerMenuUtility.getOwner().openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    private boolean isEmpty(Inventory inv) {
        for (ItemStack item : inv) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

}
