package me.leon.lobby.managers;

import me.leon.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class HatManager {

    private final Main plugin;
    private final Map<UUID, ItemStack> activeHats;

    public HatManager(Main plugin) {
        this.plugin = plugin;
        this.activeHats = new HashMap<>();
    }

    public List<String> getAllHats() {
        return Arrays.asList(
                "glass", "diamond_block", "gold_block", "iron_block",
                "emerald_block", "redstone_block", "coal_block",
                "lapis_block", "tnt", "cake", "jukebox",
                "note_block", "crafting_table", "furnace",
                "enchanting_table", "anvil", "beacon",
                "pumpkin", "melon", "hay_block",

                "skull_skeleton", "skull_wither", "skull_zombie",
                "skull_creeper", "skull_player",

                "crown", "santa", "tophat", "wizard",
                "viking", "ninja", "pirate", "astronaut"
        );
    }

    public ItemStack getHatItem(String hatType) {
        ItemStack item;
        ItemMeta meta;

        switch (hatType.toLowerCase()) {
            case "glass":
                item = new ItemStack(Material.GLASS);
                meta = item.getItemMeta();
                meta.setDisplayName("§b§lGlas Hut");
                meta.setLore(Arrays.asList("", "§7Durchsichtig!", "§7Kosten§8: §650 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "diamond_block":
                item = new ItemStack(Material.DIAMOND_BLOCK);
                meta = item.getItemMeta();
                meta.setDisplayName("§b§lDiamant Block");
                meta.setLore(Arrays.asList("", "§7Wertvoll!", "§7Kosten§8: §6500 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "gold_block":
                item = new ItemStack(Material.GOLD_BLOCK);
                meta = item.getItemMeta();
                meta.setDisplayName("§6§lGold Block");
                meta.setLore(Arrays.asList("", "§7Glänzend!", "§7Kosten§8: §6400 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "iron_block":
                item = new ItemStack(Material.IRON_BLOCK);
                meta = item.getItemMeta();
                meta.setDisplayName("§7§lEisen Block");
                meta.setLore(Arrays.asList("", "§7Stabil!", "§7Kosten§8: §6200 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "emerald_block":
                item = new ItemStack(Material.EMERALD_BLOCK);
                meta = item.getItemMeta();
                meta.setDisplayName("§a§lSmaragd Block");
                meta.setLore(Arrays.asList("", "§7Luxuriös!", "§7Kosten§8: §6600 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "tnt":
                item = new ItemStack(Material.TNT);
                meta = item.getItemMeta();
                meta.setDisplayName("§c§lTNT");
                meta.setLore(Arrays.asList("", "§7BOOM!", "§7Kosten§8: §6300 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "cake":
                item = new ItemStack(Material.CAKE_BLOCK);
                meta = item.getItemMeta();
                meta.setDisplayName("§d§lKuchen");
                meta.setLore(Arrays.asList("", "§7Lecker!", "§7Kosten§8: §6250 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "pumpkin":
                item = new ItemStack(Material.PUMPKIN);
                meta = item.getItemMeta();
                meta.setDisplayName("§6§lKürbis");
                meta.setLore(Arrays.asList("", "§7Gruselig!", "§7Kosten§8: §6150 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "beacon":
                item = new ItemStack(Material.BEACON);
                meta = item.getItemMeta();
                meta.setDisplayName("§e§lBeacon");
                meta.setLore(Arrays.asList("", "§7Leuchtkraft!", "§7Kosten§8: §6700 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "anvil":
                item = new ItemStack(Material.ANVIL);
                meta = item.getItemMeta();
                meta.setDisplayName("§7§lAmboss");
                meta.setLore(Arrays.asList("", "§7Schwer!", "§7Kosten§8: §6350 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "skull_skeleton":
                item = new ItemStack(Material.SKULL_ITEM, 1, (short) 0);
                meta = item.getItemMeta();
                meta.setDisplayName("§f§lSkelett Schädel");
                meta.setLore(Arrays.asList("", "§7Spooky!", "§7Kosten§8: §6200 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "skull_wither":
                item = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
                meta = item.getItemMeta();
                meta.setDisplayName("§8§lWither Schädel");
                meta.setLore(Arrays.asList("", "§7Dunkel!", "§7Kosten§8: §6800 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "skull_zombie":
                item = new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
                meta = item.getItemMeta();
                meta.setDisplayName("§2§lZombie Kopf");
                meta.setLore(Arrays.asList("", "§7Braaains!", "§7Kosten§8: §6250 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "skull_creeper":
                item = new ItemStack(Material.SKULL_ITEM, 1, (short) 4);
                meta = item.getItemMeta();
                meta.setDisplayName("§a§lCreeper Kopf");
                meta.setLore(Arrays.asList("", "§7Ssssss!", "§7Kosten§8: §6300 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                break;

            case "crown":
                item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setDisplayName("§6§lKrone");
                skullMeta.setLore(Arrays.asList("", "§7Königlich!", "§7Kosten§8: §61000 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                item.setItemMeta(skullMeta);
                return item;

            case "santa":
                item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setDisplayName("§c§lWeihnachtsmann Mütze");
                skullMeta.setLore(Arrays.asList("", "§7Ho Ho Ho!", "§7Kosten§8: §6500 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                item.setItemMeta(skullMeta);
                return item;

            case "tophat":
                item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setDisplayName("§8§lZylinder");
                skullMeta.setLore(Arrays.asList("", "§7Elegant!", "§7Kosten§8: §6600 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                item.setItemMeta(skullMeta);
                return item;

            case "wizard":
                item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setDisplayName("§5§lZauberer Hut");
                skullMeta.setLore(Arrays.asList("", "§7Magisch!", "§7Kosten§8: §6750 Coins", "", "§a▸ Klicke zum Kaufen/Tragen"));
                item.setItemMeta(skullMeta);
                return item;

            default:
                item = new ItemStack(Material.BARRIER);
                meta = item.getItemMeta();
                meta.setDisplayName("§cUnbekannter Hut");
                break;
        }

        item.setItemMeta(meta);
        return item;
    }

    public int getHatCost(String hatType) {
        switch (hatType.toLowerCase()) {
            case "glass": return 50;
            case "pumpkin": return 150;
            case "iron_block": case "skull_skeleton": return 200;
            case "skull_zombie": case "cake": return 250;
            case "tnt": case "skull_creeper": return 300;
            case "anvil": return 350;
            case "gold_block": return 400;
            case "diamond_block": case "santa": return 500;
            case "emerald_block": case "tophat": return 600;
            case "beacon": return 700;
            case "wizard": return 750;
            case "skull_wither": return 800;
            case "crown": return 1000;
            default: return 999999;
        }
    }

    public void equipHat(Player player, String hatType) {
        ItemStack hat = getHatItemForWear(hatType);
        if (hat != null) {
            ItemStack oldHelmet = player.getInventory().getHelmet();

            player.getInventory().setHelmet(hat);
            activeHats.put(player.getUniqueId(), hat);

            player.getWorld().playEffect(player.getLocation().add(0, 1, 0), org.bukkit.Effect.ENDER_SIGNAL, 0);
        }
    }

    public void removeHat(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.getInventory().setHelmet(null);
        }
        activeHats.remove(uuid);
    }

    public boolean hasHat(UUID uuid) {
        return activeHats.containsKey(uuid);
    }

    private ItemStack getHatItemForWear(String hatType) {
        switch (hatType.toLowerCase()) {
            case "glass": return new ItemStack(Material.GLASS);
            case "diamond_block": return new ItemStack(Material.DIAMOND_BLOCK);
            case "gold_block": return new ItemStack(Material.GOLD_BLOCK);
            case "iron_block": return new ItemStack(Material.IRON_BLOCK);
            case "emerald_block": return new ItemStack(Material.EMERALD_BLOCK);
            case "tnt": return new ItemStack(Material.TNT);
            case "cake": return new ItemStack(Material.CAKE_BLOCK);
            case "pumpkin": return new ItemStack(Material.PUMPKIN);
            case "beacon": return new ItemStack(Material.BEACON);
            case "anvil": return new ItemStack(Material.ANVIL);
            case "skull_skeleton": return new ItemStack(Material.SKULL_ITEM, 1, (short) 0);
            case "skull_wither": return new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
            case "skull_zombie": return new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
            case "skull_creeper": return new ItemStack(Material.SKULL_ITEM, 1, (short) 4);
            default: return new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        }
    }
}