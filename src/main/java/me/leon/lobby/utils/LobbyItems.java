package me.leon.lobby.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class LobbyItems {

    public static void giveLobbyItems(Player player) {
        player.getInventory().clear();

        ItemStack navigator = new ItemStack(Material.COMPASS);
        ItemMeta navMeta = navigator.getItemMeta();
        navMeta.setDisplayName("§b§lNavigator");
        navMeta.setLore(Arrays.asList(
                "§7Verbinde dich mit",
                "§7verschiedenen GameModes!",
                "",
                "§eRechtsklick zum Öffnen"
        ));
        navigator.setItemMeta(navMeta);
        player.getInventory().setItem(0, navigator);

        ItemStack profile = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta profileMeta = (SkullMeta) profile.getItemMeta();
        profileMeta.setOwner(player.getName());
        profileMeta.setDisplayName("§e§lProfil");
        profileMeta.setLore(Arrays.asList(
                "§7Deine Einstellungen",
                "§7und Statistiken",
                "",
                "§eRechtsklick zum Öffnen"
        ));
        profile.setItemMeta(profileMeta);
        player.getInventory().setItem(1, profile);

        ItemStack gadgets = new ItemStack(Material.CHEST);
        ItemMeta gadgetsMeta = gadgets.getItemMeta();
        gadgetsMeta.setDisplayName("§d§lGadgets");
        gadgetsMeta.setLore(Arrays.asList(
                "§7Coole Partikeleffekte",
                "§7und mehr!",
                "",
                "§eRechtsklick zum Öffnen"
        ));
        gadgets.setItemMeta(gadgetsMeta);
        player.getInventory().setItem(4, gadgets);

        ItemStack visibility = new ItemStack(Material.INK_SACK, 1, (short) 10);
        ItemMeta visMeta = visibility.getItemMeta();
        visMeta.setDisplayName("§a§lSpieler angezeigt");
        visMeta.setLore(Arrays.asList(
                "§7Spieler verstecken/zeigen",
                "",
                "§eRechtsklick zum Umschalten"
        ));
        visibility.setItemMeta(visMeta);
        player.getInventory().setItem(7, visibility);

        ItemStack lobbySwitcher = new ItemStack(Material.NETHER_STAR);
        ItemMeta switcherMeta = lobbySwitcher.getItemMeta();
        switcherMeta.setDisplayName("§6§lLobby Wechseln");
        switcherMeta.setLore(Arrays.asList(
                "§7Wechsle zwischen",
                "§7verschiedenen Lobbies!",
                "",
                "§eRechtsklick zum Öffnen"
        ));
        lobbySwitcher.setItemMeta(switcherMeta);
        player.getInventory().setItem(8, lobbySwitcher);
    }

    public static ItemStack getVisibilityItem(boolean playersVisible) {
        ItemStack item;
        ItemMeta meta;

        if (playersVisible) {
            item = new ItemStack(Material.INK_SACK, 1, (short) 10);
            meta = item.getItemMeta();
            meta.setDisplayName("§a§lSpieler angezeigt");
            meta.setLore(Arrays.asList(
                    "§7Spieler verstecken/zeigen",
                    "",
                    "§7Status§8: §aAlle sichtbar",
                    "",
                    "§eRechtsklick zum Verstecken"
            ));
        } else {
            item = new ItemStack(Material.INK_SACK, 1, (short) 8);
            meta = item.getItemMeta();
            meta.setDisplayName("§c§lSpieler versteckt");
            meta.setLore(Arrays.asList(
                    "§7Spieler verstecken/zeigen",
                    "",
                    "§7Status§8: §cAlle versteckt",
                    "",
                    "§eRechtsklick zum Anzeigen"
            ));
        }

        item.setItemMeta(meta);
        return item;
    }
}