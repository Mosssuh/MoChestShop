package mv.mossuh.mochestshop.UTILITIES;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestShopConfig;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.InvalidInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.PurchasingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.SellingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.TradeInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.Economies.Economy;
import mv.mossuh.mochestshop.UTILITIES.Enchantments.Enchant;
import mv.mossuh.mochestshop.UTILITIES.Enchantments.Enchantments;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mocore.GUI.Holders.SingleGUI;
import mv.mossuh.mocore.ITEM.MoEnchant;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.TimeFormat;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class UtilMethods {

    public static ItemStack getItemInHand(Player player) {
        try {
            if (ServerVersion.isAtLeast(ServerVersion.MC1_9)) {
                return player.getInventory().getItemInMainHand();
            } else {
                return player.getInventory().getItemInHand();
            }

        } catch (Exception e) {
            return new ItemStack(Material.AIR);
        }
    }

    public static int[] getChestInvPositions() {
        return new int[]{20, 21, 22, 23, 24, 29, 30, 31, 32, 33};
    }
    public static List<Integer> getChestInvPositionsList() {
        return new ArrayList<>(Arrays.asList(20, 21, 22, 23, 24, 29, 30, 31, 32, 33));
    }

    public static int getInvPosition(int slot) {
        int[] positions = {20, 21, 22, 23, 24, 29, 30, 31, 32, 33};
        int i = 1;
        for (int pos : positions) {
            if (slot == i) {
                return pos;
            }
            i += 1;
        }
        return -1;
    }

    public static List<MoEnchant> getFormattedEnchants(Enchantments enchants) {
        List<MoEnchant> enchantments = new ArrayList<>();
        if (enchants == null) return new ArrayList<>();

        for (Enchant e : enchants.getEnchants()) {
            Enchantment ench = Enchantment.getByName(e.getEnchantName());
            if (ench == null) {
                UtilString.get(Config.PREFIX + " &cUnknown enchant:" + e.getEnchantName()).hex().sendMessageInConsole();
                continue;
            }
            enchantments.add(new MoEnchant(ench, e.getLevel()));
        }
        return enchantments;
    }

    public static void giveItem(Player player, ItemStack stack, Location location) {
        if (player == null || stack == null) return;
        if (stack.getType() == Material.AIR) return;

        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(stack);
        } else {
            Location dropLocation = location.add(0, 1, 0);
            player.getWorld().dropItemNaturally(dropLocation, stack);
        }
    }


    public static boolean hasEnoughSpace(Player player, ItemStack item, int amount) {
        if (player == null || item == null || amount <= 0) return false;

        int remaining = amount; // cantidad que queremos agregar

        for (ItemStack content : getInventory(player)) {
            if (content == null || content.getType() == Material.AIR) {
                // Slot vacío = cabe un stack completo
                remaining -= item.getMaxStackSize();
            } else if (content.isSimilar(item)) {
                // Misma clase de ítem (tipo, meta, etc.)
                int freeInStack = item.getMaxStackSize() - content.getAmount();
                remaining -= freeInStack;
            }

            if (remaining <= 0) {
                // Ya hay suficiente espacio
                return true;
            }
        }

        return false; // No hubo suficiente espacio
    }

    public static void giveItem(Player player, ItemStack item, int amount, Location location) {
        if (player == null || item == null || amount <= 0) return;

        int maxStack = item.getMaxStackSize();
        int remaining = amount;

        while (remaining > 0) {
            int giveAmount = Math.min(maxStack, remaining);
            ItemStack stackToGive = item.clone();
            stackToGive.setAmount(giveAmount);

            if (hasEnoughSpace(player, stackToGive, giveAmount)) {
                player.getInventory().addItem(stackToGive);
            } else {
                Location dropLocation = location != null ? location.clone().add(0, 1, 0) : player.getLocation().clone().add(0, 1, 0);
                player.getWorld().dropItemNaturally(dropLocation, stackToGive);
            }

            remaining -= giveAmount;
        }

        player.updateInventory();
    }

    public static boolean hasItem(Player player, ItemStack item, int amount) {
        if (player == null || item == null || amount <= 0) return false;

        int has = 0;

        for (ItemStack content : getInventory(player)) {
            if (content == null || content.getType() == Material.AIR) continue;

            if (item.isSimilar(content)) {
                has += content.getAmount();
            }
        }

        return has >= amount;
    }

    public static void removeItem(Player player, ItemStack item, int amount) {
        if (player == null || item == null || amount <= 0) return;

        int remaining = amount;

        ItemStack[] contents = getInventory(player);

        for (int i = 0; i < contents.length; i++) {
            ItemStack content = contents[i];

            if (content == null || content.getType() == Material.AIR) continue;

            if (!item.isSimilar(content)) continue;

            int stackAmount = content.getAmount();

            if (stackAmount > remaining) {
                content.setAmount(stackAmount - remaining);
                return;
            } else {
                remaining -= stackAmount;
                contents[i] = null; // remove stack
            }

            if (remaining <= 0) break;
        }
        player.getInventory().setContents(contents);
        player.updateInventory();
    }

    private static ItemStack[] getInventory(Player player) {
        if (ServerVersion.isAtLeast(ServerVersion.MC1_9)) {
            return player.getInventory().getStorageContents();
        } else {
            return player.getInventory().getContents();
        }
    }

    public static List<VariableArg> getChestShopConfigVariables(ChestShopConfig config) {
        List<VariableArg> variables = new ArrayList<>();
        if (config != null && config.isValid()) {
            int defaultSlots = config.getUnlockedSlots();
            List<String> economies = config.getEconomies();
            variables.add(new VariableArg("%max_slots%", 10+""));
            variables.add(new VariableArg("%unlocked_slots%", defaultSlots+""));
            for (int i = 0; i < economies.size(); i++) {
                int pos = i + 1;
                String economy = economies.get(i);
                variables.add(new VariableArg("%economy_" + pos + "%", economy));
            }
        }
        return variables;
    }

    public static List<VariableArg> getChestShopVariables(ChestShop chest) {
        List<VariableArg> variables = new ArrayList<>();
        if (chest != null && chest.isValid()) {
            int defaultSlots = chest.getUnlockedSlots();
            String selectedEconomy = chest.getSelectedEconomy();
            Economy economy = Configs.getEconomy(selectedEconomy);
            String prefix = economy.getPrefix();
            List<String> economies = chest.getEconomies();
            UUID ownerUUID = chest.getOwner();
            OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUUID);
            variables.add(new VariableArg("%player%", owner.getName()));
            variables.add(new VariableArg("%chest_status%", chest.getStatus().getPrefix()));
            variables.add(new VariableArg("%chest_trade%", chest.getTradeType().getPrefix()));

            for (Slot slot : chest.getSlots()) {
                ItemStack stack = slot.getItem();
                if (stack.getItemMeta() != null) {
                    String slotPrefix = UtilString.get(stack.getItemMeta().getDisplayName()).setPlaceholders(ownerUUID).apply();
                    variables.add(new VariableArg("%chest_items_name_" + slot.getSlot() + "%", slotPrefix));
                }
            }


            variables.add(new VariableArg("%max_slots%", 10+""));
            variables.add(new VariableArg("%unlocked_slots%", defaultSlots+""));
            variables.add(new VariableArg("%selected_economy%", selectedEconomy));
            variables.add(new VariableArg("%selected_economy_prefix%", prefix));
            for (int i = 0; i < economies.size(); i++) {
                int pos = i + 1;
                String e = economies.get(i);
                variables.add(new VariableArg("%economy_" + pos + "%", e));
            }
        }
        return variables;
    }

    public static List<String> getListSlotFormat(UUID ownerUUID, List<Slot> slots) {
        List<String> names = new ArrayList<>();

        if (slots.isEmpty()) {
            names.add(UtilString.get(Config.NO_ITEMS_FOR_TRADE).hex().apply());
            return names;
        }

        for (int i = 0; i < slots.size(); i++) {
            int pos = i+1;
            List<VariableArg> variables = new ArrayList<>();
            variables.add(new VariableArg("%position%", pos+""));
            Slot slot = slots.get(i);
            TradeType type = slot.getInfo().getType();
            if (type.equals(TradeType.SELL)) {
                String slotPrefix = UtilString.get(Config.SELL_LIST_ITEMS_FORMAT).setVariables(variables)
                        .setSlot(slot).setPlaceholders(ownerUUID).hex().apply();
                names.add(slotPrefix);
            } else if (type.equals(TradeType.BUY)) {
                String slotPrefix = UtilString.get(Config.BUY_LIST_ITEMS_FORMAT).setVariables(variables)
                        .setSlot(slot).setPlaceholders(ownerUUID).hex().apply();
                names.add(slotPrefix);
            }
        }
        return names;
    }

    public static List<VariableArg> getSlotVariables(TradeInfo info) {
        List<VariableArg> variables = new ArrayList<>();
        TradeType type = info.getType();
        switch (type) {
            case SELL:
                SellingInfo selling = (SellingInfo) info;
                int soldItems = selling.getSoldItems();
                int saleItems = selling.getItemsForSale();
                double sellingPrice = selling.getSellingPrice();
                double earnings = soldItems*sellingPrice;
                variables.add(new VariableArg("%sold_items%", soldItems+""));
                variables.add(new VariableArg("%sale_items%", saleItems+""));
                variables.add(new VariableArg("%selling_price%", UsefulMethods.formatNumber(sellingPrice, Config.MAX_DECIMAL)));
                variables.add(new VariableArg("%sales_earnings%", UsefulMethods.formatNumber(earnings, Config.MAX_DECIMAL)));
                break;
            case BUY:
                PurchasingInfo purchasing = (PurchasingInfo) info;
                variables.add(new VariableArg("%purchased_items%", purchasing.getPurchasedItems()+""));
                variables.add(new VariableArg("%purchasing_price%", UsefulMethods.formatNumber(purchasing.getPurchasingPrice(), Config.MAX_DECIMAL)));
                break;
        }
        return variables;
    }

    public static List<VariableArg> getSlotVariables(Slot slot) {
        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%slot%", slot.getSlot()+""));
        TradeInfo info = slot.getInfo();
        TradeType type = info.getType();
        ItemStack stack = slot.getItem();
        if (stack.getItemMeta() != null && stack.getItemMeta().hasDisplayName()) {
            variables.add(new VariableArg("%item_name%", stack.getItemMeta().getDisplayName()));
        } else {
            variables.add(new VariableArg("%item_name%", stack.getType().name()));
        }
        switch (type) {
            case SELL:
                SellingInfo selling = (SellingInfo) info;
                int soldItems = selling.getSoldItems();
                int saleItems = selling.getItemsForSale();
                double sellingPrice = selling.getSellingPrice();
                double earnings = soldItems*sellingPrice;
                variables.add(new VariableArg("%sold_items%", soldItems+""));
                variables.add(new VariableArg("%sale_items%", saleItems+""));
                variables.add(new VariableArg("%selling_price%", UsefulMethods.formatNumber(sellingPrice, Config.MAX_DECIMAL)));
                variables.add(new VariableArg("%sales_earnings%", UsefulMethods.formatNumber(earnings, Config.MAX_DECIMAL)));
                break;
            case BUY:
                PurchasingInfo purchasing = (PurchasingInfo) info;
                variables.add(new VariableArg("%purchased_items%", purchasing.getPurchasedItems()+""));
                variables.add(new VariableArg("%purchasing_price%", UsefulMethods.formatNumber(purchasing.getPurchasingPrice(), Config.MAX_DECIMAL)));
                break;
        }
        return variables;
    }

    public static void inventoryCloser(Location location) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryView view = player.getOpenInventory();
            if (view == null) continue;

            InventoryHolder top = view.getTopInventory().getHolder();
            if (top == null) continue;

            if (!(top instanceof SingleGUI)) continue;

            SingleGUI playerGUI = (SingleGUI) top;
            if (playerGUI.getLocation().equals(location)) {
                player.closeInventory();
            }
        }
    }

    public static Set<Player> closeInventoryPlayers(Location location, Player ignore) {
        Set<Player> players = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryView view = player.getOpenInventory();
            if (view == null) continue;

            InventoryHolder top = view.getTopInventory().getHolder();
            if (top == null) continue;

            if (!(top instanceof SingleGUI)) continue;

            SingleGUI playerGUI = (SingleGUI) top;
            if (playerGUI.getLocation().equals(location)) {

                if (player.getUniqueId().equals(ignore.getUniqueId())) {
                    continue;
                }
                player.closeInventory();
                players.add(player);
            }
        }
        return players;
    }

    public static List<Slot> getSlots(String serializedItems, String serializedData) {
        if (serializedItems == null || serializedData == null) return new ArrayList<>();
        ItemStack[] items = Slot.deserializeItemStack(serializedItems);
        Map<Integer, TradeInfo> slotData = Slot.deserializeSlotData(serializedData);
        List<Slot> slots = new ArrayList<>();
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                int slot = i + 1;
                TradeInfo info = slotData.getOrDefault(slot, new InvalidInfo());
                Slot s = new Slot(slot, items[i], info);
                slots.add(s);
            }
        }
        return slots;
    }

    public static TimeFormat getTimeFormat() {
        String YEAR = "y";
        String MONTH = "m";
        String WEAK = "w";
        String DAY = "d";
        String HOUR = "h";
        String MINUTE = "m";
        String SECOND = "s";
        return new TimeFormat(YEAR, MONTH, WEAK, DAY, HOUR, MINUTE, SECOND);
    }

    public static List<Economy> getEconomies(ActiveChestShop active) {
        List<String> ids = active.getChest().getEconomies();
        List<Economy> economies = new ArrayList<>();
        int current = 0;
        for (String id : ids) {
            if  (current >= 10) break;
            if (Configs.hasEconomy(id)) {
                economies.add(Configs.getEconomy(id));
                current += 1;
            }
        }
        return economies;
    }

    public static List<String> getEconomyIDs(ActiveChestShop active) {
        List<String> ids = active.getChest().getEconomies();
        List<String> economies = new ArrayList<>();
        int current = 0;
        for (String id : ids) {
            if  (current >= 10) break;
            if (Configs.hasEconomy(id)) {
                economies.add(Configs.getEconomy(id).getID());
                current += 1;
            }
        }
        return economies;
    }
}
