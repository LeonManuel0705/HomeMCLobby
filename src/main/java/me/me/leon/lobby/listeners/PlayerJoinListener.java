package me.leon.lobby.listeners;

import me.leon.lobby.Main;
import me.leon.lobby.utils.LobbyItems;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Main plugin;

    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        plugin.getCoinManager().loadPlayer(player);
        plugin.getFriendManager().loadFriends(player.getUniqueId());
        plugin.getShopManager().loadPurchases(player.getUniqueId());

        if (plugin.getNickManager().hasAutoNick(player.getUniqueId())) {
            if (!plugin.getNickManager().isNicked(player.getUniqueId())) {
                plugin.getNickManager().nickPlayer(player);
            }
        }

        if (!plugin.getNickManager().isNicked(player.getUniqueId())) {
            String rankColor = plugin.getRankManager().getRankColor(player);
            String prefix = plugin.getRankManager().getPrefix(player);
            String clanTag = plugin.getClanManager().getClanSuffix(player);
            player.setDisplayName(rankColor + prefix + player.getName() + clanTag);
            player.setPlayerListName(rankColor + prefix + player.getName() + clanTag);
        }

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(player.hasPermission("homemc.fly"));

        LobbyItems.giveLobbyItems(player);

        plugin.getScoreboardManager().setScoreboard(player);
        plugin.getTabManager().setupPlayer(player);
        plugin.getTabManager().updateTabList(player);
    }
}