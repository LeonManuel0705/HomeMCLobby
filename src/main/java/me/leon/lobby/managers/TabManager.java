package me.leon.lobby.managers;

import me.leon.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TabManager {

    private final Main plugin;

    public TabManager(Main plugin) {
        this.plugin = plugin;
        startTabUpdateTask();
    }

    public void setupPlayer(Player player) {
        Scoreboard board = player.getScoreboard();
        if (board == null || board == Bukkit.getScoreboardManager().getMainScoreboard()) {
            return;
        }

        String prefix = plugin.getRankManager().getPrefix(player);
        int priority = plugin.getRankManager().getRankPriority(player);
        String playerName = player.getName();

        String teamName = String.format("%02d_%s", priority, playerName);
        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }

        Team oldTeam = board.getTeam(teamName);
        if (oldTeam != null) {
            oldTeam.unregister();
        }

        Team team = board.registerNewTeam(teamName);

        if (prefix.length() > 16) {
            team.setPrefix(prefix.substring(0, 16));
        } else {
            team.setPrefix(prefix);
        }

        team.addEntry(playerName);

        updateAllPlayersTab();
    }

    private void updateAllPlayersTab() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!plugin.isLobbyWorld(online.getWorld().getName())) {
                continue;
            }

            Scoreboard board = online.getScoreboard();
            if (board == null) continue;

            for (Player target : Bukkit.getOnlinePlayers()) {
                String prefix = plugin.getRankManager().getPrefix(target);
                int priority = plugin.getRankManager().getRankPriority(target);
                String teamName = String.format("%02d_%s", priority, target.getName());

                if (teamName.length() > 16) {
                    teamName = teamName.substring(0, 16);
                }

                Team team = board.getTeam(teamName);
                if (team == null) {
                    team = board.registerNewTeam(teamName);
                    if (prefix.length() > 16) {
                        team.setPrefix(prefix.substring(0, 16));
                    } else {
                        team.setPrefix(prefix);
                    }
                }

                if (!team.hasEntry(target.getName())) {
                    team.addEntry(target.getName());
                }
            }
        }
    }

    public void updateTabList(Player player) {
        try {
            String header = buildHeader(player).replace("\"", "\\\"");
            String footer = buildFooter().replace("\"", "\\\"");

            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." +
                    getServerVersion() + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object handle = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
            Object connection = handle.getClass().getField("playerConnection").get(handle);

            Class<?> ichat = Class.forName("net.minecraft.server." + getServerVersion() + ".IChatBaseComponent");
            Class<?> chatSerializer = Class.forName("net.minecraft.server." + getServerVersion() + ".IChatBaseComponent$ChatSerializer");
            Class<?> packetClass = Class.forName("net.minecraft.server." + getServerVersion() + ".PacketPlayOutPlayerListHeaderFooter");

            Object headerComponent = chatSerializer.getMethod("a", String.class)
                    .invoke(null, "{\"text\":\"" + header + "\"}");
            Object footerComponent = chatSerializer.getMethod("a", String.class)
                    .invoke(null, "{\"text\":\"" + footer + "\"}");

            Object packet = packetClass.newInstance();

            java.lang.reflect.Field a = packetClass.getDeclaredField("a");
            java.lang.reflect.Field b = packetClass.getDeclaredField("b");
            a.setAccessible(true);
            b.setAccessible(true);
            a.set(packet, headerComponent);
            b.set(packet, footerComponent);

            connection.getClass().getMethod("sendPacket",
                            Class.forName("net.minecraft.server." + getServerVersion() + ".Packet"))
                    .invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getServerVersion() {
        String name = org.bukkit.Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }
    private String buildHeader(Player player) {
        String rankColor = plugin.getRankManager().getRankColor(player);
        String rankName = plugin.getRankManager().getRankName(player);
        int coins = plugin.getCoinManager().getCoins(player.getUniqueId());

        return "\n" +
                "§6§l⚡ §b§lHOMEMC §6§l⚡\n" +
                "§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "\n" +
                "§7Dein Rang§8: " + rankColor + rankName + "\n" +
                "§7Deine Coins§8: §6" + formatNumber(coins) + "\n" +
                "\n";
    }

    private String buildFooter() {
        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();

        return "\n" +
                "§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "§7Spieler online§8: §a" + online + "§8/§c" + max + "\n" +
                "\n" +
                "§e§lWebsite§8: §fwww.homemc.org\n" +
                "§9§lDiscord§8: §fdiscord.gg/homemc\n" +
                "\n" +
                "§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━\n";
    }

    private String formatNumber(int number) {
        if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000) {
            return String.format("%.1fk", number / 1_000.0);
        }
        return String.valueOf(number);
    }

    private void startTabUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (plugin.isLobbyWorld(player.getWorld().getName())) {
                    updateTabList(player);
                }
            }
        }, 20L, 20L * 5);
    }

    public void removePlayer(Player player) {
        Scoreboard board = player.getScoreboard();
        if (board == null) return;

        for (Team team : board.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }
}