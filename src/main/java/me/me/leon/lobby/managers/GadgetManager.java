package me.leon.lobby.managers;

import me.leon.lobby.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GadgetManager {

    private final Main plugin;
    private final Map<UUID, String> activeGadgets;
    private final Map<UUID, Integer> gadgetTimers;

    public GadgetManager(Main plugin) {
        this.plugin = plugin;
        this.activeGadgets = new HashMap<>();
        this.gadgetTimers = new HashMap<>();
        startGadgetTask();
    }

    public void setGadget(UUID uuid, String gadgetName) {
        if (gadgetName == null) {
            activeGadgets.remove(uuid);
            gadgetTimers.remove(uuid);
        } else {
            activeGadgets.put(uuid, gadgetName.toLowerCase());
            gadgetTimers.put(uuid, 0);
        }
    }

    public String getActiveGadget(UUID uuid) {
        return activeGadgets.get(uuid);
    }

    public boolean hasGadget(UUID uuid) {
        return activeGadgets.containsKey(uuid);
    }

    public List<String> getAllGadgets() {
        return Arrays.asList(
                "hearts", "flames", "magic", "snow", "rainbow",
                "smoketrail", "lightning", "cloud", "notes",
                "ender", "lava", "water", "spiral", "wings",
                "halo", "storm"
        );
    }

    public ItemStack getGadgetItem(String gadgetName) {
        ItemStack item = new ItemStack(Material.FIREWORK);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            switch (gadgetName.toLowerCase()) {
                case "hearts":
                    item.setType(Material.RED_ROSE);
                    meta.setDisplayName("§c§l❤ Herzen");
                    meta.setLore(Arrays.asList("", "§7Zeige deine Liebe!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "flames":
                    item.setType(Material.BLAZE_POWDER);
                    meta.setDisplayName("§6§l⚡ Flammen");
                    meta.setLore(Arrays.asList("", "§7Sei on fire!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "magic":
                    item.setType(Material.MAGMA_CREAM);
                    meta.setDisplayName("§d§l✦ Magie");
                    meta.setLore(Arrays.asList("", "§7Magische Partikel!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "snow":
                    item.setType(Material.SNOW_BALL);
                    meta.setDisplayName("§f§l❄ Schnee");
                    meta.setLore(Arrays.asList("", "§7Schneeflocken!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "rainbow":
                    item.setType(Material.INK_SACK);
                    meta.setDisplayName("§5§l✨ Regenbogen");
                    meta.setLore(Arrays.asList("", "§7Ein bunter Farbwirbel!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "smoketrail":
                    item.setType(Material.SULPHUR);
                    meta.setDisplayName("§8§l☁ Rauchspur");
                    meta.setLore(Arrays.asList("", "§7Hinterlasse eine Rauchwolke!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "lightning":
                    item.setType(Material.GLOWSTONE_DUST);
                    meta.setDisplayName("§e§l⚡ Blitz");
                    meta.setLore(Arrays.asList("", "§7Zücke den Donner!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "cloud":
                    item.setType(Material.WEB);
                    meta.setDisplayName("§f§l☁ Wolke");
                    meta.setLore(Arrays.asList("", "§7Schwebe in den Lüften!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "notes":
                    item.setType(Material.NOTE_BLOCK);
                    meta.setDisplayName("§3§l♪ Musik Noten");
                    meta.setLore(Arrays.asList("", "§7Musikalische Vibes!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "ender":
                    item.setType(Material.ENDER_PEARL);
                    meta.setDisplayName("§5§l⬡ Ender");
                    meta.setLore(Arrays.asList("", "§7Mysteriöse Partikel!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "lava":
                    item.setType(Material.LAVA_BUCKET);
                    meta.setDisplayName("§c§l🔥 Lava");
                    meta.setLore(Arrays.asList("", "§7Heiße Lava-Tropfen!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "water":
                    item.setType(Material.WATER_BUCKET);
                    meta.setDisplayName("§9§l💧 Wasser");
                    meta.setLore(Arrays.asList("", "§7Erfrischende Wassertropfen!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "spiral":
                    item.setType(Material.FIREWORK_CHARGE);
                    meta.setDisplayName("§b§l◉ Spirale");
                    meta.setLore(Arrays.asList("", "§7Rotierende Spirale!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "wings":
                    item.setType(Material.FEATHER);
                    meta.setDisplayName("§e§l⚝ Flügel");
                    meta.setLore(Arrays.asList("", "§7Engelflügel!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "halo":
                    item.setType(Material.GOLD_NUGGET);
                    meta.setDisplayName("§6§l◯ Heiligenschein");
                    meta.setLore(Arrays.asList("", "§7Goldener Heiligenschein!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                case "storm":
                    item.setType(Material.GHAST_TEAR);
                    meta.setDisplayName("§7§l⚡ Sturm");
                    meta.setLore(Arrays.asList("", "§7Wirbelsturm-Effekt!", "", "§a▸ Klicke zum Aktivieren"));
                    break;

                default:
                    meta.setDisplayName("§7Unbekanntes Gadget");
                    meta.setLore(Collections.singletonList("§cFehler beim Laden"));
                    break;
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    private void startGadgetTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                World world = player.getWorld();
                if (world == null || !plugin.isLobbyWorld(world.getName())) continue;
                if (!hasGadget(player.getUniqueId())) continue;

                String gadget = getActiveGadget(player.getUniqueId());
                if (gadget == null) continue;

                int timer = gadgetTimers.getOrDefault(player.getUniqueId(), 0);
                gadgetTimers.put(player.getUniqueId(), timer + 1);

                Location loc = player.getLocation();
                Location center = loc.clone().add(0, 1, 0);

                playGadgetEffect(world, center, gadget, timer, player);
            }
        }, 0L, 3L);
    }

    private void playGadgetEffect(World world, Location center, String gadget, int timer, Player player) {
        switch (gadget) {
            case "hearts":
                world.playEffect(center, Effect.HEART, 0);
                world.playEffect(center.clone().add(0.3, 0, 0.3), Effect.HEART, 0);
                world.playEffect(center.clone().add(-0.3, 0, -0.3), Effect.HEART, 0);
                break;

            case "flames":
                world.playEffect(center, Effect.FLAME, 0);
                world.playEffect(center.clone().add(0.3, 0, 0.3), Effect.FLAME, 0);
                world.playEffect(center.clone().add(-0.3, 0, -0.3), Effect.FLAME, 0);
                break;

            case "magic":
                world.playEffect(center, Effect.WITCH_MAGIC, 0);
                world.playEffect(center.clone().add(0.3, 0.3, 0.3), Effect.WITCH_MAGIC, 0);
                break;

            case "snow":
                world.playEffect(center, Effect.SNOW_SHOVEL, 0);
                world.playEffect(center.clone().add(0.3, 0, 0.3), Effect.SNOW_SHOVEL, 0);
                world.playEffect(center.clone().add(-0.3, 0, -0.3), Effect.SNOW_SHOVEL, 0);
                break;

            case "rainbow":
                for (int i = 0; i < 7; i++) {
                    double angle = (timer + i * 51) * 0.1;
                    double x = Math.cos(angle) * 0.5;
                    double z = Math.sin(angle) * 0.5;
                    world.playEffect(center.clone().add(x, 0, z), Effect.COLOURED_DUST, i);
                }
                break;

            case "smoketrail":
                world.playEffect(center, Effect.SMOKE, 4);
                world.playEffect(center.clone().add(0.2, 0, 0.2), Effect.SMOKE, 4);
                break;

            case "lightning":
                world.playEffect(center, Effect.MOBSPAWNER_FLAMES, 0);
                if (timer % 40 == 0) {
                    world.strikeLightningEffect(center);
                }
                break;

            case "cloud":
                world.playEffect(center, Effect.CLOUD, 0);
                world.playEffect(center.clone().add(0.2, 0, -0.2), Effect.CLOUD, 0);
                break;

            case "notes":
                world.playEffect(center, Effect.NOTE, 0);
                if (timer % 10 == 0) {
                    player.playSound(center, Sound.NOTE_PIANO, 0.5f, (float) (Math.random() + 0.5));
                }
                break;

            case "ender":
                world.playEffect(center, Effect.ENDER_SIGNAL, 0);
                world.playEffect(center.clone().add(0.3, 0.5, 0), Effect.ENDER_SIGNAL, 0);
                break;

            case "lava":
                world.playEffect(center, Effect.LAVA_POP, 0);
                world.playEffect(center.clone().add(0.2, 0.3, 0.2), Effect.LAVA_POP, 0);
                break;

            case "water":
                world.playEffect(center, Effect.WATERDRIP, 0);
                world.playEffect(center.clone().add(0.3, 0.5, 0), Effect.WATERDRIP, 0);
                world.playEffect(center.clone().add(-0.3, 0.5, 0), Effect.WATERDRIP, 0);
                break;

            case "spiral":
                double angle = timer * 0.2;
                for (int i = 0; i < 3; i++) {
                    double a = angle + (i * 120);
                    double x = Math.cos(Math.toRadians(a)) * 0.5;
                    double z = Math.sin(Math.toRadians(a)) * 0.5;
                    double y = (timer % 40) * 0.05;
                    world.playEffect(center.clone().add(x, y, z), Effect.WITCH_MAGIC, 0);
                }
                break;

            case "wings":
                for (int i = 0; i < 3; i++) {
                    world.playEffect(center.clone().add(-0.3 - i * 0.1, 0.5, i * 0.1), Effect.CLOUD, 0);
                }
                for (int i = 0; i < 3; i++) {
                    world.playEffect(center.clone().add(0.3 + i * 0.1, 0.5, i * 0.1), Effect.CLOUD, 0);
                }
                break;

            case "halo":
                for (int i = 0; i < 8; i++) {
                    double a = i * 45;
                    double x = Math.cos(Math.toRadians(a)) * 0.4;
                    double z = Math.sin(Math.toRadians(a)) * 0.4;
                    world.playEffect(center.clone().add(x, 1.5, z), Effect.LAVA_POP, 0);
                }
                break;

            case "storm":
                for (int i = 0; i < 5; i++) {
                    double a = (timer * 10 + i * 72) % 360;
                    double radius = 0.3 + (i * 0.1);
                    double x = Math.cos(Math.toRadians(a)) * radius;
                    double z = Math.sin(Math.toRadians(a)) * radius;
                    world.playEffect(center.clone().add(x, i * 0.2, z), Effect.CLOUD, 0);
                }
                break;
        }
    }
}