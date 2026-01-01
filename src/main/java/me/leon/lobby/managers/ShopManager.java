package me.leon.lobby.managers;

import me.leon.lobby.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShopManager {

    private final Main plugin;
    private final Map<UUID, Set<String>> purchasedItems;
    private final Set<UUID> loadingPlayers;

    public ShopManager(Main plugin) {
        this.plugin = plugin;
        this.purchasedItems = new ConcurrentHashMap<>();
        this.loadingPlayers = ConcurrentHashMap.newKeySet();
    }

    public void loadPurchases(UUID uuid) {
        loadingPlayers.add(uuid);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Set<String> items = ConcurrentHashMap.newKeySet();

                try (PreparedStatement ps = plugin.getMySQL().getConnection().prepareStatement(
                        "SELECT item_id FROM shop_purchases WHERE uuid = ?"
                )) {
                    ps.setString(1, uuid.toString());
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            items.add(rs.getString("item_id"));
                        }
                    }
                }

                purchasedItems.put(uuid, items);

            } catch (Exception e) {
                plugin.getLogger().warning("Fehler beim Laden der Shop-Käufe für " + uuid + ": " + e.getMessage());
                purchasedItems.put(uuid, ConcurrentHashMap.newKeySet());
            } finally {
                loadingPlayers.remove(uuid);
            }
        });
    }

    public boolean isLoading(UUID uuid) {
        return loadingPlayers.contains(uuid);
    }

    public boolean hasPurchased(UUID uuid, String itemId) {
        Set<String> items = purchasedItems.get(uuid);
        return items != null && items.contains(itemId);
    }

    public void purchase(UUID uuid, String itemId, int cost) {
        if (plugin.getCoinManager() == null || !plugin.getCoinManager().hasCoins(uuid, cost)) {
            return;
        }

        plugin.getCoinManager().removeCoins(uuid, cost);

        purchasedItems.computeIfAbsent(uuid, k -> ConcurrentHashMap.newKeySet()).add(itemId);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try (PreparedStatement ps = plugin.getMySQL().getConnection().prepareStatement(
                    "INSERT INTO shop_purchases (uuid, item_id, purchased_at) VALUES (?, ?, ?)"
            )) {
                ps.setString(1, uuid.toString());
                ps.setString(2, itemId);
                ps.setLong(3, System.currentTimeMillis());
                ps.executeUpdate();
            } catch (Exception e) {
                plugin.getLogger().severe("Fehler beim Speichern des Shop-Kaufs: " + e.getMessage());
            }
        });
    }

    public void unloadPlayer(UUID uuid) {
        purchasedItems.remove(uuid);
        loadingPlayers.remove(uuid);
    }

    public Map<String, Integer> getShopItems() {
        Map<String, Integer> items = new LinkedHashMap<>();

        // Gadgets
        items.put("gadget_hearts", 100);
        items.put("gadget_flames", 150);
        items.put("gadget_magic", 200);
        items.put("gadget_snow", 150);
        items.put("gadget_ender", 250);
        items.put("gadget_redstone", 200);

        return items;
    }

    public String getItemDisplayName(String itemId) {
        switch (itemId) {
            case "gadget_hearts": return "§c§lHerzen Trail";
            case "gadget_flames": return "§6§lFlammen Trail";
            case "gadget_magic": return "§d§lMagic Trail";
            case "gadget_snow": return "§f§lSchnee Trail";
            case "gadget_ender": return "§5§lEnder Trail";
            case "gadget_redstone": return "§4§lRedstone Trail";
            default: return "§7Unbekannt";
        }
    }
}