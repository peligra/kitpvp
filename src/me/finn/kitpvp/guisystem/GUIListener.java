package me.finn.kitpvp.guisystem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class GUIListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        Player p = (Player) e.getWhoClicked();
        InventoryHolder holder = e.getClickedInventory().getHolder();

        if (holder instanceof GUI) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) { return; }
            GUI gui = (GUI) holder;
            gui.handleMenu(e);
        }
    }

}
