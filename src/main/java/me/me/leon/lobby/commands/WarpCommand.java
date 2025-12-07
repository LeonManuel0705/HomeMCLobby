package me.leon.lobby.commands;

import me.leon.lobby.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private final Main plugin;

    public WarpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl verwenden!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Main.PREFIX + "§cNutzung: /warp <Name>");

            if (!plugin.getWarpManager().getWarpNames().isEmpty()) {
                player.sendMessage(Main.PREFIX + "§7Verfügbare Warps: §e" + String.join(", ", plugin.getWarpManager().getWarpNames()));
            }
            return true;
        }

        String warpName = args[0].toLowerCase();
        Location warp = plugin.getWarpManager().getWarp(warpName);

        if (warp == null) {
            player.sendMessage(Main.PREFIX + "§cDieser Warp existiert nicht!");
            return true;
        }

        player.teleport(warp);
        player.sendMessage(Main.PREFIX + "§aDu wurdest zu §e" + warpName + " §ateleportiert!");
        return true;
    }
}