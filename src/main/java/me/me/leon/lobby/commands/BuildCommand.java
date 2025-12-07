package me.leon.lobby.commands;

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
            player.sendMessage(Main.PREFIX + "§cKeine Berechtigung!");
            return true;
        }

        plugin.getBuildManager().toggleBuild(player.getUniqueId());

        if (plugin.getBuildManager().isBuilding(player.getUniqueId())) {
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.sendMessage(Main.PREFIX + "§aBaumodus aktiviert!");
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            me.leon.lobby.utils.LobbyItems.giveLobbyItems(player);
            player.sendMessage(Main.PREFIX + "§cBaumodus deaktiviert!");
        }

        return true;
    }
}