package me.finn.kitpvp.utils;

import org.bukkit.ChatColor;

public class Colorize {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static String stripColor(String s) {
        return ChatColor.stripColor(s);
    }

}
