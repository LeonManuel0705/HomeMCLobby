package me.leon.lobby.managers;

import me.leon.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WarpManager {

    private final Main plugin;
    private File warpsFile;
    private FileConfiguration warpsConfig;
    private Map<String, Location> warps;

    public WarpManager(Main plugin) {
        this.plugin = plugin;
        this.warps = new HashMap<>();

        setupWarpsFile();
        loadWarps();
    }

    private void setupWarpsFile() {
        warpsFile = new File(plugin.getDataFolder(), "warps.yml");

        if (!warpsFile.exists()) {
            try {
                warpsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);
    }

    private void loadWarps() {
        if (warpsConfig.getConfigurationSection("warps") == null) {
            return;
        }

        for (String warpName : warpsConfig.getConfigurationSection("warps").getKeys(false)) {
            String path = "warps." + warpName;
            String worldName = warpsConfig.getString(path + ".world");
            double x = warpsConfig.getDouble(path + ".x");
            double y = warpsConfig.getDouble(path + ".y");
            double z = warpsConfig.getDouble(path + ".z");
            float yaw = (float) warpsConfig.getDouble(path + ".yaw");
            float pitch = (float) warpsConfig.getDouble(path + ".pitch");

            if (Bukkit.getWorld(worldName) != null) {
                Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                warps.put(warpName.toLowerCase(), loc);
            }
        }
    }

    public void setWarp(String name, Location location) {
        name = name.toLowerCase();
        warps.put(name, location);

        String path = "warps." + name;
        warpsConfig.set(path + ".world", location.getWorld().getName());
        warpsConfig.set(path + ".x", location.getX());
        warpsConfig.set(path + ".y", location.getY());
        warpsConfig.set(path + ".z", location.getZ());
        warpsConfig.set(path + ".yaw", location.getYaw());
        warpsConfig.set(path + ".pitch", location.getPitch());

        saveWarps();
    }

    public void deleteWarp(String name) {
        name = name.toLowerCase();
        warps.remove(name);
        warpsConfig.set("warps." + name, null);
        saveWarps();
    }

    public Location getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    public Set<String> getWarpNames() {
        return warps.keySet();
    }

    public boolean warpExists(String name) {
        return warps.containsKey(name.toLowerCase());
    }

    private void saveWarps() {
        try {
            warpsConfig.save(warpsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
