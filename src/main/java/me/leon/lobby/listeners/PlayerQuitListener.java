package me.leon.lobby.listeners;

import me.leon.lobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final Main plugin;

    public PlayerQuitListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        plugin.getGadgetManager().setGadget(player.getUniqueId(), null);

        if (plugin.getNickManager().isNicked(player.getUniqueId())) {
            plugin.getNickManager().unnickPlayer(player);
        }
        if (plugin.getGadgetManager() != null) {
            plugin.getGadgetManager().setGadget(uuid, null);
        }

        if (plugin.getPetManager() != null) {
            plugin.getPetManager().removePet(uuid);
        }

        if (plugin.getHatManager() != null) {
            plugin.getHatManager().removeHat(uuid);
        }
    }
}