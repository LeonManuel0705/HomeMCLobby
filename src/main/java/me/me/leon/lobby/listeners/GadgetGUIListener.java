package me.leon.lobby.listeners;

import me.leon.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GadgetGUIListener implements Listener {

    private final Main plugin;

    public GadgetGUIListener(Main plugin) {
        this.plugin = plugin;
    }

    public void openGadgetMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§d§l⚡ Gadgets & Cosmetics");

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i : new int[]{0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53}) {
            inv.setItem(i, glass);
        }

        ItemStack particles = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta particleMeta = particles.getItemMeta();
        particleMeta.setDisplayName("§6§lPartikel Effekte");
        particleMeta.setLore(Arrays.asList(
                "",
                "§7Coole Partikel-Trails!",
                "",
                "§e▸ Klicke zum Öffnen"
        ));
        particles.setItemMeta(particleMeta);
        inv.setItem(20, particles);

        ItemStack pets = new ItemStack(Material.MONSTER_EGG, 1, (short) 95);
        ItemMeta petMeta = pets.getItemMeta();
        petMeta.setDisplayName("§a§lPets");
        petMeta.setLore(Arrays.asList(
                "",
                "§7Treue Begleiter!",
                "",
                "§e▸ Klicke zum Öffnen"
        ));
        pets.setItemMeta(petMeta);
        inv.setItem(22, pets);

        ItemStack hats = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta hatMeta = hats.getItemMeta();
        hatMeta.setDisplayName("§b§lHüte");
        hatMeta.setLore(Arrays.asList(
                "",
                "§7Stylische Kopfbedeckungen!",
                "",
                "§e▸ Klicke zum Öffnen"
        ));
        hats.setItemMeta(hatMeta);
        inv.setItem(24, hats);

        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("§e§lInformation");
        int coins = plugin.getCoinManager().getCoins(player.getUniqueId());
        infoMeta.setLore(Arrays.asList(
                "",
                "§7Deine Coins§8: §6" + coins,
                "",
                "§7Kaufe Cosmetics mit Coins!",
                "§7Verdiene Coins durch Spielen."
        ));
        info.setItemMeta(infoMeta);
        inv.setItem(49, info);

        player.openInventory(inv);
    }

    public void openParticleMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§6§l⚡ Partikel Effekte");

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i : new int[]{0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53}) {
            inv.setItem(i, glass);
        }

        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        int slotIndex = 0;

        for (String gadget : plugin.getGadgetManager().getAllGadgets()) {
            if (slotIndex >= slots.length) break;
            inv.setItem(slots[slotIndex++], plugin.getGadgetManager().getGadgetItem(gadget));
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§c§lZurück");
        back.setItemMeta(backMeta);
        inv.setItem(48, back);

        ItemStack disable = new ItemStack(Material.BARRIER);
        ItemMeta disableMeta = disable.getItemMeta();
        disableMeta.setDisplayName("§c§lEffekt Deaktivieren");
        disable.setItemMeta(disableMeta);
        inv.setItem(49, disable);

        player.openInventory(inv);
    }

    public void openPetMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§a§l⚡ Pets");

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i : new int[]{0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53}) {
            inv.setItem(i, glass);
        }

        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        int slotIndex = 0;

        for (String pet : plugin.getPetManager().getAllPets()) {
            if (slotIndex >= slots.length) break;
            ItemStack petItem = plugin.getPetManager().getPetItem(pet);

            boolean owned = plugin.getShopManager().hasPurchased(player.getUniqueId(), "pet_" + pet);
            ItemMeta meta = petItem.getItemMeta();
            if (owned) {
                meta.setLore(Arrays.asList("", "§a§lIM BESITZ", "", "§e▸ Klicke zum Spawnen"));
            }
            petItem.setItemMeta(meta);

            inv.setItem(slots[slotIndex++], petItem);
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§c§lZurück");
        back.setItemMeta(backMeta);
        inv.setItem(48, back);

        ItemStack remove = new ItemStack(Material.BARRIER);
        ItemMeta removeMeta = remove.getItemMeta();
        removeMeta.setDisplayName("§c§lPet Entfernen");
        remove.setItemMeta(removeMeta);
        inv.setItem(49, remove);

        player.openInventory(inv);
    }

    public void openHatMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§b§l⚡ Hüte");

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i : new int[]{0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53}) {
            inv.setItem(i, glass);
        }

        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        int slotIndex = 0;

        for (String hat : plugin.getHatManager().getAllHats()) {
            if (slotIndex >= slots.length) break;
            ItemStack hatItem = plugin.getHatManager().getHatItem(hat);

            boolean owned = plugin.getShopManager().hasPurchased(player.getUniqueId(), "hat_" + hat);
            ItemMeta meta = hatItem.getItemMeta();
            if (owned) {
                meta.setLore(Arrays.asList("", "§a§lIM BESITZ", "", "§e▸ Klicke zum Tragen"));
            }
            hatItem.setItemMeta(meta);

            inv.setItem(slots[slotIndex++], hatItem);
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§c§lZurück");
        back.setItemMeta(backMeta);
        inv.setItem(48, back);

        ItemStack remove = new ItemStack(Material.BARRIER);
        ItemMeta removeMeta = remove.getItemMeta();
        removeMeta.setDisplayName("§c§lHut Abnehmen");
        remove.setItemMeta(removeMeta);
        inv.setItem(49, remove);

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        ItemStack item = event.getCurrentItem();

        if (item == null || !item.hasItemMeta()) return;

        String displayName = item.getItemMeta().getDisplayName();

        if (title.equals("§d§l⚡ Gadgets & Cosmetics")) {
            event.setCancelled(true);

            if (displayName.equals("§6§lPartikel Effekte")) {
                openParticleMenu(player);
                player.playSound(player.getLocation(), org.bukkit.Sound.CLICK, 1.0f, 1.0f);
            } else if (displayName.equals("§a§lPets")) {
                openPetMenu(player);
                player.playSound(player.getLocation(), org.bukkit.Sound.CLICK, 1.0f, 1.0f);
            } else if (displayName.equals("§b§lHüte")) {
                openHatMenu(player);
                player.playSound(player.getLocation(), org.bukkit.Sound.CLICK, 1.0f, 1.0f);
            }
        }

        else if (title.equals("§6§l⚡ Partikel Effekte")) {
            event.setCancelled(true);

            if (displayName.equals("§c§lZurück")) {
                openGadgetMenu(player);
                player.playSound(player.getLocation(), org.bukkit.Sound.CLICK, 1.0f, 1.0f);
            } else if (displayName.equals("§c§lEffekt Deaktivieren")) {
                plugin.getGadgetManager().setGadget(player.getUniqueId(), null);
                player.sendMessage(Main.PREFIX + "§cPartikel-Effekt deaktiviert!");
                player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BREAK, 1.0f, 1.0f);
                player.closeInventory();
            } else {
                for (String gadget : plugin.getGadgetManager().getAllGadgets()) {
                    ItemStack gadgetItem = plugin.getGadgetManager().getGadgetItem(gadget);
                    if (gadgetItem.getItemMeta().getDisplayName().equals(displayName)) {
                        plugin.getGadgetManager().setGadget(player.getUniqueId(), gadget);
                        player.sendMessage(Main.PREFIX + "§aPartikel aktiviert§8: " + displayName);
                        player.playSound(player.getLocation(), org.bukkit.Sound.LEVEL_UP, 1.0f, 1.5f);
                        player.closeInventory();
                        break;
                    }
                }
            }
        }

        else if (title.equals("§a§l⚡ Pets")) {
            event.setCancelled(true);

            if (displayName.equals("§c§lZurück")) {
                openGadgetMenu(player);
                player.playSound(player.getLocation(), org.bukkit.Sound.CLICK, 1.0f, 1.0f);
            } else if (displayName.equals("§c§lPet Entfernen")) {
                plugin.getPetManager().removePet(player.getUniqueId());
                player.sendMessage(Main.PREFIX + "§cPet entfernt!");
                player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BREAK, 1.0f, 1.0f);
                player.closeInventory();
            } else {
                for (String pet : plugin.getPetManager().getAllPets()) {
                    ItemStack petItem = plugin.getPetManager().getPetItem(pet);
                    if (petItem.getItemMeta().getDisplayName().equals(displayName)) {
                        handlePetPurchase(player, pet);
                        break;
                    }
                }
            }
        }

        else if (title.equals("§b§l⚡ Hüte")) {
            event.setCancelled(true);

            if (displayName.equals("§c§lZurück")) {
                openGadgetMenu(player);
                player.playSound(player.getLocation(), org.bukkit.Sound.CLICK, 1.0f, 1.0f);
            } else if (displayName.equals("§c§lHut Abnehmen")) {
                plugin.getHatManager().removeHat(player.getUniqueId());
                player.sendMessage(Main.PREFIX + "§cHut abgenommen!");
                player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BREAK, 1.0f, 1.0f);
                player.closeInventory();
            } else {
                for (String hat : plugin.getHatManager().getAllHats()) {
                    ItemStack hatItem = plugin.getHatManager().getHatItem(hat);
                    if (hatItem.getItemMeta().getDisplayName().equals(displayName)) {
                        handleHatPurchase(player, hat);
                        break;
                    }
                }
            }
        }
    }

    private void handlePetPurchase(Player player, String petType) {
        String itemId = "pet_" + petType;

        if (plugin.getShopManager().hasPurchased(player.getUniqueId(), itemId)) {
            plugin.getPetManager().spawnPet(player, petType);
            player.sendMessage(Main.PREFIX + "§aPet gespawnt!");
            player.playSound(player.getLocation(), org.bukkit.Sound.LEVEL_UP, 1.0f, 1.5f);
            player.closeInventory();
        } else {
            int cost = plugin.getPetManager().getPetCost(petType);
            int coins = plugin.getCoinManager().getCoins(player.getUniqueId());

            if (coins >= cost) {
                plugin.getShopManager().purchase(player.getUniqueId(), itemId, cost);
                plugin.getPetManager().spawnPet(player, petType);
                player.sendMessage(Main.PREFIX + "§aPet gekauft für §6" + cost + " Coins§a!");
                player.playSound(player.getLocation(), org.bukkit.Sound.LEVEL_UP, 1.0f, 2.0f);
                player.closeInventory();
            } else {
                player.sendMessage(Main.PREFIX + "§cNicht genug Coins! §7(Benötigt: §6" + cost + "§7)");
                player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BREAK, 1.0f, 1.0f);
            }
        }
    }

    private void handleHatPurchase(Player player, String hatType) {
        String itemId = "hat_" + hatType;

        if (plugin.getShopManager().hasPurchased(player.getUniqueId(), itemId)) {
            plugin.getHatManager().equipHat(player, hatType);
            player.sendMessage(Main.PREFIX + "§aHut ausgerüstet!");
            player.playSound(player.getLocation(), org.bukkit.Sound.LEVEL_UP, 1.0f, 1.5f);
            player.closeInventory();
        } else {
            int cost = plugin.getHatManager().getHatCost(hatType);
            int coins = plugin.getCoinManager().getCoins(player.getUniqueId());

            if (coins >= cost) {
                plugin.getShopManager().purchase(player.getUniqueId(), itemId, cost);
                plugin.getHatManager().equipHat(player, hatType);
                player.sendMessage(Main.PREFIX + "§aHut gekauft für §6" + cost + " Coins§a!");
                player.playSound(player.getLocation(), org.bukkit.Sound.LEVEL_UP, 1.0f, 2.0f);
                player.closeInventory();
            } else {
                player.sendMessage(Main.PREFIX + "§cNicht genug Coins! §7(Benötigt: §6" + cost + "§7)");
                player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BREAK, 1.0f, 1.0f);
            }
        }
    }
}