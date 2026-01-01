package me.leon.lobby.listeners;

import me.leon.lobby.Main;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyProtectionListener implements Listener {

    private final Main plugin;

    public LobbyProtectionListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        if (!plugin.getBuildManager().isBuilding(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        if (!plugin.getBuildManager().isBuilding(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        if (!plugin.getBuildManager().isBuilding(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

        // Hut-Drop Protection
        ItemStack item = event.getItemDrop().getItemStack();
        if (plugin.getHatManager().hasHat(player.getUniqueId())) {
            ItemStack helmet = player.getInventory().getHelmet();
            if (helmet != null && item.getType() == helmet.getType()) {
                event.setCancelled(true);
                player.sendMessage(Main.PREFIX + "§cDu kannst deinen Hut nicht droppen!");
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        if (plugin.getBuildManager().isBuilding(player.getUniqueId())) {
            return;
        }

        String title = event.getView().getTitle();

        if (!title.contains("⚡") && !title.contains("Navigator") &&
                !title.contains("Profil") && !title.contains("Gadgets") &&
                !title.contains("Pets") && !title.contains("Hüte") &&
                !title.contains("Partikel")) {

            ItemStack clicked = event.getCurrentItem();
            if (clicked != null && clicked.hasItemMeta()) {
                String displayName = clicked.getItemMeta().getDisplayName();

                if (displayName != null && (
                        displayName.contains("Navigator") ||
                                displayName.contains("Profil") ||
                                displayName.contains("Gadgets") ||
                                displayName.contains("Spieler") ||
                                displayName.contains("Lobby") ||
                                displayName.contains("Pets"))) {
                    event.setCancelled(true);
                }
            }

            if (plugin.getHatManager().hasHat(player.getUniqueId())) {
                if (event.getSlotType() == org.bukkit.event.inventory.InventoryType.SlotType.ARMOR) {
                    if (event.getRawSlot() == 5) {
                        event.setCancelled(true);
                        player.sendMessage(Main.PREFIX + "§cDu kannst deinen Hut nicht abnehmen! Nutze das Gadget-Menü.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        if (!plugin.getBuildManager().isBuilding(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!plugin.isLobbyWorld(event.getWorld().getName())) {
            return;
        }

        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityInteract(org.bukkit.event.entity.EntityInteractEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!plugin.isLobbyWorld(player.getWorld().getName())) {
                return;
            }

            if (!plugin.getBuildManager().isBuilding(player.getUniqueId())) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        if (plugin.getBuildManager().isBuilding(player.getUniqueId())) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() == Material.BOW) {
                return;
            }
        }

        if (event.getClickedBlock() != null) {
            Material type = event.getClickedBlock().getType();
            if (type == Material.STONE_BUTTON || type == Material.WOOD_BUTTON) {
                return;
            }
        }

        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.CHEST ||
                    event.getClickedBlock().getType() == Material.TRAPPED_CHEST ||
                    event.getClickedBlock().getType() == Material.FURNACE ||
                    event.getClickedBlock().getType() == Material.BEACON ||
                    event.getClickedBlock().getType() == Material.DROPPER ||
                    event.getClickedBlock().getType() == Material.DISPENSER) {

                event.setCancelled(true);
                return;
            }
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamageOther(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (event.getDamager() instanceof Player &&
                plugin.isLobbyWorld(player.getWorld().getName()) &&
                !plugin.getBuildManager().isBuilding(player.getUniqueId())) {

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        if (plugin.isLobbyWorld(event.getRightClicked().getWorld().getName()) &&
                !plugin.getBuildManager().isBuilding(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFrameDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ItemFrame) {
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                if (plugin.isLobbyWorld(event.getEntity().getWorld().getName()) &&
                        !plugin.getBuildManager().isBuilding(damager.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            Player remover = (Player) event.getRemover();
            if (plugin.isLobbyWorld(event.getEntity().getWorld().getName()) &&
                    !plugin.getBuildManager().isBuilding(remover.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}