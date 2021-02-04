package me.finn.kitpvp.kit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public class Kit {

    private String name;
    private String displayName;
    private String description = "&r&7Default description.";
    private Integer price = 0;
    private Material icon = Material.IRON_SWORD;
    private Inventory inv;
    private ArrayList<PotionEffect> effects = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public void setIcon(Material icon) {
        this.icon = icon;
    }
    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public String getName() {
        return name;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getDescription() {
        return description;
    }
    public Integer getPrice() {
        return price;
    }
    public Material getIcon() {
        return icon;
    }
    public Inventory getInv() {
        return inv;
    }
    public ArrayList<PotionEffect> getEffects() {
        return effects;
    }

}
