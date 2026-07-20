package mv.mossuh.mochestshop.MENUS.OwnerMenus;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.MENUS.GUIItems.OwnerItems;
import mv.mossuh.mochestshop.MENUS.ItemStacks.OwnerStacks;
import mv.mossuh.mochestshop.MoChestShop;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.PurchasingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.TradeInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.ENUMS.ResponseType;
import mv.mossuh.mocore.GUI.Holders.SingleGUI;
import mv.mossuh.mocore.GUI.Interfaces.Utilities.ConfirmConfiguration;
import mv.mossuh.mocore.GUI.Items.GUIItem;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PurchasingConfigurationMenu {
    private static final MoChestShop instance = MoChestShop.getInstance();

    public static void open(UUID uuid, ActiveChestShop active, Slot slot, SingleGUI previousGUI) {
        ChestShop chest = active.getChest();
        UUID ownerUUID = chest.getOwner();
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        Location location = previousGUI.getLocation();

        OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUUID);

        String guiName = UtilString.get(Configs.getGuiNameConfig()).setPlaceholders(ownerUUID).setDefaultPlayerVariables(owner)
                .setChestShop(chest).hex().apply();
        SingleGUI gui = new SingleGUI(3, guiName, location);
        gui.onGUIClick((e) -> {
            e.setCancelled(true);
        });

        GUIItem gray = OwnerItems.GrayPanelItem();
        gui.setItem(gray, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 19, 20, 21, 23 ,24, 25, 26);

        GUIItem collect1 = RemoveAmountItem(1, player, chest, gui, slot);
        gui.setItem(11, collect1);
        GUIItem collect16 = RemoveAmountItem(16, player, chest, gui, slot);
        gui.setItem(12, collect16);
        GUIItem collect32 = RemoveAmountItem(32, player, chest, gui, slot);
        gui.setItem(13, collect32);
        GUIItem collect64 = RemoveAmountItem(64, player, chest, gui, slot);
        gui.setItem(14, collect64);

        PurchasingInfo info = (PurchasingInfo) slot.getInfo();
        int available = info.getPurchasedItems();
        GUIItem collectALL = RemoveAmountItem(available, player, chest, gui, slot);
        gui.setItem(15, collectALL);

        GUIItem backItem = OwnerItems.BackItem(player, active);
        gui.setItem(18, backItem);

        GUIItem assignPrice = AssignPrice(chest, gui, slot);
        gui.setItem(22, assignPrice);

        player.openInventory(gui.getInventory());
    }

    private static GUIItem RemoveAmountItem(int amount, Player player, ChestShop chest, SingleGUI gui, Slot activeSlot) {
        UUID ownerUUID = chest.getOwner();
        ItemStack stack = OwnerStacks.getPurchasingRemoveAmount(chest, activeSlot, amount);
        TradeInfo tradeInfo = activeSlot.getInfo();

        if (tradeInfo.getType() != TradeType.BUY) { return new GUIItem(stack); }
        PurchasingInfo info = (PurchasingInfo) tradeInfo;

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%amount%", amount+""));

        ItemStack purchasedItem = activeSlot.getItem();
        GUIItem item = new GUIItem(stack);
        item.onItemClick((e) -> {
            int newAvailable = info.getPurchasedItems();
            int newAmount = newAvailable - amount;
            List<VariableArg> collectedVariables = new ArrayList<>(UtilMethods.getSlotVariables(info));
            collectedVariables.add(new VariableArg("%amount%", amount+""));

            if (newAvailable == 0) {
                UtilString.get(Messages.SLOT_NO_ITEMS_COLLECT).setVariables(variables).setDefaultPlayerVariables(player)
                        .setPlaceholders(ownerUUID).setChestShop(chest).setSlot(activeSlot).hex().sendMessage(player);
                player.closeInventory();
                return;
            }
            if (newAmount < 0) {
                UtilString.get(Messages.SLOT_NO_ITEMS_COLLECT).setVariables(variables).setDefaultPlayerVariables(player)
                        .setPlaceholders(ownerUUID).setChestShop(chest).setSlot(activeSlot).hex().sendMessage(player);
                player.closeInventory();
                return;
            }
            if (!UtilMethods.hasEnoughSpace(player, purchasedItem, amount)) {
                UtilString.get(Messages.COLLECT_ITEMS_NOT_ENOUGH_SPACE).setVariables(variables).setDefaultPlayerVariables(player)
                        .setPlaceholders(ownerUUID).setChestShop(chest).setSlot(activeSlot).hex().sendMessage(player);
                player.closeInventory();
                return;
            }
            UtilMethods.giveItem(player, purchasedItem, amount, player.getLocation());
            UtilString.get(Messages.SLOT_COLLECT_ITEMS).setDefaultPlayerVariables(player)
                    .setVariables(collectedVariables).setPlaceholders(ownerUUID).setChestShop(chest).setSlot(activeSlot).hex().sendMessage(player);

            info.setPurchasedItems(newAmount);
            item.setItemStack(OwnerStacks.getPurchasingRemoveAmount(chest, activeSlot, amount));
            Bukkit.getScheduler().runTask(instance, () -> {
                player.openInventory(gui.getInventory());
                gui.refreshGUI(player);
            });
        });
        return item;
    }

    private static GUIItem AssignPrice(ChestShop chest, SingleGUI gui, Slot activeSlot) {
        ItemStack stack = OwnerStacks.getPurchasingAssignPrice(chest, activeSlot);

        TradeInfo tradeInfo = activeSlot.getInfo();

        if (tradeInfo.getType() != TradeType.BUY) { return new GUIItem(stack); }
        PurchasingInfo info = (PurchasingInfo) tradeInfo;
        double price = info.getPurchasingPrice();


        List<VariableArg> variables = new ArrayList<>(UtilMethods.getSlotVariables(info));
        variables.add(new VariableArg("%price%", UsefulMethods.formatNumber(price, Config.MAX_DECIMAL)));

        GUIItem item = new GUIItem(stack);
        List<String> assignPrice = new ArrayList<>();
        String assignPriceWrong = UtilString.get(Messages.ASSIGN_PRICE_WRONG).setVariables(variables).setChestShop(chest).setSlot(activeSlot).hex().apply();
        for (String l : Messages.ASSIGN_PRICE) {
            assignPrice.add(UtilString.get(l).setVariables(variables).setChestShop(chest).setSlot(activeSlot).hex().apply());
        }

        ConfirmConfiguration confirmConfig = new ConfirmConfiguration(assignPrice, null, null, assignPriceWrong);
        item.setConfirmConfiguration(confirmConfig);
        item.onConfirm((player, clickType, value) -> {
            double newPrice = (double) value;

            if (newPrice < 0) {
                UtilString.get(Messages.INVALID_PRICE).setChestShop(chest).setSlot(activeSlot)
                        .hex().sendMessage(player);
                return;
            }



            PurchasingInfo refreshedInfo = (PurchasingInfo) tradeInfo;
            refreshedInfo.setPurchasingPrice(newPrice);

            double refreshedPrice = refreshedInfo.getPurchasingPrice();
            List<VariableArg> refreshedVariables = new ArrayList<>(UtilMethods.getSlotVariables(refreshedInfo));
            refreshedVariables.add(new VariableArg("%price%", UsefulMethods.formatNumber(refreshedPrice, Config.MAX_DECIMAL)));
            UtilString.get(Messages.ASSIGNED_PRICE).setVariables(refreshedVariables).setChestShop(chest).setSlot(activeSlot).hex().sendMessage(player);
            item.setItemStack(OwnerStacks.getPurchasingAssignPrice(chest, activeSlot));
            Bukkit.getScheduler().runTask(instance, () -> {
                player.openInventory(gui.getInventory());
                gui.refreshGUI(player);
            });

        }, ResponseType.DOUBLE);
        return item;
    }
}
