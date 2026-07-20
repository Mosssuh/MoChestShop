package mv.mossuh.mochestshop.MENUS.GUIItems;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import mv.mossuh.mochestshop.API.Events.PlayerTradeEvent;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.MENUS.ItemStacks.PlayerStacks;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.PurchasingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.SellingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.ENUMS.ResponseType;
import mv.mossuh.mocore.GUI.Holders.SingleGUI;
import mv.mossuh.mocore.GUI.Interfaces.Utilities.ConfirmConfiguration;
import mv.mossuh.mocore.GUI.Items.GUIItem;
import mv.mossuh.mocore.ITEM.MoItemStack;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerItems {

    public static GUIItem SlotItem(SingleGUI gui, ActiveChestShop active, Slot activeSlot) {
        ChestShop chest = active.getChest();
        String id = chest.getSelectedEconomy();
        UUID ownerUUID = chest.getOwner();
        ItemStack tradedItem = activeSlot.getItem();

        ItemStack clone = tradedItem.clone();
        MoItemStack editor = new MoItemStack(clone);
        TradeType type = activeSlot.getInfo().getType();

        switch (type) {
            case BUY:
                List<String> pLore = Configs.getSelectedItemClientConfig().getPurchasingLore();
                for (String l : pLore) {
                    editor.addLoreLine(UtilString.get(l).setSlot(activeSlot).setChestShop(chest)
                            .setPlaceholders(ownerUUID).hex().apply());
                }
                break;
            case SELL:
                List<String> sLore = Configs.getSelectedItemClientConfig().getSellingLore();
                for (String l : sLore) {
                    editor.addLoreLine(UtilString.get(l).setSlot(activeSlot).setChestShop(chest)
                            .setPlaceholders(ownerUUID).hex().apply());
                }
                break;
        }

        List<String> assignTradeQuantity = new ArrayList<>();
        String assignTradeQuantityWrong = UtilString.get(Messages.ASSIGN_TRADE_QUANTITY_WRONG).setSlot(activeSlot).setChestShop(chest).hex().apply();
        for (String l : Messages.ASSIGN_TRADE_QUANTITY) {
            assignTradeQuantity.add(UtilString.get(l).setSlot(activeSlot).setChestShop(chest).hex().apply());
        }

        ConfirmConfiguration confirmConfig = new ConfirmConfiguration(assignTradeQuantity, null, null, assignTradeQuantityWrong);
        GUIItem item = new GUIItem(clone);
        item.setConfirmConfiguration(confirmConfig);
        item.onConfirm((player, clickType, value) -> {
            int amount = (int) value;

            if (amount <= 0) {
                UtilString.get(Messages.INVALID_QUANTITY).setChestShop(chest).setSlot(activeSlot)
                        .hex().sendMessage(player);
                return;
            }

            UUID uuid = player.getUniqueId();
            String economyID = chest.getSelectedEconomy();
            List<VariableArg> refreshedVariables = new ArrayList<>();

            if (type.equals(TradeType.BUY)) {
                // The player is selling his items
                PurchasingInfo info = (PurchasingInfo) activeSlot.getInfo();
                double cost = info.getPurchasingPrice();

                if (!UtilMethods.hasItem(player, tradedItem, amount)) {
                    UtilString.get(Messages.PURCHASING_CLIENT_NO_ENOUGH_ITEMS).setChestShop(chest).setSlot(activeSlot)
                            .setPlaceholders(ownerUUID).setItemStack(tradedItem).hex().sendMessage(player);
                    player.closeInventory();
                    return;
                }

                if (!ChestShopAPI.getManagers().getEconomies().getEconomy(economyID).hasAmount(ownerUUID, cost)) {
                    UtilString.get(Messages.PURCHASING_OWNER_NO_ENOUGH_COINS).setChestShop(chest).setSlot(activeSlot)
                            .setPlaceholders(ownerUUID).setItemStack(tradedItem).hex().sendMessage(player);
                    player.closeInventory();
                    return;
                }

                PlayerTradeEvent tradeEvent = new PlayerTradeEvent(active, player, type, activeSlot, amount);
                Bukkit.getPluginManager().callEvent(tradeEvent);

                if (tradeEvent.isCancelled()) return;

                refreshedVariables.add(new VariableArg("%amount%", amount+""));

                info.addPurchasedItems(amount);
                UtilMethods.removeItem(player, tradedItem, amount);
                UtilString.get(Messages.PURCHASING_OWNER_PURCHASED_ITEMS).setVariables(refreshedVariables)
                        .setChestShop(chest).setSlot(activeSlot).setDefaultPlayerVariables(player).setPlaceholders(ownerUUID).setItemStack(tradedItem).hex().sendMessage(ownerUUID);
                UtilString.get(Messages.PURCHASING_CLIENT_SOLD_ITEMS).setVariables(refreshedVariables)
                        .setChestShop(chest).setSlot(activeSlot).setDefaultPlayerVariables(player).setPlaceholders(ownerUUID).setItemStack(tradedItem).hex().sendMessage(player);
                player.closeInventory();
            } else if (type.equals(TradeType.SELL)) {
                // The player is purchasing items
                SellingInfo info = (SellingInfo) activeSlot.getInfo();
                double cost = info.getSellingPrice() * amount;

                if (info.getItemsForSale() < amount) {
                    UtilString.get(Messages.SELLING_OWNER_NO_ENOUGH_ITEMS).setChestShop(chest).setSlot(activeSlot)
                            .setPlaceholders(ownerUUID).setDefaultPlayerVariables(player).setItemStack(tradedItem).hex().sendMessage(player);
                    player.closeInventory();
                    return;
                }

                if (!ChestShopAPI.getManagers().getEconomies().getEconomy(economyID).hasAmount(uuid, cost)) {
                    UtilString.get(Messages.SELLING_CLIENT_NO_ENOUGH_COINS).setChestShop(chest).setSlot(activeSlot)
                            .setPlaceholders(ownerUUID).setDefaultPlayerVariables(player).setItemStack(tradedItem).hex().sendMessage(player);
                    player.closeInventory();
                    return;
                }

                if (!UtilMethods.hasEnoughSpace(player, tradedItem, amount)) {
                    UtilString.get(Messages.SELLING_CLIENT_NO_ENOUGH_SPACE).setChestShop(chest).setSlot(activeSlot)
                            .setPlaceholders(ownerUUID).setDefaultPlayerVariables(player).setItemStack(tradedItem).hex().sendMessage(player);
                    player.closeInventory();
                    return;
                }

                PlayerTradeEvent tradeEvent = new PlayerTradeEvent(active, player, type, activeSlot, amount);
                Bukkit.getPluginManager().callEvent(tradeEvent);

                if (tradeEvent.isCancelled()) return;

                refreshedVariables.add(new VariableArg("%amount%", amount+""));
                info.removeItemsForSale(amount);
                info.addSoldItems(amount);
                UtilMethods.giveItem(player, tradedItem, amount, player.getLocation());
                ChestShopAPI.getManagers().getEconomies().getEconomy(id).removeAmount(uuid, cost);

                UtilString.get(Messages.SELLING_OWNER_SOLD_ITEMS).setVariables(refreshedVariables)
                        .setChestShop(chest).setSlot(activeSlot).setDefaultPlayerVariables(player).setPlaceholders(ownerUUID)
                        .setItemStack(tradedItem).hex().sendMessage(ownerUUID);
                UtilString.get(Messages.SELLING_CLIENT_PURCHASED_ITEMS).setVariables(refreshedVariables)
                        .setChestShop(chest).setSlot(activeSlot).setDefaultPlayerVariables(player).setPlaceholders(ownerUUID)
                        .setItemStack(tradedItem).hex().sendMessage(player);
                player.closeInventory();
            }
        }, ResponseType.INTEGER);
        return item;
    }

    public static GUIItem NoItemInTradeItem() {
        ItemStack stack = PlayerStacks.NoItemInTradeItem();
        return new GUIItem(stack);
    }

}
