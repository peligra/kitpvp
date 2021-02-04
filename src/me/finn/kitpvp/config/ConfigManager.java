package me.finn.kitpvp.config;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private KitPVP pl;
    private final FileConfiguration kitsConfiguration;
    private final File kitsFile;

    public ConfigManager(KitPVP pl) {
        this.pl = pl;

        // gets files and actual config
        this.kitsFile = new File(pl.getDataFolder(), "kits.yml");
        this.kitsConfiguration = new YamlConfiguration();

        // loading config could throw errors so try catch
        try {
            this.kitsConfiguration.load(this.kitsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveKits() {
        // clear file
        if (kitsFile.delete()) {
            saveConfig();
        }

        // save kits
        for (Kit kit : pl.kits) {
            saveKit(kit);
        }
    }

    public void loadKits() {
        for (String name : kitsConfiguration.getKeys(false)) {
            ConfigurationSection section = kitsConfiguration.getConfigurationSection(name);
            loadKit(section);
        }
    }

    public void loadKit(ConfigurationSection section) {
        Kit kit = new Kit();
        kit.setName(section.getName());
        kit.setDisplayName(section.getString("displayname"));
        kit.setDescription(section.getString("description"));
        kit.setIcon(Material.getMaterial(section.getString("icon")));
        kit.setPrice(section.getInt("price"));

        // effects
        ConfigurationSection effects = section.getConfigurationSection("effects");

        if (effects != null) {
            for (String effectName : effects.getKeys(false)) {
                ConfigurationSection effectSection = effects.getConfigurationSection(effectName);

                PotionEffectType type = PotionEffectType.getByName(effectSection.getString("type"));
                int duration = effectSection.getInt("duration");
                int amplifier = effectSection.getInt("amplifier");

                kit.getEffects().add(new PotionEffect(type, duration, amplifier));
            }
        }

        // items
        ConfigurationSection items = section.getConfigurationSection("items");

        if (items != null) {
            Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER);
            for (String itemName : items.getKeys(false)) {
                ConfigurationSection itemSection = items.getConfigurationSection(itemName);

                ItemStack item = new ItemStack(
                        Material.getMaterial(itemSection.getString("material")),
                        itemSection.getInt("amount"));

                ItemMeta meta = item.getItemMeta();
                meta.setLore(itemSection.getStringList("lore"));
                if (!itemSection.getString("displayname").equals("")) {
                    meta.setDisplayName(itemSection.getString("displayname"));
                }

                ConfigurationSection enchantsSection = itemSection.getConfigurationSection("enchants");
                for (String enchKey : enchantsSection.getKeys(false)) {
                    ConfigurationSection enchantmentSection = enchantsSection.getConfigurationSection(enchKey);
                    meta.addEnchant(
                            Enchantment.getByName(enchantmentSection.getString("name")),
                            enchantmentSection.getInt("level"),
                            true);
                }

                item.setItemMeta(meta);
                inv.setItem(Integer.valueOf(itemName), item);
            }

            kit.setInv(inv);
        }

        pl.kits.add(kit);
    }

    public void saveKit(Kit kit) {
        // wipe kit if already exists
        if (kitsConfiguration.isConfigurationSection(kit.getName())) {
            kitsConfiguration.set(kit.getName(), null);
        }

        ConfigurationSection section = kitsConfiguration.createSection(kit.getName());
        section.set("displayname", kit.getDisplayName());
        section.set("description", kit.getDescription());
        section.set("icon", kit.getIcon().name());
        section.set("price", kit.getPrice());

        // create effects section if there are any
        if (kit.getEffects().size() != 0) {
            ConfigurationSection effects = section.createSection("effects");

            int i = 1;
            for (PotionEffect effect : kit.getEffects()) {
                addPotionEffect(effects.createSection(String.valueOf(i)), effect);
                i++;
            }
        }

        // create items section if there any any
        if (kit.getInv() != null && kit.getInv().getContents().length != 0) {
            ConfigurationSection items = section.createSection("items");

            for (int i = 0; i < kit.getInv().getSize(); i++) {
                ItemStack item = kit.getInv().getItem(i);
                if (item != null) {
                    addItem(items.createSection(String.valueOf(i)), item);
                }
            }
        }
        saveConfig();
    }

    public void addPotionEffect(ConfigurationSection section, PotionEffect effect) {
        section.set("type", effect.getType().getName());
        section.set("duration", effect.getDuration());
        section.set("amplifier", effect.getAmplifier());
    }

    public void addItem(ConfigurationSection section, ItemStack item) {
        section.set("material", item.getType().name());
        section.set("amount", item.getAmount());
        section.set("displayname", item.getItemMeta().getDisplayName());
        section.set("lore", item.getItemMeta().getLore());
        ConfigurationSection enchants = section.createSection("enchants");

        int i = 1;
        for (Enchantment ench : item.getEnchantments().keySet()) {
            ConfigurationSection enchant = enchants.createSection(String.valueOf(i));
            enchant.set("name", ench.getName());
            enchant.set("level", item.getEnchantments().get(ench));
            i++;
        }
    }

    public void deleteKit(Kit kit) {
        if (kitsConfiguration.isConfigurationSection(kit.getName())) {
            kitsConfiguration.set(kit.getName(), null);
        }
        saveConfig();
    }

    public void saveConfig() {
        try {
            kitsConfiguration.save(kitsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
