package me.leon.lobby.listeners;

import me.leon.lobby.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VoidTeleportListener implements Listener {

    private final Main plugin;

    public VoidTeleportListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        double voidY = plugin.getConfig().getDouble("void-teleport-y", -50);

        if (player.getLocation().getY() <= voidY) {
            Location spawnLocation = player.getWorld().getSpawnLocation();

            player.teleport(spawnLocation);
            player.setFallDistance(0);
            player.playSound(player.getLocation(), org.bukkit.Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
        }
    }
}