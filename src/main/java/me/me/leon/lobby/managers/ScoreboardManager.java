package me.leon.lobby.managers;

import me.leon.core.managers.ClanManager;
import me.leon.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ScoreboardManager {

    private final Main plugin;
    private final org.bukkit.scoreboard.ScoreboardManager manager;

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
        this.manager = Bukkit.getScoreboardManager();
        startScoreboardTask();
    }

    public void setScoreboard(Player player) {
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("lobby", "dummy");
        obj.setDisplayName("§b§lMAIN LOBBY");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(board);
        updateScoreboard(player);
    }

    public void updateScoreboard(Player player) {
        Scoreboard board = player.getScoreboard();
        if (board == null) {
            setScoreboard(player);
            return;
        }

        Objective obj = board.getObjective("lobby");
        if (obj == null) {
            setScoreboard(player);
            return;
        }

        board.getEntries().forEach(board::resetScores);

        String rankColor = plugin.getRankManager().getRankColor(player);
        String playerName = player.getName();
        int coins = plugin.getCoinManager().getCoins(player.getUniqueId());
        String playTime = formatPlayTime(getPlayTime(player));
        ClanManager.Clan clan = plugin.getClanManager().getPlayerClan(player.getUniqueId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        String formattedDate = dateFormat.format(new Date());

        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        int line = 14;

            setScore(obj, "§7" + formattedDate, line--);
            setScore(obj, " ", line--);

            setScore(obj, "§6§lAbout", line--);
            setScore(obj, "  §eName: " + plugin.getRankManager().getRankColor(player) + playerName, line--);
            setScore(obj, "  §eCoins: §b" + plugin.getCoinManager().getCoins(player.getUniqueId()), line--);
            setScore(obj, "  §eSpielzeit: §b" + playTime, line--);

            if (clan != null) {
                String clanColor = clan.getColor();
                setScore(obj, "  §eClan:", line--);
                setScore(obj, "§e    " + clanColor + clan.getName(), line--);
            } else {
                setScore(obj, "  §eClan: §cKein Clan", line--);
            }

            setScore(obj, "  ", line--);

            setScore(obj, "§6§lServer", line--);
            setScore(obj, "  §eOnline: §b" + onlinePlayers, line--);

            setScore(obj, "   ", line--);

            setScore(obj, "§bplay.homemc.org", line--);
    }


    private void setScore(Objective obj, String text, int score) {
        String uniqueText = text;
        if (text.trim().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < score; i++) {
                sb.append("§r");
            }
            uniqueText = sb.toString();
        }
        Score s = obj.getScore(uniqueText);
        s.setScore(score);
    }

    private String formatNumber(int number) {
        if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000) {
            return String.format("%.1fk", number / 1_000.0);
        }
        return String.valueOf(number);
    }

    private long getPlayTime(Player player) {
        try {
            java.sql.PreparedStatement ps = plugin.getCore().getMySQL().getConnection().prepareStatement(
                    "SELECT first_join FROM player_data WHERE uuid = ?"
            );
            ps.setString(1, player.getUniqueId().toString());
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                long firstJoin = rs.getLong("first_join");
                rs.close();
                ps.close();
                if (firstJoin > 0) {
                    return System.currentTimeMillis() - firstJoin;
                }
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
        }
        return player.getPlayerTime();
    }

    private String formatPlayTime(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);

        if (hours >= 1) {
            return hours + "h";
        } else {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
            return minutes + "m";
        }
    }

    private void startScoreboardTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (plugin.isLobbyWorld(player.getWorld().getName())) {
                    updateScoreboard(player);
                }
            }
        }, 20L, 20L);
    }

    public void removeScoreboard(Player player) {
        player.setScoreboard(manager.getNewScoreboard());
    }
}