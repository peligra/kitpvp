package me.finn.kitpvp.kit;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.utils.Colorize;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class KitManager {

    private KitPVP pl;

    public KitManager(KitPVP pl) {
        this.pl = pl;
    }

    public Kit getKit(String name, Boolean ignoreCase) {
        if (ignoreCase) {
            for (Kit kit : pl.kits) {
                if (kit.getName().equalsIgnoreCase(name) || kit.getDisplayName().equalsIgnoreCase(name)) {
                    return kit;
                }
            }
        } else {
            for (Kit kit : pl.kits) {
                if (kit.getName().equals(name) || kit.getDisplayName().equals(name)) {
                    return kit;
                }
            }
        }
        return null;
    }

    public Kit newKit(String displayName) {
        Kit kit = new Kit();
        kit.setDisplayName(displayName);
        kit.setName(Colorize.removeColorNotation(displayName)
                .toLowerCase()
                .replace(" ", "_"));
        return kit;
    }

    public void equipKit(Player p, Kit kit) {
        preparePlayer(p);
        p.getInventory().setContents(kit.getInv().getContents());
    }

    public void deleteKit(Kit kit) {
        pl.kits.remove(kit);
        pl.cm.deleteKit(kit);
    }

    public void preparePlayer(Player p) {
        p.getInventory().clear();
        p.setHealth(20);
        p.setFoodLevel(20);
        for (PotionEffect potionEffect : p.getActivePotionEffects()) {
            p.removePotionEffect(potionEffect.getType());
        }
    }

    public void sendActionbar(Player p, String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Colorize.color(message)));
    }

}
