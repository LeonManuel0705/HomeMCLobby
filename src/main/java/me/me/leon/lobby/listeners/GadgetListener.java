package me.leon.lobby.listeners;

import me.leon.lobby.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GadgetListener implements Listener {

    private final Main plugin;

    public GadgetListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getGadgetManager().setGadget(event.getPlayer().getUniqueId(), null);
    }
}