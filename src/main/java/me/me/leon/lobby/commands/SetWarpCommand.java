package me.leon.lobby.commands;

import me.leon.lobby.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {

    private final Main plugin;

    public SetWarpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl verwenden!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lobby.warp.set")) {
            player.sendMessage("§cKeine Berechtigung!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Main.PREFIX + "§cNutzung: /setwarp <Name>");
            return true;
        }

        String warpName = args[0].toLowerCase();
        plugin.getWarpManager().setWarp(warpName, player.getLocation());

        player.sendMessage(Main.PREFIX + "§aWarp §e" + warpName + " §awurde erstellt!");
        return true;
    }
}