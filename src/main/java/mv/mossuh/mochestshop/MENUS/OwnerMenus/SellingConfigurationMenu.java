package mv.mossuh.mochestshop.MENUS.OwnerMenus;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.MENUS.GUIItems.OwnerItems;
import mv.mossuh.mochestshop.MENUS.ItemStacks.OwnerStacks;
import mv.mossuh.mochestshop.MoChestShop;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.SellingInfo;
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
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SellingConfigurationMenu {
    private static final MoChestShop instance = MoChestShop.getInstance();

    public static void open(UUID uuid, ActiveChestShop active, Slot slot, SingleGUI previousGUI) {
        ChestShop chest = active.getChest();
        UUID ownerUUID = chest.getOwner();
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        Location location = previousGUI.getLocation();

        OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUUID);

        String guiName = UtilString.get(Configs.getGuiNameConfig()).setVariables(chest).setVariables(owner).setPlaceholders(ownerUUID).hex().apply();
        SingleGUI gui = new SingleGUI(3, guiName, location);
        gui.onGUIClick((e) -> {
            e.setCancelled(true);
        });

        GUIItem gray = OwnerItems.GrayPanelItem();
        gui.setItem(gray, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 19, 20, 22 ,24, 25, 26);

        GUIItem collect1 = WithdrawCurrencyItem(player, chest, gui, slot);
        gui.setItem(collect1, 11);
        GUIItem collect16 = WithdrawCurrencyItem(player, chest, gui, slot);
        gui.setItem(collect16, 12);
        GUIItem collect32 = WithdrawCurrencyItem(player, chest, gui, slot);
        gui.setItem(collect32, 13);
        GUIItem collect64 = WithdrawCurrencyItem(player, chest, gui, slot);
        gui.setItem(collect64, 14);

        GUIItem collectALL = WithdrawCurrencyItem(player, chest, gui, slot);
        gui.setItem(collectALL, 15);

        GUIItem backItem = OwnerItems.BackItem(player, active);
        gui.setItem(backItem, 18);

        GUIItem assignPrice = AssignPrice(chest, gui, slot);
        gui.setItem(assignPrice, 21);

        GUIItem itemsForSale = ItemsForSale(chest, gui, slot);
        gui.setItem(itemsForSale, 23);

        player.openInventory(gui.getInventory());
    }

    private static GUIItem ItemsForSale(ChestShop chest, SingleGUI gui, Slot activeSlot) {
        ItemStack stack = OwnerStacks.getItemsForSale(chest, activeSlot);
        TradeInfo tradeInfo = activeSlot.getInfo();

        if (!tradeInfo.getType().equals(TradeType.SELL)) {
            return new GUIItem(stack);
        }
        SellingInfo info = (SellingInfo) tradeInfo;

        ItemStack saleItem = activeSlot.getItem();

        List<String> forSaleQuantity = new ArrayList<>();
        String forSaleQuantityWrong = UtilString.get(Messages.ASSIGN_QUANTITY_WRONG).setVariables(chest).setVariables(activeSlot).hex().apply();
        for (String l : Messages.ASSIGN_QUANTITY) {
            forSaleQuantity.add(UtilString.get(l).setVariables(chest).setVariables(activeSlot).hex().apply());
        }
        ConfirmConfiguration config = new ConfirmConfiguration(forSaleQuantity, null, null, forSaleQuantityWrong);
        GUIItem item = new GUIItem(stack);
        item.setConfirmConfiguration(config);
        item.onConfirm((player, clickType, value) -> {
            int quantity = (int) value;

            if (quantity <= 0) {
                UtilString.get(Messages.INVALID_QUANTITY).setVariables(chest).setVariables(activeSlot)
                        .hex().sendMessage(player);
                return;
            }

            List<VariableArg> variables = new ArrayList<>();
            variables.add(new VariableArg("%quantity%", quantity+""));

            switch (clickType) {
                case LEFT:
                case SHIFT_LEFT:
                    // Add stock
                    boolean hasItems = UtilMethods.hasItem(player, saleItem, quantity);
                    if (!hasItems) {
                        UtilString.get(Messages.SELLING_ITEMS_ADD_STOCK_NO_ENOUGH).setVariables(chest).setVariables(activeSlot)
                                .setVariables(variables).hex().sendMessage(player);
                        return;
                    }

                    UtilMethods.removeItem(player, saleItem, quantity);
                    info.addItemsForSale(quantity);

                    item.setItemStack(OwnerStacks.getItemsForSale(chest, activeSlot));
                    Bukkit.getScheduler().runTask(instance, () -> {
                            player.openInventory(gui.getInventory());
                            gui.refreshGUI(player);
                    });
                    UtilString.get(Messages.SELLING_ITEMS_ADDED_STOCK).setVariables(chest).setVariables(activeSlot)
                            .setVariables(variables).hex().sendMessage(player);
                    break;
                case RIGHT:
                case SHIFT_RIGHT:
                    // Withdraw stock
                    int quantitySale = info.getItemsForSale();

                    if (quantitySale < quantity) {
                        UtilString.get(Messages.SELLING_ITEMS_WITHDRAW_STOCK_NO_ENOUGH).setVariables(chest).setVariables(activeSlot)
                                .setVariables(variables).hex().sendMessage(player);
                        return;
                    }

                    boolean hasSpace = UtilMethods.hasEnoughSpace(player, saleItem, quantity);
                    if (!hasSpace) {
                        UtilString.get(Messages.SELLING_ITEMS_WITHDRAW_NO_ENOUGH_SPACE).setVariables(chest).setVariables(activeSlot)
                                .setVariables(variables).hex().sendMessage(player);
                        return;
                    }

                    info.removeItemsForSale(quantity);
                    UtilMethods.giveItem(player, saleItem, quantity, player.getLocation());
                    UtilString.get(Messages.SELLING_ITEMS_WITHDRAWN_STOCK).setVariables(chest).setVariables(activeSlot)
                            .setVariables(variables).hex().sendMessage(player);

                    item.setItemStack(OwnerStacks.getItemsForSale(chest, activeSlot));
                    Bukkit.getScheduler().runTask(instance, () -> {
                        player.openInventory(gui.getInventory());
                        gui.refreshGUI(player);
                    });
                    break;

            }
        }, ResponseType.INTEGER);

        return item;
    }

    private static GUIItem AssignPrice(ChestShop chest, SingleGUI gui, Slot activeSlot) {
        ItemStack stack = OwnerStacks.getSellAssignPrice(chest, activeSlot);
        TradeInfo tradeInfo = activeSlot.getInfo();

        GUIItem item = new GUIItem(stack);
        List<String> assignPrice = new ArrayList<>();
        String assignPriceWrong = UtilString.get(Messages.ASSIGN_PRICE_WRONG).setVariables(activeSlot).setVariables(chest).hex().apply();
        for (String l : Messages.ASSIGN_PRICE) {
            assignPrice.add(UtilString.get(l).setVariables(activeSlot).setVariables(chest).hex().apply());
        }

        ConfirmConfiguration confirmConfig = new ConfirmConfiguration(assignPrice, null, null, assignPriceWrong);
        item.setConfirmConfiguration(confirmConfig);
        item.onConfirm((player, clickType, value) -> {
            double newPrice = (double) value;

            if (newPrice < 0) {
                UtilString.get(Messages.INVALID_PRICE).setVariables(chest).setVariables(activeSlot)
                        .hex().sendMessage(player);
                return;
            }

            SellingInfo info = (SellingInfo) tradeInfo;
            if (info.getSoldItems() > 0) {
                UtilString.get(Messages.COLLECT_COINS_TO_CHANGE).setVariables(chest).setVariables(activeSlot)
                        .hex().sendMessage(player);
                return;
            }


            info.setSellingPrice(newPrice);

            double refreshedPrice = info.getSellingPrice();
            List<VariableArg> refreshedVariables = new ArrayList<>();
            refreshedVariables.add(new VariableArg("%price%", UsefulMethods.formatNumber(refreshedPrice, Config.MAX_DECIMAL)));

            UtilString.get(Messages.ASSIGNED_PRICE).setVariables(refreshedVariables).setVariables(chest).setVariables(activeSlot).hex().sendMessage(player);

            item.setItemStack(OwnerStacks.getSellAssignPrice(chest, activeSlot));
            Bukkit.getScheduler().runTask(instance, () -> {
                player.openInventory(gui.getInventory());
                gui.refreshGUI(player);
            });
        }, ResponseType.DOUBLE);
        return item;
    }

    private static GUIItem WithdrawCurrencyItem(Player player, ChestShop chest, SingleGUI gui, Slot activeSlot) {
        UUID ownerUUID = chest.getOwner();
        ItemStack stack = OwnerStacks.getWithdrawCurrency(chest, activeSlot);
        String id = chest.getSelectedEconomy();

        TradeInfo tradeInfo = activeSlot.getInfo();
        if (tradeInfo.getType() != TradeType.SELL) { return new GUIItem(new ItemStack(Material.AIR)); }
        SellingInfo info = (SellingInfo) tradeInfo;

        GUIItem item = new GUIItem(stack);
        item.onItemClick((e) -> {
            UUID uuid = player.getUniqueId();
            int newAvailable = info.getSoldItems();
            int newAmount = 0;

            if (newAvailable == 0) {
                UtilString.get(Messages.SLOT_NO_COINS_COLLECT).setVariables(activeSlot).setVariables(player).setVariables(chest)
                        .setPlaceholders(ownerUUID).hex().sendMessage(player);
                player.closeInventory();
                return;
            }
            UtilString.get(Messages.SLOT_COLLECT_COINS).setVariables(player)
                    .setVariables(activeSlot).setVariables(chest).setPlaceholders(ownerUUID).hex().sendMessage(player);


            double price = newAvailable * info.getSellingPrice();
            info.setSoldItems(newAmount);
            ChestShopAPI.getManagers().getEconomies().getEconomy(id).addAmount(uuid, price);

            item.setItemStack(OwnerStacks.getWithdrawCurrency(chest, activeSlot));
            Bukkit.getScheduler().runTask(instance, () -> {
                player.openInventory(gui.getInventory());
                gui.refreshGUI(player);
            });
        });
        return item;
    }
}
