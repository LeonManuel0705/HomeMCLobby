package me.leon.lobby.commands;

import me.leon.lobby.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarpCommand implements CommandExecutor {

    private final Main plugin;

    public DelWarpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl verwenden!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lobby.warp.delete")) {
            player.sendMessage("§cKeine Berechtigung!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Main.PREFIX + "§cNutzung: /delwarp <Name>");
            return true;
        }

        String warpName = args[0].toLowerCase();

        if (!plugin.getWarpManager().warpExists(warpName)) {
            player.sendMessage(Main.PREFIX + "§cDieser Warp existiert nicht!");
            return true;
        }

        plugin.getWarpManager().deleteWarp(warpName);
        player.sendMessage(Main.PREFIX + "§cWarp §e" + warpName + " §cwurde gelöscht!");
        return true;
    }
}