package me.leon.lobby.managers;

import me.leon.core.managers.RankManager;
import me.leon.lobby.Main;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PetManager implements Listener {

    private final Main plugin;
    private final Map<UUID, Entity> activePets;
    private final Map<UUID, String> petNames;
    private final Map<UUID, String> petTypes;

    public PetManager(Main plugin) {
        this.plugin = plugin;
        this.activePets = new HashMap<>();
        this.petNames = new HashMap<>();
        this.petTypes = new HashMap<>();

        startPetFollowTask();
    }

    public List<String> getAllPets() {
        return Arrays.asList(
                "dog", "cat", "pig", "sheep", "cow",
                "chicken", "rabbit", "horse", "ocelot",
                "wolf", "villager", "zombie_pigman", "iron_golem",
                "snowman", "mushroom_cow", "slime", "magma_cube",
                "silverfish", "endermite", "bat"
        );
    }

    public ItemStack getPetItem(String petType) {
        ItemStack item = new ItemStack(Material.MONSTER_EGG);
        ItemMeta meta = item.getItemMeta();

        switch (petType.toLowerCase()) {
            case "dog":
                item.setDurability((short) 95);
                meta.setDisplayName("§6§lHund");
                meta.setLore(Arrays.asList("", "§7Treuer Begleiter!", "§7Kosten§8: §6250 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "cat":
                item.setDurability((short) 98);
                meta.setDisplayName("§e§lKatze");
                meta.setLore(Arrays.asList("", "§7Niedliche Katze!", "§7Kosten§8: §6200 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "pig":
                item.setDurability((short) 90);
                meta.setDisplayName("§d§lSchwein");
                meta.setLore(Arrays.asList("", "§7Oink oink!", "§7Kosten§8: §6150 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "sheep":
                item.setDurability((short) 91);
                meta.setDisplayName("§f§lSchaf");
                meta.setLore(Arrays.asList("", "§7Flauschig und süß!", "§7Kosten§8: §6150 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "cow":
                item.setDurability((short) 92);
                meta.setDisplayName("§8§lKuh");
                meta.setLore(Arrays.asList("", "§7Muuuuh!", "§7Kosten§8: §6150 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "chicken":
                item.setDurability((short) 93);
                meta.setDisplayName("§f§lHuhn");
                meta.setLore(Arrays.asList("", "§7Gackernder Freund!", "§7Kosten§8: §6100 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "rabbit":
                item.setDurability((short) 101);
                meta.setDisplayName("§7§lHase");
                meta.setLore(Arrays.asList("", "§7Hüpfender Begleiter!", "§7Kosten§8: §6200 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "horse":
                item.setDurability((short) 100);
                meta.setDisplayName("§6§lPferd");
                meta.setLore(Arrays.asList("", "§7Majestätischer Begleiter!", "§7Kosten§8: §6300 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "mushroom_cow":
                item.setDurability((short) 96);
                meta.setDisplayName("§c§lMooshroom");
                meta.setLore(Arrays.asList("", "§7Pilzkuh!", "§7Kosten§8: §6400 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "iron_golem":
                item.setDurability((short) 99);
                meta.setDisplayName("§7§lEisengolem");
                meta.setLore(Arrays.asList("", "§7Starker Beschützer!", "§7Kosten§8: §6500 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "snowman":
                item.setDurability((short) 97);
                meta.setDisplayName("§f§lSchneemann");
                meta.setLore(Arrays.asList("", "§7Frostiger Freund!", "§7Kosten§8: §6350 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "slime":
                item.setDurability((short) 55);
                meta.setDisplayName("§a§lSchleim");
                meta.setLore(Arrays.asList("", "§7Hüpfender Schleim!", "§7Kosten§8: §6300 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "magma_cube":
                item.setDurability((short) 62);
                meta.setDisplayName("§c§lMagmawürfel");
                meta.setLore(Arrays.asList("", "§7Heißer Begleiter!", "§7Kosten§8: §6350 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "bat":
                item.setDurability((short) 65);
                meta.setDisplayName("§8§lFledermaus");
                meta.setLore(Arrays.asList("", "§7Fliegender Freund!", "§7Kosten§8: §6250 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "silverfish":
                item.setDurability((short) 60);
                meta.setDisplayName("§7§lSilberfischchen");
                meta.setLore(Arrays.asList("", "§7Kleiner Krieger!", "§7Kosten§8: §6200 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "endermite":
                item.setDurability((short) 67);
                meta.setDisplayName("§5§lEndermilbe");
                meta.setLore(Arrays.asList("", "§7Mysteriöser Begleiter!", "§7Kosten§8: §6300 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "villager":
                item.setDurability((short) 120);
                meta.setDisplayName("§2§lDorfbewohner");
                meta.setLore(Arrays.asList("", "§7Hmmm...", "§7Kosten§8: §6250 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            case "zombie_pigman":
                item.setDurability((short) 57);
                meta.setDisplayName("§d§lZombie Pigman");
                meta.setLore(Arrays.asList("", "§7Nether Freund!", "§7Kosten§8: §6400 Coins", "", "§a▸ Klicke zum Kaufen/Spawnen"));
                break;

            default:
                meta.setDisplayName("§7Unbekanntes Pet");
                break;
        }

        item.setItemMeta(meta);
        return item;
    }

    public int getPetCost(String petType) {
        switch (petType.toLowerCase()) {
            case "chicken": return 100;
            case "pig": case "sheep": case "cow": return 150;
            case "cat": case "rabbit": case "silverfish": return 200;
            case "dog": case "bat": case "villager": return 250;
            case "horse": case "slime": case "endermite": return 300;
            case "snowman": case "magma_cube": return 350;
            case "mushroom_cow": case "zombie_pigman": return 400;
            case "iron_golem": return 500;
            default: return 999999;
        }
    }

    public void spawnPet(Player player, String petType) {
        if (activePets.containsKey(player.getUniqueId())) {
            removePet(player.getUniqueId());
        }

        Location loc = player.getLocation();
        Entity pet = null;

        switch (petType.toLowerCase()) {
            case "dog":
                Wolf wolf = (Wolf) loc.getWorld().spawnEntity(loc, EntityType.WOLF);
                wolf.setTamed(true);
                wolf.setOwner(player);
                wolf.setAdult();
                wolf.setCollarColor(DyeColor.RED);
                pet = wolf;
                break;

            case "cat":
                Ocelot ocelot = (Ocelot) loc.getWorld().spawnEntity(loc, EntityType.OCELOT);
                ocelot.setCatType(Ocelot.Type.RED_CAT);
                ocelot.setAdult();
                pet = ocelot;
                break;

            case "pig":
                Pig pig = (Pig) loc.getWorld().spawnEntity(loc, EntityType.PIG);
                pig.setAdult();
                pet = pig;
                break;

            case "sheep":
                Sheep sheep = (Sheep) loc.getWorld().spawnEntity(loc, EntityType.SHEEP);
                sheep.setColor(DyeColor.getByColor(Color.fromRGB((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255))));
                sheep.setAdult();
                pet = sheep;
                break;

            case "cow":
                Cow cow = (Cow) loc.getWorld().spawnEntity(loc, EntityType.COW);
                cow.setAdult();
                pet = cow;
                break;

            case "chicken":
                Chicken chicken = (Chicken) loc.getWorld().spawnEntity(loc, EntityType.CHICKEN);
                chicken.setAdult();
                pet = chicken;
                break;

            case "rabbit":
                Rabbit rabbit = (Rabbit) loc.getWorld().spawnEntity(loc, EntityType.RABBIT);
                rabbit.setAdult();
                pet = rabbit;
                break;

            case "horse":
                Horse horse = (Horse) loc.getWorld().spawnEntity(loc, EntityType.HORSE);
                horse.setAdult();
                horse.setTamed(true);
                horse.setOwner(player);
                pet = horse;
                break;

            case "mushroom_cow":
                MushroomCow mooshroom = (MushroomCow) loc.getWorld().spawnEntity(loc, EntityType.MUSHROOM_COW);
                mooshroom.setAdult();
                pet = mooshroom;
                break;

            case "iron_golem":
                pet = loc.getWorld().spawnEntity(loc, EntityType.IRON_GOLEM);
                break;

            case "snowman":
                pet = loc.getWorld().spawnEntity(loc, EntityType.SNOWMAN);
                break;

            case "slime":
                Slime slime = (Slime) loc.getWorld().spawnEntity(loc, EntityType.SLIME);
                slime.setSize(1);
                pet = slime;
                break;

            case "magma_cube":
                MagmaCube magma = (MagmaCube) loc.getWorld().spawnEntity(loc, EntityType.MAGMA_CUBE);
                magma.setSize(1);
                pet = magma;
                break;

            case "bat":
                pet = loc.getWorld().spawnEntity(loc, EntityType.BAT);
                break;

            case "silverfish":
                pet = loc.getWorld().spawnEntity(loc, EntityType.SILVERFISH);
                break;

            case "endermite":
                pet = loc.getWorld().spawnEntity(loc, EntityType.ENDERMITE);
                break;

            case "villager":
                Villager villager = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
                villager.setAdult();
                pet = villager;
                break;

            case "zombie_pigman":
                PigZombie pigman = (PigZombie) loc.getWorld().spawnEntity(loc, EntityType.PIG_ZOMBIE);
                pigman.setBaby(false);
                pet = pigman;
                break;
        }

        if (pet != null) {
            pet.setCustomNameVisible(true);
            String petName = petNames.getOrDefault(player.getUniqueId(), player.getName() + "s Pet");
            pet.setCustomName("§e" + petName);
            pet.setMetadata("homemc.pet", new FixedMetadataValue(plugin, player.getUniqueId().toString()));

            if (pet instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) pet;
                living.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true, false));
                living.setRemoveWhenFarAway(false);
            }

            activePets.put(player.getUniqueId(), pet);
            petTypes.put(player.getUniqueId(), petType);
        }
    }

    public void removePet(UUID uuid) {
        if (activePets.containsKey(uuid)) {
            Entity pet = activePets.get(uuid);
            if (pet != null && !pet.isDead()) {
                pet.remove();
            }
            activePets.remove(uuid);
            petTypes.remove(uuid);
        }
    }

    public boolean hasPet(UUID uuid) {
        return activePets.containsKey(uuid) && activePets.get(uuid) != null && !activePets.get(uuid).isDead();
    }

    public void setPetName(UUID uuid, String name) {
        petNames.put(uuid, name);
        if (hasPet(uuid)) {
            activePets.get(uuid).setCustomName("§e" + name);
        }
    }

    public String getPetName(UUID uuid) {
        return petNames.getOrDefault(uuid, "Pet");
    }

    private void startPetFollowTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Map.Entry<UUID, Entity> entry : new HashMap<>(activePets).entrySet()) {
                Player player = Bukkit.getPlayer(entry.getKey());
                Entity pet = entry.getValue();

                if (player == null || !player.isOnline() || pet == null || pet.isDead()) {
                    removePet(entry.getKey());
                    continue;
                }

                if (!plugin.isLobbyWorld(player.getWorld().getName())) {
                    removePet(entry.getKey());
                    continue;
                }

                Location petLoc = pet.getLocation();
                Location playerLoc = player.getLocation();

                double distance = petLoc.distance(playerLoc);

                if (distance > 15) {
                    pet.teleport(playerLoc);
                    continue;
                }

                if (distance > 3) {
                    Location target = playerLoc.clone();

                    double x = target.getX() - petLoc.getX();
                    double y = target.getY() - petLoc.getY();
                    double z = target.getZ() - petLoc.getZ();

                    double length = Math.sqrt(x * x + y * y + z * z);
                    x /= length;
                    y /= length;
                    z /= length;

                    double speed = 0.3;
                    Location newLoc = petLoc.clone().add(x * speed, y * speed, z * speed);
                    newLoc.setYaw(petLoc.getYaw());
                    newLoc.setPitch(petLoc.getPitch());

                    pet.teleport(newLoc);
                }
            }
        }, 0L, 5L);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity.hasMetadata("homemc.pet")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();

        if (entity.hasMetadata("homemc.pet")) {
            String ownerUUID = entity.getMetadata("homemc.pet").get(0).asString();
            if (player.getUniqueId().toString().equals(ownerUUID)) {
                player.sendMessage(Main.PREFIX + "§7Benutze §e/petname <Name> §7um deinem Pet einen Namen zu geben!");
                if (player.hasPermission("homemc.chatcolor")) {
                    player.sendMessage(Main.PREFIX + "§7Du kannst Farbcodes in deinem Petnamen verwenden!");
                } else {
                    RankManager.RankData vip = plugin.getRankManager() != null ? plugin.getRankManager().getRankData("homemc.vip") : null;
                    String vipFormatted = vip != null ? vip.color + vip.displayName : "§6VIP";
                    player.sendMessage(Main.PREFIX + "§7Um Farbcodes im Namen zu verwenden, benötigst du: §2Rang: " + vipFormatted);
                }
            }
            event.setCancelled(true);
        }
    }
}