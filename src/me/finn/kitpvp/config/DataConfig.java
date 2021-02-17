package me.finn.kitpvp.config;

import me.finn.kitpvp.KitPVP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DataConfig {

    private KitPVP pl;
    private final FileConfiguration dataConfig;
    private final File dataFile;

    public DataConfig(KitPVP pl) {
        this.pl = pl;

        // gets files and actual config
        this.dataFile = new File(pl.getDataFolder(), "data.yml");
        this.dataConfig = new YamlConfiguration();

        // loading config could throw errors so try catch
        try {
            this.dataConfig.load(this.dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void setSpawn(Location location) {
        dataConfig.set("spawn", Arrays.asList(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()));
        saveConfig();
    }

    public Location getSpawn() {
        if (!dataConfig.isList("spawn")) { return null; }

        List<String> list = dataConfig.getStringList("spawn");
        World world = Bukkit.getWorld(list.get(0));

        if (world == null) {
            Bukkit.getLogger().log(Level.SEVERE, "KitPVP spawn location ERROR! The world named " + list.get(0) + " could not be found!");
            return null;
        }

        return new Location(
                world,
                Double.parseDouble(list.get(1)),
                Double.parseDouble(list.get(2)),
                Double.parseDouble(list.get(3)),
                Float.parseFloat(list.get(4)),
                Float.parseFloat(list.get(5)));
    }

    public void setKills(UUID uuid, Integer kills) {
        // set kills here w/ config
    }

    public void setDeaths(UUID uuid, Integer deaths) {
        // set deaths here w/ config
    }

    public Integer getKills(UUID uuid) {
        // get kills from config here
        return 8;
    }

    public Integer getDeaths(UUID uuid) {
        // get deaths from config here
        return 8;
    }

    public void saveConfig() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
