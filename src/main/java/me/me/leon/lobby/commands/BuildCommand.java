package me.leon.lobby.commands;

import me.leon.core.managers.RankManager;
import me.leon.lobby.Main;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    private final Main plugin;

    public BuildCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl verwenden!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lobby.build")) {
            RankManager.RankData builder = plugin.getRankManager().getRankData("homemc.builder");
            String builderPrefix = builder.prefix;
            String builderColor = builder.color;
            String builderName = builder.displayName;
            String builderFormatted = builderColor + builderName;
            player.sendMessage("§8§m=======§r §c§lZugriff verweigert §8§m=======§r");
            player.sendMessage("§3Um diesen Befehl auszuführen, benötigst du:");
            player.sendMessage(" §8• §2Rang: " + builderFormatted);
            player.sendMessage("§8§m====================================");
            return true;
        }

        plugin.getBuildManager().toggleBuild(player.getUniqueId());

        if (plugin.getBuildManager().isBuilding(player.getUniqueId())) {
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.sendMessage(Main.PREFIX + "§aBau-Modus aktiviert!");
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            me.leon.lobby.utils.LobbyItems.giveLobbyItems(player);
            player.sendMessage(Main.PREFIX + "§cBau-Modus deaktiviert!");
        }

        return true;
    }
}