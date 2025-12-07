package me.leon.lobby.managers;

import me.leon.lobby.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ShopManager {

    private final Main plugin;
    private Map<UUID, Set<String>> purchasedItems;

    public ShopManager(Main plugin) {
        this.plugin = plugin;
        this.purchasedItems = new HashMap<>();
    }

    public void loadPurchases(UUID uuid) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Set<String> items = new HashSet<>();

                PreparedStatement ps = plugin.getMySQL().getConnection().prepareStatement(
                        "SELECT item_id FROM shop_purchases WHERE uuid = ?"
                );
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    items.add(rs.getString("item_id"));
                }

                purchasedItems.put(uuid, items);

            } catch (Exception e) {
                plugin.getLogger().warning("Fehler beim Laden der Shop-Käufe für " + uuid + ": " + e.getMessage());
                purchasedItems.put(uuid, new HashSet<>());
            }
        });
    }

    public boolean hasPurchased(UUID uuid, String itemId) {
        return purchasedItems.getOrDefault(uuid, new HashSet<>()).contains(itemId);
    }

    public void purchase(UUID uuid, String itemId, int cost) {
        if (!plugin.getCoinManager().hasCoins(uuid, cost)) {
            return;
        }

        plugin.getCoinManager().removeCoins(uuid, cost);

        if (!purchasedItems.containsKey(uuid)) {
            purchasedItems.put(uuid, new HashSet<>());
        }
        purchasedItems.get(uuid).add(itemId);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement ps = plugin.getMySQL().getConnection().prepareStatement(
                        "INSERT INTO shop_purchases (uuid, item_id, purchased_at) VALUES (?, ?, ?)"
                );
                ps.setString(1, uuid.toString());
                ps.setString(2, itemId);
                ps.setLong(3, System.currentTimeMillis());
                ps.executeUpdate();
            } catch (Exception e) {
                plugin.getLogger().severe("Fehler beim Speichern des Shop-Kaufs: " + e.getMessage());
            }
        });
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