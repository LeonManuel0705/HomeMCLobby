package me.leon.lobby.listeners;

import me.leon.lobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class HatProtectionListener implements Listener {

    private final Main plugin;

    public HatProtectionListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) return;

        if (!plugin.getHatManager().hasHat(player.getUniqueId())) return;

        if (event.getSlotType() == InventoryType.SlotType.ARMOR && event.getRawSlot() == 5) {
            event.setCancelled(true);
            player.sendMessage(Main.PREFIX + "§cDu kannst deinen Hut nicht abnehmen! Nutze das Gadget-Menü.");
            player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BREAK, 0.5f, 0.5f);
        }

        if (event.isShiftClick() && event.getCurrentItem() != null) {
            ItemStack clicked = event.getCurrentItem();
            if (clicked.getType().name().contains("HELMET") || clicked.getType().name().contains("SKULL")) {
                if (player.getInventory().getHelmet() != null &&
                        player.getInventory().getHelmet().equals(clicked)) {
                    event.setCancelled(true);
                    player.sendMessage(Main.PREFIX + "§cDu kannst deinen Hut nicht abnehmen! Nutze das Gadget-Menü.");
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) return;

        if (!plugin.getHatManager().hasHat(player.getUniqueId())) return;

        ItemStack dropped = event.getItemDrop().getItemStack();
        ItemStack helmet = player.getInventory().getHelmet();

        if (helmet != null && dropped.getType() == helmet.getType()) {
            event.setCancelled(true);
            player.sendMessage(Main.PREFIX + "§cDu kannst deinen Hut nicht droppen!");
            player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BREAK, 0.5f, 0.5f);
        }
    }
}