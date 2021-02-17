package me.finn.kitpvp.game;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.guisystem.guis.KitSelectorGUI;
import me.finn.kitpvp.kit.Kit;
import me.finn.kitpvp.utils.Colorize;
import me.finn.kitpvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
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

    public void selectKit(Player p, Kit kit) {
        if (isPlaying(p)) {
            setSelectedKit(p, kit);
        } else {
            join(p, kit);
        }
    }

    public void join(Player p, Kit kit) {
        resetPlayer(p);
        players.add(p);
        pl.km.equipKit(p, kit);
        setSelectedKit(p, kit);
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
        announceChat("&f⚐ &a" + killer.getName() + "&f has killed &c" + target.getName());
        target.getWorld().strikeLightningEffect(target.getLocation());
        respawn(target);
    }

    public void respawn(Player p) {
        teleportToSpawn(p);
        resetPlayer(p);
        giveControlItems(p);
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

    private void giveControlItems(Player p) {
        p.getInventory().setItem(4, new ItemBuilder(Material.COMPASS)
                .setName("&a&lKit Selector").setLore("&7Select your kit here!", "", "&fClick here to &aview kits!")
                .toItemStack());
        p.getInventory().setItem(8, new ItemBuilder(Material.BARRIER)
                .setName("&c&lExit KitPVP").setLore("&7Leave the KitPVP game.", "", "&fClick here to &cleave KitPVP!")
                .toItemStack());
    }

    private void teleportToSpawn(Player p) {
        if (pl.dc.getSpawn() == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't teleport " + p.getName() + " to spawn, as no location was set!");
        } else {
            p.teleport(pl.dc.getSpawn());
        }
    }

    private void setSelectedKit(Player p, Kit kit) {
        selectedKit.remove(p.getUniqueId());
        selectedKit.put(p.getUniqueId(), kit.getName());
    }

    private Kit getSelectedKit(Player p) {
        String name = selectedKit.get(p.getUniqueId());
        if (name == null) {
            return null;
        }

        Kit kit = pl.km.getKit(name, false);

        if (kit == null) {
            return null;
        }

        return kit;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isPlaying(e.getPlayer())) {
                ItemStack item = e.getItem();

                if (item != null) {
                    if (item.getType() == Material.COMPASS) {
                        new KitSelectorGUI(KitPVP.getPlayerMenuUtility(e.getPlayer()), pl).open();
                    } else if (item.getType() == Material.BARRIER) {
                        remove(e.getPlayer());
                        teleportToSpawn(e.getPlayer());
                    }
                }
            }
        }
    }

    // join game thing-o
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (pl.dc.getSpawn() != null) {
            ;if (e.getTo().getWorld() == pl.dc.getSpawn()) {
                if (p.getGameMode() == GameMode.ADVENTURE) {
                    if (e.getTo().getY() <= 102 && e.getTo().getY() > 101) {
                        if (isPlaying(p)) {
                            Kit kit = getSelectedKit(p);
                            if (kit != null) {
                                pl.km.equipKit(p, kit);
                            }
                        } else {
                            teleportToSpawn(p);
                            enteringLocation.put(p.getUniqueId(), p.getLocation());
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 99999, true));

                            new KitSelectorGUI(KitPVP.getPlayerMenuUtility(p), pl).open();
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player target = (Player) e.getEntity();

            if (isPlaying(damager) && isPlaying(target)) {
                if (target.getHealth() - e.getFinalDamage() <= 0) {
                    e.setCancelled(true);

                    kill(target, damager);
                }
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
