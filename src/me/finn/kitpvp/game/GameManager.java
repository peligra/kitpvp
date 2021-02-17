package me.finn.kitpvp.game;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.guisystem.guis.KitSelectorGUI;
import me.finn.kitpvp.kit.Kit;
import me.finn.kitpvp.utils.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class GameManager implements Listener {

    private KitPVP pl;

    public GameManager(KitPVP pl) {
        this.pl = pl;
    }

    public List<Player> players = new ArrayList<>();
    public HashMap<UUID, String> selectedKit = new HashMap<>();
    public HashMap<UUID, Location> enteringLocation = new HashMap<>();

    public void join(Player p, Kit kit) {
        resetPlayer(p);
        players.add(p);
        pl.km.equipKit(p, kit);
        if (enteringLocation.containsKey(p.getUniqueId())) {
            p.teleport(enteringLocation.get(p.getUniqueId()));
            enteringLocation.remove(p.getUniqueId());
        }
    }

    public void remove(Player p) {
        resetPlayer(p);
        players.remove(p);
    }

    public boolean isPlaying(Player p) {
        return players.contains(p);
    }

    public void kill(Player target, Player killer) {
        UUID targetUUID = target.getUniqueId();
        UUID killerUUID = killer.getUniqueId();

        pl.dc.setDeaths(targetUUID, pl.dc.getKills(targetUUID) + 1);
        pl.dc.setKills(killerUUID, pl.dc.getKills(killerUUID) + 1);

        // todo: make kill messages config based
        announceChat("&c⚐ &a" + killer.getName() + "&f has killed &c" + target.getName());
        respawn(target);
    }

    public void respawn(Player p) {
        if (pl.dc.getSpawn() == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't teleport " + p.getName() + " to spawn, as no location was set!");
        } else {
            p.teleport(pl.dc.getSpawn());
        }
        remove(p);
    }

    public void announceChat(String message) {
        for (Player p : players) {
            p.sendMessage(Colorize.color(message));
        }
    }

    private void resetPlayer(Player p) {
        p.getInventory().clear();
        p.setHealth(20);
        p.setHealthScale(20);
        p.setFoodLevel(20);
        clearPotionEffects(p);
    }

    private void clearPotionEffects(Player p) {
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
    }

    // join game thing-o
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (!isPlaying(p) && p.getGameMode() == GameMode.ADVENTURE) {
            if (e.getTo().getY() <= 101) {
                if (pl.dc.getSpawn() == null) {
                    Bukkit.getLogger().log(Level.SEVERE, "Couldn't teleport " + p.getName() + " to spawn, as no location was set!");
                } else {
                    enteringLocation.put(p.getUniqueId(), p.getLocation());
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 99999, true));
                    p.teleport(pl.dc.getSpawn());
                }

                new KitSelectorGUI(KitPVP.getPlayerMenuUtility(p), pl).open();
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player target = (Player) e.getEntity();

            if (target.getHealth() - e.getFinalDamage() <= 0) {
                e.setCancelled(true);

                kill(target, damager);
            }
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                Player p = (Player) e.getEntity();

                if (isPlaying(p)) {
                    e.setCancelled(true);
                }
            }
        }
    }

}
