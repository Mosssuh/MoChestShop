package mv.mossuh.mochestshop.UTILITIES;

import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestShopConfig;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.PurchasingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.SellingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.TradeInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.Economies.Economy;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultVariables {
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
}
