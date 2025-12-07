package me.leon.lobby.managers;

import me.leon.lobby.Main;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class RocketManager {

    private final Main plugin;
    private Map<UUID, Long> lastClicks;
    private List<Location> rocketLocations;

    public RocketManager(Main plugin) {
        this.plugin = plugin;
        this.lastClicks = new HashMap<>();
        this.rocketLocations = new ArrayList<>();

        loadRocketLocations();
    }

    private void loadRocketLocations() {
        if (plugin.getConfig().contains("rockets")) {
            for (String key : plugin.getConfig().getConfigurationSection("rockets").getKeys(false)) {
                String path = "rockets." + key;
                String world = plugin.getConfig().getString(path + ".world");
                double x = plugin.getConfig().getDouble(path + ".x");
                double y = plugin.getConfig().getDouble(path + ".y");
                double z = plugin.getConfig().getDouble(path + ".z");

                if (plugin.getServer().getWorld(world) != null) {
                    Location loc = new Location(plugin.getServer().getWorld(world), x, y, z);
                    rocketLocations.add(loc);
                }
            }
        }
    }

    public void addRocketLocation(Location loc) {
        rocketLocations.add(loc);

        int id = rocketLocations.size();
        String path = "rockets." + id;
        plugin.getConfig().set(path + ".world", loc.getWorld().getName());
        plugin.getConfig().set(path + ".x", loc.getX());
        plugin.getConfig().set(path + ".y", loc.getY());
        plugin.getConfig().set(path + ".z", loc.getZ());
        plugin.saveConfig();
    }

    public void onRocketClick(Player player, Location loc) {
        UUID uuid = player.getUniqueId();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement ps = plugin.getMySQL().getConnection().prepareStatement(
                        "SELECT clicked_at FROM lobby_rocket_clicks WHERE uuid = ?"
                );
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    long lastClick = rs.getLong("clicked_at");
                    long now = System.currentTimeMillis();
                    long cooldown = 24 * 60 * 60 * 1000; // 24 Stunden

                    if (now - lastClick < cooldown) {
                        long remaining = (cooldown - (now - lastClick)) / 1000 / 60 / 60;
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            player.sendMessage(Main.PREFIX + "§cDu kannst die Rakete erst in §e" + remaining + " Stunden §cwieder nutzen!");
                        });
                        return;
                    }
                }

                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    shootRocket(loc);
                    plugin.getCoinManager().addCoins(uuid, 2);
                    player.sendMessage(Main.PREFIX + "§a+2 Coins! §7(Gesamt: §e" + plugin.getCoinManager().getCoins(uuid) + "§7)");
                });

                ps = plugin.getMySQL().getConnection().prepareStatement(
                        "INSERT INTO lobby_rocket_clicks (uuid, clicked_at) VALUES (?, ?) " +
                                "ON DUPLICATE KEY UPDATE clicked_at = ?"
                );
                long now = System.currentTimeMillis();
                ps.setString(1, uuid.toString());
                ps.setLong(2, now);
                ps.setLong(3, now);
                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void shootRocket(Location loc) {
        Firework fw = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta meta = fw.getFireworkMeta();

        org.bukkit.FireworkEffect effect = org.bukkit.FireworkEffect.builder()
                .withColor(org.bukkit.Color.RED)
                .withColor(org.bukkit.Color.YELLOW)
                .withColor(org.bukkit.Color.ORANGE)
                .with(org.bukkit.FireworkEffect.Type.BALL_LARGE)
                .trail(true)
                .build();

        meta.addEffect(effect);
        meta.setPower(2);
        fw.setFireworkMeta(meta);
    }

    public List<Location> getRocketLocations() {
        return rocketLocations;
    }
}