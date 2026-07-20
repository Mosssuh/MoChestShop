package mv.mossuh.mochestshop.CONFIGS;

import mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig.*;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestShopConfig;
import mv.mossuh.mochestshop.MoChestShop;
import mv.mossuh.mochestshop.UTILITIES.Economies.Economy;
import mv.mossuh.mochestshop.UTILITIES.Enchantments.Enchantments;
import mv.mossuh.mochestshop.UTILITIES.Enums.ChestStatus;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mochestshop.UTILITIES.ItemInfo;
import mv.mossuh.mocore.CONFIG.FolderConfigs;
import mv.mossuh.mocore.CONFIG.MoConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Configs {
    private static final MoChestShop main = MoChestShop.getInstance();
    private static MoConfig informationConfig;
    private static MoConfig generalConfig;
    private static MoConfig chestsData;
    private static MoConfig messagesConfig;
    private static FolderConfigs chestsFolder;


    public static void configureConfigs() {
        generalConfig = new MoConfig("config.yml", main, null);
        generalConfig.registerConfig(true);
        Config.load();

        informationConfig = new MoConfig("information.yml", main, null);
        informationConfig.registerConfig(true);

        messagesConfig = new MoConfig("messages.yml", main, null);
        messagesConfig.registerConfig(true);
        Messages.load();

        chestsData = new MoConfig("datas/chestshopdata.db", main, null);
        chestsData.registerConfig(false);

        chestsFolder = new FolderConfigs(true, main, "chests");
        chestsFolder.configure("chest_example1.yml");

        ChestStatus.OPEN.setPrefix(Config.OPEN_PREFIX);
        ChestStatus.CLOSED.setPrefix(Config.CLOSED_PREFIX);
        TradeType.BUY.setPrefix(Config.BUY_PREFIX);
        TradeType.SELL.setPrefix(Config.SELL_PREFIX);

        LoadEconomyList();
        LoadChestList();
        LoadCommonItemConfiguration();
        LoadChestConfiguration();
        LoadPurchasingConfiguration();
        LoadSellingConfiguration();

        LoadChestMenuClientConfig();
        LoadSlotTokenConfig();
    }

    public static MoConfig getConfig() { return generalConfig; }
    public static MoConfig getChestsData() { return chestsData; }
    public static MoConfig getMessagesConfig() { return messagesConfig; }

    private static final Set<Economy> economyList = new HashSet<>();

    public static Set<Economy> getEconomies() { return economyList; }
    public static Set<Economy> getEconomies(String applicator) {
        Set<Economy> economies = new HashSet<>();
        for (Economy e : economyList) {
            if (e.getApplicator().equalsIgnoreCase(applicator)) {
                economies.add(e);
            }
        }
        return economies;
    }
    public static Economy getEconomy(String applicator, String economy) {
        for (Economy e : economyList) {
            if (e.getApplicator().equalsIgnoreCase(applicator) && e.getEconomy().equalsIgnoreCase(economy)) {
                return e;
            }
        }
        return new Economy();
    }
    public static boolean hasEconomy(String applicator, String economy) {
        for (Economy e : economyList) {
            if (e.getApplicator().equalsIgnoreCase(applicator) && e.getEconomy().equalsIgnoreCase(economy)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEconomy(String id) {
        for (Economy e : economyList) {
            if (e.getID().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public static Economy getEconomy(String id) {
        for (Economy e : economyList) {
            if (e.getID().equalsIgnoreCase(id)) {
                return e;
            }
        }
        return new Economy();
    }


    private static void LoadEconomyList() {
        FileConfiguration file = getConfig().getConfig();
        if (!file.contains("ECONOMIES") || file.getConfigurationSection("ECONOMIES").getKeys(false).isEmpty()) {
            return;
        }

        for (String applicator : file.getConfigurationSection("ECONOMIES").getKeys(false)) {
            ConfigurationSection applicatorSection = file.getConfigurationSection("ECONOMIES." + applicator);
            if (applicatorSection == null) continue;

            // Caso 1: Applicator doesn't have sub-economies (like Vault)
            if (applicatorSection.contains("id")) {
                String id = applicatorSection.getString("id");
                String prefix = applicatorSection.getString("prefix", applicator);
                Economy economy = new Economy(applicator, "", id, prefix);
                economyList.add(economy);
                continue;
            }

            // Caso 2: Applicator has sub-economies (like Minecraft.Experience)
            for (String subEconomy : applicatorSection.getKeys(false)) {
                ConfigurationSection subSection = file.getConfigurationSection("ECONOMIES." + applicator + "." + subEconomy);
                if (subSection == null) continue;

                String id = subSection.getString("id");
                String prefix = subSection.getString("prefix", subEconomy);
                Economy economy = new Economy(applicator, subEconomy, id, prefix);
                economyList.add(economy);
            }
        }
    }


    private static final Set<ChestShopConfig> chestsList = new HashSet<>();
    public static Set<ChestShopConfig> getChestsShop() { return chestsList; }
    public static ChestShopConfig getChestShop(String section) {
        for (ChestShopConfig c : chestsList) {
            if (c.isSection(section)) {
                return c;
            }
        }
        return new ChestShopConfig();
    }
    public static boolean hasChestShop(String section) {
        for (ChestShopConfig c : chestsList) {
            if (c.isSection(section)) {
                return true;
            }
        }
        return false;
    }

    private static void LoadChestList() {
        List<MoConfig> files = new ArrayList<>();
        files.add(getConfig());
        files.addAll(chestsFolder.getConfigs());

        for (MoConfig vConfig : files) {
            FileConfiguration file = vConfig.getConfig();
            if (!file.contains("CHESTS") || file.getConfigurationSection("CHESTS").getKeys(false).isEmpty()) {
                continue;
            }

            for (String section : file.getConfigurationSection("CHESTS").getKeys(false)) {
                int defaultSlots = file.getInt("CHESTS." + section + ".unlocked-slots");
                List<String> economies = file.getStringList("CHESTS." + section + ".economies");

                String itemName = file.getString("CHESTS." + section + ".item-info.name");
                List<String> itemLore = file.getStringList("CHESTS." + section + ".item-info.lore");
                Boolean itemUnbreakable = file.getBoolean("CHESTS." + section + ".item-info.unbreakable");
                Boolean itemUnique = file.getBoolean("CHESTS." + section + ".item-info.unique");
                List<String> itemEnchantments = file.getStringList("CHESTS." + section + ".item-info.enchantments");
                List<String> itemFlags = file.getStringList("CHESTS." + section + ".item-info.flags");

                ItemInfo itemInfo = new ItemInfo("CHEST", itemName, itemLore, new Enchantments(itemEnchantments), itemFlags, itemUnbreakable, itemUnique);

                ChestShopConfig chest = new ChestShopConfig(section, itemInfo, defaultSlots, economies);
                chestsList.add(chest);
            }
        }
    }

    private static String guiNameConfig = "";
    private static ChestSelectedItemConfig selectedItemConfig = new ChestSelectedItemConfig();
    private static NormalItemConfig unLockedItemConfig = new NormalItemConfig();
    private static NormalItemConfig lockedItemConfig = new NormalItemConfig();
    private static NormalItemConfig closeItemConfig = new NormalItemConfig();
    private static TradeSwitchItemConfig tradeItemConfig = new TradeSwitchItemConfig();
    private static EconomySwitchItemConfig economyItemConfig = new EconomySwitchItemConfig();
    private static ChestStatusSwitchItemConfig chestStatusItemConfig = new ChestStatusSwitchItemConfig();
    private static NormalItemConfig backItemConfig = new NormalItemConfig();

    public static String getGuiNameConfig() { return guiNameConfig; }
    public static ChestSelectedItemConfig getSelectedItemConfig() { return selectedItemConfig; }
    public static NormalItemConfig getUnLockedItemConfig() { return unLockedItemConfig; }
    public static NormalItemConfig getLockedItemConfig() { return lockedItemConfig; }
    public static NormalItemConfig getCloseItemConfig() { return closeItemConfig; }
    public static TradeSwitchItemConfig getTradeItemConfig() { return tradeItemConfig; }
    public static EconomySwitchItemConfig getEconomyItemConfig() { return economyItemConfig; }
    public static ChestStatusSwitchItemConfig getChestStatusItemConfig() { return chestStatusItemConfig; }
    public static NormalItemConfig getBackItemConfig() { return backItemConfig; }

    private static void LoadCommonItemConfiguration() {
        MoConfig config = getConfig();

        FileConfiguration file = config.getConfig();
        if (!file.contains("GUIS") || file.getConfigurationSection("GUIS").getKeys(false).isEmpty()) {
            return;
        }

        String guiName = file.getString("GUIS.GUI_NAME");
        if (guiName != null) {
            guiNameConfig = guiName;
        }

        // Close Item
        closeItemConfig = getNormalConfig(file, "GUIS.CLOSE_ITEM");

        backItemConfig = getNormalConfig(file, "GUIS.BACK_ITEM");

    }
    private static void LoadChestConfiguration() {
        MoConfig config = getConfig();
        
        FileConfiguration file = config.getConfig();
        
        if (!file.contains("GUIS.CHEST_MENU_CONFIGURATION") || file.getConfigurationSection("GUIS.CHEST_MENU_CONFIGURATION").getKeys(false).isEmpty()) {
            return;
        }

        // Selected Item Config
        List<String> selectedPurchasingLore = file.getStringList("GUIS.CHEST_MENU_CONFIGURATION.SELECTED_SLOT_ITEM.PURCHASING.LORE");
        List<String> selectedSellingLore = file.getStringList("GUIS.CHEST_MENU_CONFIGURATION.SELECTED_SLOT_ITEM.SELLING.LORE");
        ChestSelectedItemConfig selected = new ChestSelectedItemConfig(selectedPurchasingLore, selectedSellingLore);

        // Unlocked Slot Item
        NormalItemConfig unLockedSlotItem = getNormalConfig(file, "GUIS.CHEST_MENU_CONFIGURATION.UNLOCKED_SLOT_ITEM");

        // Locked Slot Item
        NormalItemConfig lockedSlotItem = getNormalConfig(file, "GUIS.CHEST_MENU_CONFIGURATION.LOCKED_SLOT_ITEM");

        // Trade Switch Item
        String tradeMaterial = file.getString("GUIS.CHEST_MENU_CONFIGURATION.TRADE_SWITCH_ITEM.MATERIAL");
        Byte tradeData = (byte) file.getLong("GUIS.CHEST_MENU_CONFIGURATION.TRADE_SWITCH_ITEM.DATA");
        String tradeName = file.getString("GUIS.CHEST_MENU_CONFIGURATION.TRADE_SWITCH_ITEM.NAME");
        String tradeSwitchMessage = file.getString("GUIS.CHEST_MENU_CONFIGURATION.TRADE_SWITCH_ITEM.SWITCH_MESSAGE");
        TradeSwitchItemConfig tradeSwitchItem = new TradeSwitchItemConfig(tradeMaterial, tradeData, tradeName, tradeSwitchMessage);

        // Economy Switch Item
        String economyMaterial = file.getString("GUIS.CHEST_MENU_CONFIGURATION.ECONOMY_SWITCH_ITEM.MATERIAL");
        Byte economyData = (byte) file.getLong("GUIS.CHEST_MENU_CONFIGURATION.ECONOMY_SWITCH_ITEM.DATA");
        String economyName = file.getString("GUIS.CHEST_MENU_CONFIGURATION.ECONOMY_SWITCH_ITEM.NAME");
        String economySwitchMessage = file.getString("GUIS.CHEST_MENU_CONFIGURATION.ECONOMY_SWITCH_ITEM.SWITCH_MESSAGE");
        EconomySwitchItemConfig  economySwitchItem = new EconomySwitchItemConfig(economyMaterial, economyData, economyName, economySwitchMessage);

        // Chest Status Switch Item
        String chestStatusMaterial = file.getString("GUIS.CHEST_MENU_CONFIGURATION.CHEST_STATUS_SWITCH_ITEM.MATERIAL");
        Byte chestStatusData = (byte) file.getLong("GUIS.CHEST_MENU_CONFIGURATION.CHEST_STATUS_SWITCH_ITEM.DATA");
        String chestStatusName = file.getString("GUIS.CHEST_MENU_CONFIGURATION.CHEST_STATUS_SWITCH_ITEM.NAME");
        String chestStatusSwitchMessage = file.getString("GUIS.CHEST_MENU_CONFIGURATION.CHEST_STATUS_SWITCH_ITEM.SWITCH_MESSAGE");
        ChestStatusSwitchItemConfig chestStatusSwitchItem = new ChestStatusSwitchItemConfig(chestStatusMaterial, chestStatusData, chestStatusName, chestStatusSwitchMessage);

        selectedItemConfig = selected;
        unLockedItemConfig = unLockedSlotItem;
        lockedItemConfig = lockedSlotItem;
        tradeItemConfig = tradeSwitchItem;
        economyItemConfig = economySwitchItem;
        chestStatusItemConfig = chestStatusSwitchItem;
    }

    private static NormalItemConfig purchasingRemoveAmountItemConfig = new NormalItemConfig();
    public static NormalItemConfig getPurchasingRemoveAmountItemConfig() { return purchasingRemoveAmountItemConfig; }
    private static NormalItemConfig assignPurchasePriceItemConfig = new NormalItemConfig();
    public static NormalItemConfig getAssignPurchasePriceItemConfig() { return assignPurchasePriceItemConfig; }

    private static void LoadPurchasingConfiguration() {
        MoConfig config = getConfig();

        FileConfiguration file = config.getConfig();

        if (!file.contains("GUIS.PURCHASING_MENU_CONFIGURATION") || file.getConfigurationSection("GUIS.PURCHASING_MENU_CONFIGURATION").getKeys(false).isEmpty()) {
            return;
        }

        purchasingRemoveAmountItemConfig = getNormalConfig(file, "GUIS.PURCHASING_MENU_CONFIGURATION.REMOVE_AMOUNT_ITEM");
        assignPurchasePriceItemConfig = getNormalConfig(file, "GUIS.PURCHASING_MENU_CONFIGURATION.ASSIGN_PRICE");
    }

    private static NormalItemConfig sellingWithdrawItemConfig = new NormalItemConfig();
    public static NormalItemConfig getSellingWithdrawItemConfig() { return sellingWithdrawItemConfig; }
    private static NormalItemConfig assignSalePriceItemConfig = new NormalItemConfig();
    public static NormalItemConfig getAssignSalePriceItemConfig() { return assignSalePriceItemConfig; }
    private static NormalItemConfig itemsForSaleItemConfig = new NormalItemConfig();
    public static NormalItemConfig getItemsForSaleItemConfig() { return itemsForSaleItemConfig; }

    private static void LoadSellingConfiguration() {
        MoConfig config = getConfig();

        FileConfiguration file = config.getConfig();

        if (!file.contains("GUIS.SELLING_MENU_CONFIGURATION") || file.getConfigurationSection("GUIS.SELLING_MENU_CONFIGURATION").getKeys(false).isEmpty()) {
            return;
        }

        sellingWithdrawItemConfig = getNormalConfig(file, "GUIS.SELLING_MENU_CONFIGURATION.REMOVE_AMOUNT_ITEM");
        assignSalePriceItemConfig = getNormalConfig(file, "GUIS.SELLING_MENU_CONFIGURATION.ASSIGN_PRICE");
        itemsForSaleItemConfig = getNormalConfig(file, "GUIS.SELLING_MENU_CONFIGURATION.ITEMS_FOR_SALE");
    }


    private static ChestSelectedItemConfig selectedItemClientConfig = new ChestSelectedItemConfig();
    public static ChestSelectedItemConfig getSelectedItemClientConfig( ) { return selectedItemClientConfig; }

    private static NormalItemConfig noItemInTradeConfig = new NormalItemConfig();
    public static NormalItemConfig getNoItemInTradeConfig() { return noItemInTradeConfig; }

    private static void LoadChestMenuClientConfig() {
        MoConfig config = getConfig();

        FileConfiguration file = config.getConfig();

        if (!file.contains("GUIS.CHEST_MENU_CLIENT") || file.getConfigurationSection("GUIS.CHEST_MENU_CLIENT").getKeys(false).isEmpty()) {
            return;
        }

        // Purchasing and Selling
        List<String> selectedPurchasingLore = file.getStringList("GUIS.CHEST_MENU_CLIENT.PURCHASING.LORE");
        List<String> selectedSellingLore = file.getStringList("GUIS.CHEST_MENU_CLIENT.SELLING.LORE");
        selectedItemClientConfig = new ChestSelectedItemConfig(selectedPurchasingLore, selectedSellingLore);

        noItemInTradeConfig = getNormalConfig(file, "GUIS.CHEST_MENU_CLIENT.NO_ITEM_IN_TRADE");
    }

    private static NormalItemConfig slotTokenConfig = new NormalItemConfig();
    public static NormalItemConfig getSlotTokenConfig() { return slotTokenConfig; }
    private static void LoadSlotTokenConfig() {
        MoConfig config = getConfig();

        FileConfiguration file = config.getConfig();

        if (!file.contains("SLOT_TOKEN") || file.getConfigurationSection("SLOT_TOKEN").getKeys(false).isEmpty()) {
            return;
        }

        slotTokenConfig = getNormalConfig(file, "SLOT_TOKEN");
    }


    private static NormalItemConfig getNormalConfig(FileConfiguration file, String path) {
        String itemMaterial = file.getString( path+".MATERIAL");
        String itemName = file.getString(path+".NAME");
        List<String> itemLore = file.getStringList(path+".LORE");
        Boolean itemUnbreakable = file.getBoolean(path+".UNBREAKABLE");
        Boolean itemUnique = file.getBoolean(path+".UNIQUE");
        List<String> itemEnchantments = file.getStringList(path+".ENCHANTMENTS");
        List<String> itemFlags = file.getStringList(path+".FLAGS");

        ItemInfo info = new ItemInfo(itemMaterial, itemName, itemLore,
                new Enchantments(itemEnchantments), itemFlags, itemUnbreakable, itemUnique);
        return new NormalItemConfig(info);
    }
}
