package me.leon.lobby.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.leon.lobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerConnectListener implements Listener {

    private final Main plugin;

    public ServerConnectListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (plugin.getCore().getNickManager().hasAutoNick(player.getUniqueId())) {
            sendAutoNickSignal(player);
        }
    }

    private void sendAutoNickSignal(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF("homemc.autonick");

        ByteArrayDataOutput messageOut = ByteStreams.newDataOutput();
        messageOut.writeUTF(player.getUniqueId().toString());

        byte[] message = messageOut.toByteArray();
        out.writeShort(message.length);
        out.write(message);

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}