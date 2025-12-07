package me.leon.lobby.commands;

import me.leon.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LobbyCommand implements CommandExecutor {

    private final Main plugin;

    public LobbyCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl verwenden!");
            return true;
        }

        Player player = (Player) sender;

        // Prüfen ob Premium-Lobby verfügbar ist
        boolean hasPremiumAccess = player.hasPermission("lobby.premiumlobby");

        Location targetLobby;
        String lobbyName;

        player.teleport(spawnLocation);
        player.sendMessage(Main.PREFIX + "§aDu wurdest zum Spawn teleportiert!");
        player.playSound(player.getLocation(), org.bukkit.Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);

        return true;
    }

    private Location getLeastPopulatedPremiumLobby() {
        List<String> premiumLobbies = new ArrayList<>();

        for (String warpName : plugin.getWarpManager().getWarpNames()) {
            if (warpName.toLowerCase().startsWith("premiumlobby-")) {
                premiumLobbies.add(warpName);
            }
        }

        if (premiumLobbies.isEmpty()) {
            return null;
        }

        String bestLobby = null;
        int minPlayers = Integer.MAX_VALUE;

        for (String lobbyName : premiumLobbies) {
            Location loc = plugin.getWarpManager().getWarp(lobbyName);
            if (loc != null && loc.getWorld() != null) {
                int playerCount = countPlayersNearLocation(loc, 100); // 100 Blöcke Radius

                if (playerCount < minPlayers) {
                    minPlayers = playerCount;
                    bestLobby = lobbyName;
                }
            }
        }

        return bestLobby != null ? plugin.getWarpManager().getWarp(bestLobby) : null;
    }

    private Location getLeastPopulatedNormalLobby() {
        List<String> normalLobbies = new ArrayList<>();

        for (String warpName : plugin.getWarpManager().getWarpNames()) {
            String lower = warpName.toLowerCase();

            if (lower.startsWith("premiumlobby-")) {
                continue;
            }

            if (lower.contains("lobby") || lower.equals("spawn")) {
                normalLobbies.add(warpName);
            }
        }

        if (normalLobbies.isEmpty()) {
            if (plugin.getWarpManager().warpExists("spawn")) {
                return plugin.getWarpManager().getWarp("spawn");
            }
            return null;
        }

        String bestLobby = null;
        int minPlayers = Integer.MAX_VALUE;

        for (String lobbyName : normalLobbies) {
            Location loc = plugin.getWarpManager().getWarp(lobbyName);
            if (loc != null && loc.getWorld() != null) {
                int playerCount = countPlayersNearLocation(loc, 100); // 100 Blöcke Radius

                if (playerCount < minPlayers) {
                    minPlayers = playerCount;
                    bestLobby = lobbyName;
                }
            }
        }

        return bestLobby != null ? plugin.getWarpManager().getWarp(bestLobby) : null;
    }

    private int countPlayersNearLocation(Location location, double radius) {
        if (location.getWorld() == null) {
            return 0;
        }

        int count = 0;
        double radiusSquared = radius * radius;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().equals(location.getWorld())) {
                continue;
            }

            if (player.getLocation().distanceSquared(location) <= radiusSquared) {
                count++;
            }
        }

        return count;
    }
}