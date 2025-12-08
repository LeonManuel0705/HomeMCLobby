package me.leon.lobby;

import me.leon.core.managers.*;
import me.leon.lobby.commands.*;
import me.leon.lobby.database.MySQL;
import me.leon.lobby.listeners.*;
import me.leon.lobby.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Animals;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import me.leon.core.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Main extends JavaPlugin implements PluginMessageListener {

    private static Main instance;

    private Core core;

    private MySQL mySQL;

    private WarpManager warpManager;
    private GadgetManager gadgetManager;
    private BuildManager buildManager;
    private ScoreboardManager scoreboardManager;
    private TabManager tabManager;
    private ShopManager shopManager;
    private RocketManager rocketManager;
    private PetManager petManager;
    private HatManager hatManager;

    public static final String PREFIX = "§aLobby §8» ";

    @Override
    public void onEnable() {
        instance = this;

        this.core = (Core) Bukkit.getPluginManager().getPlugin("Core");

        if (this.core == null) {
            getLogger().severe("=============================================");
            getLogger().severe("§4§lCRITICAL ERROR");
            getLogger().severe("§c§lCORE PLUGIN NICHT GEFUNDEN!");
            getLogger().severe("§cPlugin wird deaktiviert...");
            getLogger().severe("=============================================");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("§aCore Plugin erfolgreich geladen!");

        saveDefaultConfig();

        this.mySQL = new MySQL(this);
        mySQL.connect();

        this.warpManager = new WarpManager(this);
        this.gadgetManager = new GadgetManager(this);
        this.buildManager = new BuildManager();
        this.scoreboardManager = new ScoreboardManager(this);
        this.tabManager = new TabManager(this);
        this.shopManager = new ShopManager(this);
        this.rocketManager = new RocketManager(this);
        this.petManager = new PetManager(this);
        this.hatManager = new HatManager(this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        getCommand("delwarp").setExecutor(new DelWarpCommand(this));
        getCommand("build").setExecutor(new BuildCommand(this));
        getCommand("petname").setExecutor(new PetNameCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LobbyProtectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LobbyItemListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GadgetListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RocketListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GadgetGUIListener(this), this);
        Bukkit.getPluginManager().registerEvents(petManager, this);

        for (World world : Bukkit.getWorlds()) {
            if (isLobbyWorld(world.getName())) {
                world.setSpawnFlags(false, false);

                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Monster || entity instanceof Animals) {
                        entity.remove();
                    }
                }
            }
        }

        // Mob-Cleaner Task
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (World world : Bukkit.getWorlds()) {
                if (isLobbyWorld(world.getName())) {
                    for (Entity entity : world.getEntities()) {
                        // Pets nicht entfernen!
                        if (entity.hasMetadata("homemc.pet")) {
                            continue;
                        }

                        if (entity instanceof Monster || entity instanceof Animals) {
                            entity.remove();
                        }
                    }
                }
            }
        }, 20L * 5, 20L * 5); // Alle 5 Sekunden

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "=================================");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "LobbySystem aktiviert!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Version: " + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Core: " + (core != null ? "§aGeladen" : "§cNicht gefunden"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MySQL: " + (mySQL.isConnected() ? "§aVerbunden" : "§cGetrennt"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Pets: §aAktiviert");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Hats: §aAktiviert");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "=================================");
    }

    @Override
    public void onDisable() {
        if (petManager != null) {
            for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                petManager.removePet(player.getUniqueId());
            }
        }

        if (mySQL != null && mySQL.isConnected()) {
            mySQL.disconnect();
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LobbySystem deaktiviert!");
    }

    @Override
    public void onPluginMessageReceived(String channel, org.bukkit.entity.Player player, byte[] message) {
    }

    public void sendPlayerToServer(org.bukkit.entity.Player player, String serverName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
    }

    public static Main getInstance() {
        return instance;
    }

    public Core getCore() {
        return core;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public GadgetManager getGadgetManager() {
        return gadgetManager;
    }

    public BuildManager getBuildManager() {
        return buildManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public TabManager getTabManager() {
        return tabManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public RocketManager getRocketManager() {
        return rocketManager;
    }

    // NEUE Getter
    public PetManager getPetManager() {
        return petManager;
    }

    public HatManager getHatManager() {
        return hatManager;
    }

    public boolean isLobbyWorld(String worldName) {
        for (String s : getConfig().getStringList("lobby-worlds")) {
            if (s.equalsIgnoreCase(worldName)) return true;
        }
        return false;
    }

    public RankManager getRankManager() {
        if (core == null) {
            getLogger().warning("Core is null when accessing RankManager!");
            return null;
        }
        return core.getRankManager();
    }

    public FriendManager getFriendManager() {
        if (core == null) {
            getLogger().warning("Core is null when accessing FriendManager!");
            return null;
        }
        return core.getFriendManager();
    }

    public NickManager getNickManager() {
        if (core == null) {
            getLogger().warning("Core is null when accessing NickManager!");
            return null;
        }
        return core.getNickManager();
    }

    public CoinManager getCoinManager() {
        if (core == null) {
            getLogger().warning("Core is null when accessing CoinManager!");
            return null;
        }
        return core.getCoinManager();
    }

    public PartyManager getPartyManager() {
        if (core == null) {
            getLogger().warning("Core is null when accessing PartyManager!");
            return null;
        }
        return core.getPartyManager();
    }

    public ClanManager getClanManager() {
        if (core == null) {
            getLogger().warning("Core is null when accessing ClanManager!");
            return null;
        }
        return core.getClanManager();
    }
}