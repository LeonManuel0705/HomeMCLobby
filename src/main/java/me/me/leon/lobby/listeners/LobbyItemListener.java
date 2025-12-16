package me.leon.lobby.listeners;

import me.leon.core.managers.RankManager;
import me.leon.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LobbyItemListener implements Listener {

    private final Main plugin;
    private final Map<UUID, Boolean> playerVisibility;

    public LobbyItemListener(Main plugin) {
        this.plugin = plugin;
        this.playerVisibility = new HashMap<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta()) {
            return;
        }

        if (!plugin.isLobbyWorld(player.getWorld().getName())) {
            return;
        }

        String displayName = item.getItemMeta().getDisplayName();

        if (displayName == null) {
            return;
        }

        if (displayName.equals("§b§lNavigator")) {
            event.setCancelled(true);
            openGameModeNavigator(player);
        }
        else if (displayName.equals("§e§lProfil")) {
            event.setCancelled(true);
            openProfileGUI(player);
        }
        else if (displayName.equals("§d§lGadgets")) {
            event.setCancelled(true);
            GadgetGUIListener gadgetGUI = new GadgetGUIListener(plugin);
            gadgetGUI.openGadgetMenu(player);
        }
        else if (displayName.equals("§6§lLobby Wechseln")) {
            event.setCancelled(true);
            openLobbySwitcher(player);
        }
        else if (displayName.contains("Spieler")) {
            event.setCancelled(true);
            togglePlayerVisibility(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        if (title.equals("§b§l⚡ Navigator")) {
            event.setCancelled(true);
            handleGameModeClick(player, event.getCurrentItem());
        }
        else if (title.equals("§6§l⚡ Lobby Wechseln")) {
            event.setCancelled(true);
            handleLobbySwitcherClick(player, event.getCurrentItem());
        }
        else if (title.equals("§e§l⚡ Profil")) {
            event.setCancelled(true);
            handleProfileClick(player, event.getCurrentItem());
        }
    }

    private void openGameModeNavigator(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, "§b§l⚡ Navigator");

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i : new int[]{0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44}) {
            inv.setItem(i, glass);
        }

        ItemStack bedwars = new ItemStack(Material.BED);
        ItemMeta bedwarsMeta = bedwars.getItemMeta();
        bedwarsMeta.setDisplayName("§c§lBedWars");
        bedwarsMeta.setLore(Arrays.asList(
                "§7",
                "§7Beschütze dein Bett und",
                "§7zerstöre die Betten deiner Gegner!",
                "§7",
                "§a▸ Klicke zum Verbinden"
        ));
        bedwars.setItemMeta(bedwarsMeta);
        inv.setItem(11, bedwars);

        ItemStack skywars = new ItemStack(Material.GRASS);
        ItemMeta skywarsMeta = skywars.getItemMeta();
        skywarsMeta.setDisplayName("§2§lSkyWars");
        skywarsMeta.setLore(Arrays.asList(
                "§7",
                "§7Kämpfe auf schwebenden Inseln",
                "§7und werde der letzte Überlebende!",
                "§7",
                "§a▸ Klicke zum Verbinden"
        ));
        skywars.setItemMeta(skywarsMeta);
        inv.setItem(13, skywars);

        ItemStack bridge = new ItemStack(Material.IRON_FENCE);
        ItemMeta bridgeMeta = bridge.getItemMeta();
        bridgeMeta.setDisplayName("§9§lThe§c§lBridge");
        bridgeMeta.setLore(Arrays.asList(
                "§7",
                "§7Überquere die Brücke und",
                "§7springe in das gegnerische Portal!",
                "§7",
                "§a▸ Klicke zum Verbinden"
        ));
        bridge.setItemMeta(bridgeMeta);
        inv.setItem(15, bridge);

        ItemStack buildffa = new ItemStack(Material.WOOD);
        ItemMeta buildffaMeta = buildffa.getItemMeta();
        buildffaMeta.setDisplayName("§e§lBuildFFA");
        buildffaMeta.setLore(Arrays.asList(
                "§7",
                "§7Baue und kämpfe gleichzeitig",
                "§7in diesem spannenden Modus!",
                "§7",
                "§a▸ Klicke zum Verbinden"
        ));
        buildffa.setItemMeta(buildffaMeta);
        inv.setItem(20, buildffa);

        ItemStack hideseek = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta hideseekMeta = hideseek.getItemMeta();
        hideseekMeta.setDisplayName("§5§lHideAndSeek");
        hideseekMeta.setLore(Arrays.asList(
                "§7",
                "§7Verstecke dich als Block oder",
                "§7finde alle versteckten Spieler!",
                "§7",
                "§a▸ Klicke zum Verbinden"
        ));
        hideseek.setItemMeta(hideseekMeta);
        inv.setItem(22, hideseek);

        ItemStack buildbattle = new ItemStack(Material.BRICK);
        ItemMeta buildbattleMeta = buildbattle.getItemMeta();
        buildbattleMeta.setDisplayName("§b§lBuildBattle");
        buildbattleMeta.setLore(Arrays.asList(
                "§7",
                "§7Zeige deine Kreativität und",
                "§7baue zu verschiedenen Themen!",
                "§7",
                "§a▸ Klicke zum Verbinden"
        ));
        buildbattle.setItemMeta(buildbattleMeta);
        inv.setItem(24, buildbattle);

        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("§e§lGameModes");
        infoMeta.setLore(Arrays.asList(
                "§7",
                "§7Klicke auf einen GameMode,",
                "§7um dich zu verbinden!",
                "§7",
                "§6§lViel Spaß beim Spielen!"
        ));
        info.setItemMeta(infoMeta);
        inv.setItem(40, info);

        player.openInventory(inv);
        player.playSound(player.getLocation(), org.bukkit.Sound.CHEST_OPEN, 1.0f, 1.0f);
    }

    private void openLobbySwitcher(Player player) {
        boolean hasPremiumAccess = player.hasPermission("lobby.premiumlobby");

        Inventory inv = Bukkit.createInventory(null, 54, "§6§l⚡ Lobby Wechseln");

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i : new int[]{0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53}) {
            inv.setItem(i, glass);
        }

        ItemStack separator = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta sepMeta = separator.getItemMeta();
        sepMeta.setDisplayName("§8§m-------------------");
        separator.setItemMeta(sepMeta);
        for (int i = 27; i < 36; i++) {
            if (i != 31) inv.setItem(i, separator);
        }

        int lobby1Count = countPlayersInWorld("world");
        int lobby2Count = countPlayersInWorld("Lobby2");
        int lobby3Count = countPlayersInWorld("Lobby3");

        ItemStack lobby1 = new ItemStack(Material.EMERALD);
        ItemMeta lobby1Meta = lobby1.getItemMeta();
        lobby1Meta.setDisplayName("§a§lLobby-1");
        lobby1Meta.setLore(Arrays.asList(
                "§7",
                "§7Spieler§8: §e" + lobby1Count,
                "§7",
                "§a▸ Klicke zum Wechseln"
        ));
        lobby1.setItemMeta(lobby1Meta);
        inv.setItem(11, lobby1);

        ItemStack lobby2 = new ItemStack(Material.EMERALD);
        ItemMeta lobby2Meta = lobby2.getItemMeta();
        lobby2Meta.setDisplayName("§a§lLobby-2");
        lobby2Meta.setLore(Arrays.asList(
                "§7",
                "§7Spieler§8: §e" + lobby2Count,
                "§7",
                "§a▸ Klicke zum Wechseln"
        ));
        lobby2.setItemMeta(lobby2Meta);
        inv.setItem(13, lobby2);

        ItemStack lobby3 = new ItemStack(Material.EMERALD);
        ItemMeta lobby3Meta = lobby3.getItemMeta();
        lobby3Meta.setDisplayName("§a§lLobby-3");
        lobby3Meta.setLore(Arrays.asList(
                "§7",
                "§7Spieler§8: §e" + lobby3Count,
                "§7",
                "§a▸ Klicke zum Wechseln"
        ));
        lobby3.setItemMeta(lobby3Meta);
        inv.setItem(15, lobby3);

        ItemStack middle = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta middleMeta = middle.getItemMeta();
        middleMeta.setDisplayName("§e§l⬇ Premium Lobbies ⬇");
        middleMeta.setLore(Arrays.asList(
                "§7",
                "§7Exklusive Lobbies für",
                hasPremiumAccess ? "§aPremium-Mitglieder!" : "§cPremium-Mitglieder!",
                "§7"
        ));
        middle.setItemMeta(middleMeta);
        inv.setItem(31, middle);

        if (hasPremiumAccess) {
            ItemStack premium1 = new ItemStack(Material.DIAMOND);
            ItemMeta premium1Meta = premium1.getItemMeta();
            int premium1Count = countPlayersInWorld("Premiumlobby1");
            premium1Meta.setDisplayName("§b§lPremiumLobby-1");
            premium1Meta.setLore(Arrays.asList(
                    "§7",
                    "§6§lPREMIUM LOBBY",
                    "§7Spieler§8: §e" + premium1Count,
                    "§7",
                    "§a▸ Klicke zum Wechseln"
            ));
            premium1.setItemMeta(premium1Meta);
            inv.setItem(38, premium1);

            ItemStack premium2 = new ItemStack(Material.DIAMOND);
            ItemMeta premium2Meta = premium2.getItemMeta();
            int premium2Count = countPlayersInWorld("Premiumlobby2");
            premium2Meta.setDisplayName("§b§lPremiumLobby-2");
            premium2Meta.setLore(Arrays.asList(
                    "§7",
                    "§6§lPREMIUM LOBBY",
                    "§7Spieler§8: §e" + premium2Count,
                    "§7",
                    "§a▸ Klicke zum Wechseln"
            ));
            premium2.setItemMeta(premium2Meta);
            inv.setItem(42, premium2);
        } else {
            ItemStack locked1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta locked1Meta = locked1.getItemMeta();
            locked1Meta.setDisplayName("§c§l✖ PremiumLobby-1");

            RankManager.RankData vip = plugin.getRankManager().getRankData("homemc.vip");
            String vipFormatted = vip.color + vip.displayName;

            locked1Meta.setLore(Arrays.asList(
                    "§7",
                    "§c§lGESPERRT",
                    "§7",
                    "§7Benötigt: " + vipFormatted,
                    "§7"
            ));
            locked1.setItemMeta(locked1Meta);
            inv.setItem(38, locked1);

            ItemStack locked2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta locked2Meta = locked2.getItemMeta();
            locked2Meta.setDisplayName("§c§l✖ PremiumLobby-2");
            locked2Meta.setLore(Arrays.asList(
                    "§7",
                    "§c§lGESPERRT",
                    "§7",
                    "§7Benötigt: " + vipFormatted,
                    "§7"
            ));
            locked2.setItemMeta(locked2Meta);
            inv.setItem(42, locked2);
        }

        ItemStack info = new ItemStack(Material.COMPASS);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("§6§lLobby Info");
        infoMeta.setLore(Arrays.asList(
                "§7",
                "§7Normale Lobbies§8: §a3",
                "§7Premium Lobbies§8: " + (hasPremiumAccess ? "§a2" : "§c2 §7(Gesperrt)"),
                "§7",
                "§7Wähle eine Lobby mit",
                "§7weniger Spielern für eine",
                "§7bessere Performance!",
                "§7"
        ));
        info.setItemMeta(infoMeta);
        inv.setItem(49, info);

        player.openInventory(inv);
        player.playSound(player.getLocation(), org.bukkit.Sound.CHEST_OPEN, 1.0f, 1.0f);
    }

    private void openProfileGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§e§l⚡ Profil");

        ItemStack nickItem;
        if (plugin.getNickManager() != null && plugin.getNickManager().isNicked(player.getUniqueId())) {
            nickItem = new ItemStack(Material.REDSTONE);
            ItemMeta meta = nickItem.getItemMeta();
            meta.setDisplayName("§c§lEntnicken");
            meta.setLore(Arrays.asList(
                    "",
                    "§7Dein Nick§8: §e" + plugin.getNickManager().getNickData(player.getUniqueId()).nickName,
                    "",
                    "§c▸ Klicke zum Entnicken"
            ));
            nickItem.setItemMeta(meta);
        } else {
            nickItem = new ItemStack(Material.EMERALD);
            ItemMeta meta = nickItem.getItemMeta();
            meta.setDisplayName("§a§lNicken");
            meta.setLore(Arrays.asList(
                    "",
                    "§7Verstecke deine Identität!",
                    "",
                    "§a▸ Klicke zum Nicken"
            ));
            nickItem.setItemMeta(meta);
        }
        inv.setItem(11, nickItem);

        ItemStack autoNick = new ItemStack(Material.WATCH);
        ItemMeta autoMeta = autoNick.getItemMeta();
        autoMeta.setDisplayName("§6§lAutoNick");
        boolean enabled = plugin.getNickManager() != null && plugin.getNickManager().hasAutoNick(player.getUniqueId());
        autoMeta.setLore(Arrays.asList(
                "",
                "§7Status§8: " + (enabled ? "§a§lAKTIV" : "§c§lINAKTIV"),
                "",
                "§7Automatisch nicken beim",
                "§7Verlassen der Lobby",
                "",
                "§e▸ Klicke zum Umschalten"
        ));
        autoNick.setItemMeta(autoMeta);
        inv.setItem(15, autoNick);

        ItemStack stats = new ItemStack(Material.PAPER);
        ItemMeta statsMeta = stats.getItemMeta();
        statsMeta.setDisplayName("§b§lStatistiken");

        int coins = plugin.getCoinManager() != null ? plugin.getCoinManager().getCoins(player.getUniqueId()) : 0;
        int friends = plugin.getFriendManager() != null ? plugin.getFriendManager().getFriends(player.getUniqueId()).size() : 0;

        RankManager rankManager = plugin.getRankManager();
        String rank = rankManager != null ? rankManager.getRankColor(player) + rankManager.getRankName(player) : "§7Spieler";

        statsMeta.setLore(Arrays.asList(
                "",
                "§7Rang§8: " + rank,
                "§7Coins§8: §6" + coins,
                "§7Freunde§8: §e" + friends,
                ""
        ));
        stats.setItemMeta(statsMeta);
        inv.setItem(13, stats);

        player.openInventory(inv);
        player.playSound(player.getLocation(), org.bukkit.Sound.CHEST_OPEN, 1.0f, 1.0f);
    }

    private void handleGameModeClick(Player player, ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return;
        }

        String displayName = item.getItemMeta().getDisplayName();

        if (displayName == null) {
            return;
        }

        switch (displayName) {
            case "§c§lBedWars":
                plugin.sendPlayerToServer(player, "bedwars");
                player.sendMessage(Main.PREFIX + "§aDu wirst zu §cBedWars §averbunden...");
                break;
            case "§2§lSkyWars":
                plugin.sendPlayerToServer(player, "skywars");
                player.sendMessage(Main.PREFIX + "§aDu wirst zu §2SkyWars §averbunden...");
                break;
            case "§9§lThe§c§lBridge":
                plugin.sendPlayerToServer(player, "thebridge");
                player.sendMessage(Main.PREFIX + "§aDu wirst zu §9§lThe§c§lBridge§r §averbunden...");
                break;
            case "§e§lBuildFFA":
                plugin.sendPlayerToServer(player, "buildffa");
                player.sendMessage(Main.PREFIX + "§aDu wirst zu §eBuildFFA §averbunden...");
                break;
            case "§5§lHideAndSeek":
                plugin.sendPlayerToServer(player, "hideandseek");
                player.sendMessage(Main.PREFIX + "§aDu wirst zu §5HideAndSeek §averbunden...");
                break;
            case "§b§lBuildBattle":
                plugin.sendPlayerToServer(player, "buildbattle");
                player.sendMessage(Main.PREFIX + "§aDu wirst zu §bBuildBattle §averbunden...");
                break;
        }

        player.playSound(player.getLocation(), org.bukkit.Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
        player.closeInventory();
    }

    private void handleLobbySwitcherClick(Player player, ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return;
        }

        String displayName = item.getItemMeta().getDisplayName();

        if (displayName == null) {
            return;
        }

        if (displayName.equals("§a§lLobby-1")) {
            executeWarpCommand(player, "lobby-1");
        } else if (displayName.equals("§a§lLobby-2")) {
            executeWarpCommand(player, "lobby-2");
        } else if (displayName.equals("§a§lLobby-3")) {
            executeWarpCommand(player, "lobby-3");
        }
        else if (displayName.equals("§b§lPremiumLobby-1")) {
            if (!player.hasPermission("lobby.premiumlobby")) {
                showPremiumDeniedMessage(player);
                return;
            }
            executeWarpCommand(player, "premiumlobby-1");
        } else if (displayName.equals("§b§lPremiumLobby-2")) {
            if (!player.hasPermission("lobby.premiumlobby")) {
                showPremiumDeniedMessage(player);
                return;
            }
            executeWarpCommand(player, "premiumlobby-2");
        }
        else if (displayName.contains("§c§l✖ PremiumLobby")) {
            showPremiumDeniedMessage(player);
        }
    }

    private void executeWarpCommand(Player player, String warpName) {
        Location warp = plugin.getWarpManager().getWarp(warpName);

        if (warp == null) {
            player.sendMessage(Main.PREFIX + "§cDieser Warp existiert nicht!");
            player.closeInventory();
            return;
        }

        player.teleport(warp);
        player.sendMessage(Main.PREFIX + "§aDu wurdest zu §e" + warpName + " §ateleportiert!");
        player.playSound(player.getLocation(), org.bukkit.Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
        player.closeInventory();
    }

    private void showPremiumDeniedMessage(Player player) {
        RankManager.RankData vip = plugin.getRankManager().getRankData("homemc.vip");
        String vipFormatted = vip.color + vip.displayName;

        player.sendMessage("§8§m=======§r §c§lZugriff verweigert §8§m=======§r");
        player.sendMessage("§3Um Premium-Lobbies betreten zu können, benötigst du:");
        player.sendMessage(" §8• §2Rang: " + vipFormatted);
        player.sendMessage("§8§m====================================");
        player.playSound(player.getLocation(), org.bukkit.Sound.VILLAGER_NO, 1.0f, 1.0f);
        player.closeInventory();
    }

    private void handleProfileClick(Player player, ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return;
        }

        String displayName = item.getItemMeta().getDisplayName();

        if (displayName == null) {
            return;
        }

        if (displayName.equals("§a§lNicken")) {
            if (plugin.getNickManager() != null) {
                if (!player.hasPermission("homemc.nick")) {
                    RankManager.RankData legend = plugin.getRankManager().getRankData("homemc.legend");
                    String legendPrefix = legend.prefix;
                    String legendColor = legend.color;
                    String legendName = legend.displayName;
                    String legendFormatted = legendColor + legendName;
                    player.sendMessage("§8§m=======§r §c§lZugriff verweigert §8§m=======§r");
                    player.sendMessage("§3Um dich zu nicken, benötigst du:");
                    player.sendMessage(" §8• §2Rang: " + legendFormatted);
                    player.sendMessage("§8§m====================================");
                    player.closeInventory();
                    return;
                }
                plugin.getNickManager().nickPlayer(player);
                player.playSound(player.getLocation(), org.bukkit.Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            player.closeInventory();
        }
        else if (displayName.equals("§c§lEntnicken")) {
            if (plugin.getNickManager() != null) {
                plugin.getNickManager().unnickPlayer(player);
                player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BREAK, 1.0f, 1.0f);
            }
            player.closeInventory();
        }
        else if (displayName.equals("§6§lAutoNick")) {
            if (!player.hasPermission("homemc.autonick")) {
                RankManager.RankData yt = plugin.getRankManager().getRankData("homemc.yt");
                String ytFormatted = yt.color + yt.displayName;

                player.sendMessage("§8§m=======§r §c§lZugriff verweigert §8§m=======§r");
                player.sendMessage("§3Um AutoNick nutzen zu können, benötigst du:");
                player.sendMessage(" §8• §2Rang: " + ytFormatted);
                player.sendMessage("§8§m====================================");
                player.closeInventory();
                return;
            }

            if (plugin.getNickManager() != null) {
                boolean current = plugin.getNickManager().hasAutoNick(player.getUniqueId());
                plugin.getNickManager().setAutoNick(player.getUniqueId(), !current);

                if (!current) {
                    if (!player.hasPermission("homemc.autonick")) {
                        RankManager.RankData yt = plugin.getRankManager().getRankData("homemc.yt");
                        String ytFormatted = yt.color + yt.displayName;

                        player.sendMessage("§8§m=======§r §c§lZugriff verweigert §8§m=======§r");
                        player.sendMessage("§3Um AutoNick nutzen zu können, benötigst du:");
                        player.sendMessage(" §8• §2Rang: " + ytFormatted);
                        player.sendMessage("§8§m====================================");
                        player.closeInventory();
                        return;
                    }
                    player.sendMessage(Main.PREFIX + "§aAutoNick aktiviert!");
                    player.sendMessage(Main.PREFIX + "§7Du wirst beim Verlassen der Lobby automatisch genickt.");
                    player.playSound(player.getLocation(), org.bukkit.Sound.NOTE_PLING, 1.0f, 2.0f);
                } else {
                    player.sendMessage(Main.PREFIX + "§cAutoNick deaktiviert!");
                    player.playSound(player.getLocation(), org.bukkit.Sound.NOTE_BASS, 1.0f, 1.0f);
                }
            }

            player.closeInventory();
        }
    }

    private void togglePlayerVisibility(Player player) {
        if (!player.hasPermission("lobby.silentmode")) {
            RankManager.RankData vip = plugin.getRankManager().getRankData("homemc.vip");
            String vipPrefix = vip.prefix;
            String vipColor = vip.color;
            String vipName = vip.displayName;
            String vipFormatted = vipColor + vipName;
            player.sendMessage("§8§m=======§r §c§lZugriff verweigert §8§m=======§r");
            player.sendMessage("§3Um andere Spieler zu verstecken, benötigst du:");
            player.sendMessage(" §8• §2Rang: " + vipFormatted);
            player.sendMessage("§8§m====================================");
            player.playSound(player.getLocation(), org.bukkit.Sound.VILLAGER_NO, 1.0f, 1.0f);
            return;
        }
        UUID uuid = player.getUniqueId();
        boolean currentlyVisible = playerVisibility.getOrDefault(uuid, true);

        boolean newState = !currentlyVisible;
        playerVisibility.put(uuid, newState);

        if (newState) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(player)) {
                    player.showPlayer(other);
                }
            }

            player.getInventory().setItem(7, me.leon.lobby.utils.LobbyItems.getVisibilityItem(true));
            player.sendMessage(Main.PREFIX + "§aSpieler werden wieder angezeigt!");
            player.playSound(player.getLocation(), org.bukkit.Sound.LEVEL_UP, 1.0f, 1.5f);
        } else {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(player)) {
                    player.hidePlayer(other);
                }
            }

            player.getInventory().setItem(7, me.leon.lobby.utils.LobbyItems.getVisibilityItem(false));
            player.sendMessage(Main.PREFIX + "§cSpieler wurden versteckt!");
            player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BREAK, 1.0f, 0.5f);
        }
    }

    public void cleanup(UUID uuid) {
        playerVisibility.remove(uuid);
    }

    private int countPlayersInWorld(String worldName) {
        if (Bukkit.getWorld(worldName) == null) {
            return 0;
        }
        return Bukkit.getWorld(worldName).getPlayers().size();
    }
}