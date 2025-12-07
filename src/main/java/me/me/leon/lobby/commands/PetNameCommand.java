package me.leon.lobby.commands;

import me.leon.lobby.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetNameCommand implements CommandExecutor {

    private final Main plugin;

    public PetNameCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl verwenden!");
            return true;
        }

        Player player = (Player) sender;

        if (!plugin.getPetManager().hasPet(player.getUniqueId())) {
            player.sendMessage(Main.PREFIX + "§cDu hast kein aktives Pet!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Main.PREFIX + "§cNutzung: /petname <Name>");
            return true;
        }

        StringBuilder nameBuilder = new StringBuilder();
        for (String arg : args) {
            if (nameBuilder.length() > 0) {
                nameBuilder.append(" ");
            }
            nameBuilder.append(arg);
        }

        String petName = nameBuilder.toString();

        if (petName.length() > 32) {
            player.sendMessage(Main.PREFIX + "§cDer Name ist zu lang! (Max. 32 Zeichen)");
            return true;
        }

        if (player.hasPermission("homemc.chatcolor")) {
            petName = petName.replace("&", "§");
        }

        plugin.getPetManager().setPetName(player.getUniqueId(), petName);
        player.sendMessage(Main.PREFIX + "§aPet umbenannt zu: §f" + petName);
        player.playSound(player.getLocation(), org.bukkit.Sound.LEVEL_UP, 1.0f, 1.5f);

        return true;
    }
}