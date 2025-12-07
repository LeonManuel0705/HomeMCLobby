package me.leon.lobby.commands;

import me.leon.lobby.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        Location spawnLocation = player.getWorld().getSpawnLocation();

        player.teleport(spawnLocation);
        player.sendMessage(Main.PREFIX + "§aDu wurdest zum Spawn teleportiert!");
        player.playSound(player.getLocation(), org.bukkit.Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);

        return true;
    }
}