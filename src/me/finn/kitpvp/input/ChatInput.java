package me.finn.kitpvp.input;

import me.finn.kitpvp.KitPVP;
import me.finn.kitpvp.utils.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.function.BiFunction;

public class ChatInput {

    private static HashMap<Player, Builder> waiting = new HashMap<>();

    public static class Builder {

        private String title;
        private String subtitle;
        private Player target;
        private KitPVP plugin;

        private BiFunction<Player, String, Response> completeFunction;

        public Builder(Player target) {
            this.target = target;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder plugin(KitPVP plugin) {
            this.plugin = plugin;
            plugin.getServer().getPluginManager().registerEvents(new ChatListener(plugin), plugin);
            return this;
        }

        public Builder onComplete(BiFunction<Player, String, Response> completeFunction) {
            this.completeFunction = completeFunction;
            return this;
        }

        public void run() {
            target.closeInventory();
            target.sendTitle(Colorize.color(title), Colorize.color(subtitle), 0, Integer.MAX_VALUE, 0);
            waiting.put(target, this);
        }

    }

    public static class ChatListener implements Listener {

        private KitPVP pl;

        public ChatListener(KitPVP pl) {
            this.pl = pl;
        }

        @EventHandler
        public void onChatEvent(AsyncPlayerChatEvent e) {
            Player p = e.getPlayer();
            String message = e.getMessage();

            if (waiting.containsKey(p)) {
                Builder builder = waiting.get(p);

                e.setCancelled(true);
                Bukkit.getScheduler().runTask(pl, () -> {
                    final Response response = builder.completeFunction.apply(p, message);
                    waiting.remove(p);
                    p.resetTitle();
                    if (response.isRetry()) {
                        new Builder(p)
                                .title(response.getText())
                                .subtitle("&7Please try again!")
                                .plugin(pl)
                                .onComplete(builder.completeFunction)
                                .run();
                    }
                });
            }
        }

    }

    public static class Response {

        private final String text;
        private boolean retry = false;
        private Response(String text, Boolean retry) {
            this.text = text;
            this.retry = retry;
        }

        public String getText() {
            return text;
        }
        public boolean isRetry() {
            return retry;
        }
        public static Response close() {
            return new Response(null, false);
        }
        public static Response retry(String message) {
            return new Response(message, true);
        }

    }

}
