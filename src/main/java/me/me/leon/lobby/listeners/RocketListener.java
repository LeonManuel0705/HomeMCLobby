package me.leon.lobby.listeners;

import me.leon.lobby.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class RocketListener implements Listener {

    private final Main plugin;

    public RocketListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null) return;

        if (block.getType() == Material.SKULL) {
            for (org.bukkit.Location rocketLoc : plugin.getRocketManager().getRocketLocations()) {
                if (block.getLocation().distance(rocketLoc) < 1.0) {
                    event.setCancelled(true);
                    plugin.getRocketManager().onRocketClick(player, rocketLoc);
                    break;
                }
            }
        }
    }
}