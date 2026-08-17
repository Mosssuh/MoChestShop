package mv.mossuh.mochestshop.MENUS.GUIItems;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import mv.mossuh.mochestshop.API.Events.PlayerSwitchEconomyEvent;
import mv.mossuh.mochestshop.API.Events.PlayerSwitchStatusEvent;
import mv.mossuh.mochestshop.API.Events.PlayerSwitchTradeEvent;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig.ChestStatusSwitchItemConfig;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig.EconomySwitchItemConfig;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig.TradeSwitchItemConfig;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.MENUS.ItemStacks.OwnerStacks;
import mv.mossuh.mochestshop.MENUS.OwnerMenus.ChestConfigurationMenu;
import mv.mossuh.mochestshop.MENUS.OwnerMenus.PurchasingConfigurationMenu;
import mv.mossuh.mochestshop.MENUS.OwnerMenus.SellingConfigurationMenu;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.PurchasingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.SellingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.TradeInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.DefaultVariables;
import mv.mossuh.mochestshop.UTILITIES.Economies.Economy;
import mv.mossuh.mochestshop.UTILITIES.Enums.ChestStatus;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.GUI.Holders.SingleGUI;
import mv.mossuh.mocore.GUI.Items.GUIItem;
import mv.mossuh.mocore.ITEM.MoItemStack;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OwnerItems {
    public static void updateSlots(SingleGUI gui, ActiveChestShop active) {
        ChestShop chest = active.getChest();
        int[] slotsPosition = UtilMethods.getChestInvPositions();
        int unlockedSlots = chest.getUnlockedSlots();

        int slotPos = 1;
        for (int pos : slotsPosition) {
            Slot slot = chest.getSlot(slotPos);
            if (slot.isValid()) {
                if (slot.hasItem()) {
                    GUIItem item = OwnerItems.SelectedSlotItem(gui, active, slot);
                    gui.setItem(item, pos);
                } else {
                    GUIItem item;
                    if (slotPos <= unlockedSlots) {
                        item = OwnerItems.UnlockedSlotItem(slotPos);
                    } else {
                        item = OwnerItems.LockedSlotItem(slotPos);
                    }
                    gui.setItem(item, pos);
                }
            } else {
                GUIItem item;
                if (slotPos <= unlockedSlots) {
                    item = OwnerItems.UnlockedSlotItem(slotPos);
                } else {
                    item = OwnerItems.LockedSlotItem(slotPos);
                }
                gui.setItem(item, pos);
            }
            slotPos += 1;
        }
    }

    public static GUIItem SelectedSlotItem(SingleGUI gui, ActiveChestShop active, Slot slot) {
        ChestShop chest = active.getChest();
        UUID ownerUUID = chest.getOwner();
        ItemStack tradedItem = slot.getItem();

        ItemStack clone = tradedItem.clone();
        MoItemStack editor = new MoItemStack(clone);
        TradeInfo info = slot.getInfo();
        TradeType type = info.getType();
        List<VariableArg> variables = new ArrayList<>(DefaultVariables.getSlotVariables(slot));
        switch (type) {
            case BUY:
                List<String> pLore = Configs.getSelectedItemConfig().getPurchasingLore();
                for (String l : pLore) {
                    editor.addLoreLine(UtilString.get(l).setVariables(variables).setVariables(chest)
                            .setPlaceholders(chest.getOwner()).hex().apply());
                }
                break;
            case SELL:
                List<String> sLore = Configs.getSelectedItemConfig().getSellingLore();
                for (String l : sLore) {
                    editor.addLoreLine(UtilString.get(l).setVariables(variables).setVariables(chest)
                            .setPlaceholders(chest.getOwner()).hex().apply());
                }
                break;
        }
        GUIItem item = new GUIItem(clone);
        item.onItemClick(e -> {
            Player player = e.getPlayer();
            UUID uuid = player.getUniqueId();
            ClickType click = e.getClickType();
            List<VariableArg> refreshedVariables = new ArrayList<>(DefaultVariables.getSlotVariables(slot));
            if (click.equals(ClickType.SHIFT_RIGHT)) {
                // To remove the item of the selected slot
                if (chest.getStatus().equals(ChestStatus.OPEN)) {
                    UtilString.get(Messages.CHEST_CLOSED_REQUIRED).setVariables(chest)
                            .hex().sendMessage(player);
                    player.closeInventory();
                    return;
                }

                if (!info.canSwitch()) {
                    switch (type) {
                        case BUY:
                            UtilString.get(Messages.COLLECT_ITEMS_TO_CHANGE).setVariables(chest)
                                    .hex().sendMessage(player);
                            player.closeInventory();
                            return;
                        case SELL:
                            SellingInfo si = (SellingInfo) info;
                            if (si.getSoldItems() > 0) {
                                UtilString.get(Messages.COLLECT_COINS_TO_CHANGE).setVariables(chest)
                                        .hex().sendMessage(player);
                            }
                            if (si.getItemsForSale() > 0) {
                                UtilString.get(Messages.COLLECT_ITEMS_TO_CHANGE).setVariables(chest)
                                        .hex().sendMessage(player);
                            }
                            player.closeInventory();
                            return;
                    }
                }

                int slotPosition = slot.getSlot();
                chest.removeSlot(slotPosition);
                UpdateSlotItem(player, slotPosition, chest.getUnlockedSlots(), gui);
                UtilString.get(Messages.CHEST_REMOVED_ITEM).setVariables(refreshedVariables).setVariables(chest)
                        .hex().sendMessage(player);
            } else if (click.equals(ClickType.LEFT)) {
                String COOLDOWN_CODE = Config.PLUGIN_NAME+"OwnerItems-SelectedSlotItem-Cooldown"+chest.getUUID();
                long COOLDOWN = 2;
                if (Cooldown.startAndIsOnCooldown(COOLDOWN_CODE, COOLDOWN)) {
                    List<VariableArg> cooldownVariables = new ArrayList<>();
                    cooldownVariables.add(new VariableArg("%cooldown%", Cooldown.showCooldownFormatted(COOLDOWN_CODE, COOLDOWN, UtilMethods.getTimeFormat())));
                    UtilString.get(Messages.COOLDOWN).setVariables(cooldownVariables)
                            .setVariables(chest).hex().sendMessage(player);
                    player.closeInventory();
                    return;
                }

                // Open the trade options (Sell or Purchase)
                if (type.equals(TradeType.BUY)) {
                    refreshedVariables.add(new VariableArg("%amount%", "1"));

                    PurchasingInfo i = (PurchasingInfo) slot.getInfo();
                    int purchasedItems = i.getPurchasedItems();
                    if (purchasedItems <= 0) {
                        UtilString.get(Messages.SLOT_NO_ITEMS_COLLECT).setVariables(chest).setVariables(refreshedVariables)
                                .hex().sendMessage(player);
                        player.closeInventory();
                        return;
                    }

                    if (!UtilMethods.hasEnoughSpace(player, tradedItem, 1)) {
                        UtilString.get(Messages.COLLECT_ITEMS_NOT_ENOUGH_SPACE).setVariables(refreshedVariables).setVariables(player).setVariables(chest)
                                .setPlaceholders(ownerUUID).hex().sendMessage(player);
                        player.closeInventory();
                        return;
                    }

                    UtilMethods.giveItem(player, tradedItem, 1, player.getLocation());
                    i.removePurchasedItems(1);

                    UtilString.get(Messages.SLOT_COLLECT_ITEMS).setVariables(refreshedVariables)
                            .setVariables(chest).hex().sendMessage(player);
                    player.closeInventory();

                } else if (type.equals(TradeType.SELL)) {
                    SellingInfo i = (SellingInfo) slot.getInfo();
                    int soldItems = i.getSoldItems();
                    if (soldItems <= 0) {
                        UtilString.get(Messages.SLOT_NO_COINS_COLLECT).setVariables(chest)
                                .setVariables(refreshedVariables).hex().sendMessage(player);
                        player.closeInventory();
                        return;
                    }

                    double price = soldItems * i.getSellingPrice();
                    UtilString.get(Messages.SLOT_COLLECT_COINS).setVariables(refreshedVariables)
                            .setVariables(chest).hex().sendMessage(player);

                    String id = chest.getSelectedEconomy();
                    i.setSoldItems(0);
                    ChestShopAPI.getManagers().getEconomies().getEconomy(id).addAmount(uuid, price);

                    player.closeInventory();
                }
            } else if (click.equals(ClickType.RIGHT)) {
                if (type.equals(TradeType.BUY)) {
                    PurchasingConfigurationMenu.open(player.getUniqueId(), active, slot, gui);
                } else if (type.equals(TradeType.SELL)) {
                    SellingConfigurationMenu.open(player.getUniqueId(), active, slot, gui);
                }
            }
        });
        return item;
    }

    public static void UpdateSlotItem(Player player, int slot, int unlockedSlots, SingleGUI gui) {
        int pos = UtilMethods.getInvPosition(slot);
        GUIItem item;
        if (slot <= unlockedSlots) {
            item = UnlockedSlotItem(slot);
        } else {
            item = LockedSlotItem(slot);
        }
        gui.setItem(item, pos);
        gui.refreshGUI(player);
    }


    public static GUIItem UnlockedSlotItem(int slot) {
        ItemStack stack = OwnerStacks.UnlockedSlotItem(slot);
        return new GUIItem(stack);
    }

    public static GUIItem LockedSlotItem(int slot) {
        ItemStack stack = OwnerStacks.LockedSlotItem(slot);
        return new GUIItem(stack);
    }

    public static @NotNull GUIItem TradeSwitch(Player player, SingleGUI gui, ActiveChestShop active) {
        ItemStack stack = OwnerStacks.TradeSwitch(active);
        ChestShop chest = active.getChest();

        TradeSwitchItemConfig config = Configs.getTradeItemConfig();
        String buyPrefix = UtilString.get(TradeType.BUY.getPrefix()).setVariables(chest).removeColors().apply();
        String sellPrefix = UtilString.get(TradeType.SELL.getPrefix()).setVariables(chest).removeColors().apply();
        String switchMessage = UtilString.get(config.getSwitchMessage()).setVariables(chest).apply();

        GUIItem item = new GUIItem(stack);
        item.onItemClick(e -> {

            String COOLDOWN_CODE = Config.PLUGIN_NAME+"OwnerItems-TradeSwitch-Cooldown"+chest.getUUID();
            long COOLDOWN = 2;
            if (Cooldown.startAndIsOnCooldown(COOLDOWN_CODE, COOLDOWN)) {
                List<VariableArg> cooldownVariables = new ArrayList<>();
                cooldownVariables.add(new VariableArg("%cooldown%", Cooldown.showCooldownFormatted(COOLDOWN_CODE, COOLDOWN, UtilMethods.getTimeFormat())));
                UtilString.get(Messages.COOLDOWN).setVariables(cooldownVariables)
                        .setVariables(chest).hex().sendMessage(player);
                player.closeInventory();
                return;
            }

            boolean canSwitch = true;
            boolean haveSoldItems = false;
            boolean haveStockItems = false;
            Iterator<Slot> slots = chest.getSlots().iterator();
            while (slots.hasNext()) {
                Slot slot = slots.next();
                if (!slot.getInfo().canSwitch()) {
                    canSwitch = false;
                    if (slot.getInfo().getType().equals(TradeType.SELL)) {
                        SellingInfo i = (SellingInfo) slot.getInfo();
                        if (i.getSoldItems() > 0) { haveSoldItems = true; }
                        if (i.getItemsForSale() > 0) { haveStockItems = true; }
                    }
                    break;
                }
            }

            if (!canSwitch) {
                switch (chest.getTradeType()) {
                    case BUY:
                        UtilString.get(Messages.COLLECT_ITEMS_TO_CHANGE).setVariables(chest)
                                .hex().sendMessage(player);
                        player.closeInventory();
                        break;
                    case SELL:
                        if (haveSoldItems) {
                            UtilString.get(Messages.COLLECT_COINS_TO_CHANGE).setVariables(chest)
                                    .hex().sendMessage(player);
                        }
                        if (haveStockItems) {
                            UtilString.get(Messages.COLLECT_ITEMS_TO_CHANGE).setVariables(chest)
                                    .hex().sendMessage(player);
                        }
                        player.closeInventory();
                        break;
                }
                return;
            }

            List<TradeType> types = TradeType.getTypes();
            // Recalled getTradeType() to get the last updated TradeType
            int index = types.indexOf(chest.getTradeType());
            TradeType next = types.get((index + 1) % types.size());

            PlayerSwitchTradeEvent switchTradeEvent = new PlayerSwitchTradeEvent(player, active, next);
            Bukkit.getServer().getPluginManager().callEvent(switchTradeEvent);
            if (switchTradeEvent.isCancelled()) return;

            chest.setTradeType(next);

            MoItemStack editor = item.getItemStackEditor();
            editor.setLore("&r",
                    (next == TradeType.SELL ? " &a➛ "+sellPrefix : "&7➛ "+sellPrefix),
                    (next == TradeType.BUY ? " &a➛ "+buyPrefix : "&7➛ "+buyPrefix),
                    "&r",
                    switchMessage);
            updateSlots(gui, active);
            gui.refreshGUI(player);
            Set<Player> playersInventory = UtilMethods.closeInventoryPlayers(active.getLocation(), player);
            for (Player p : playersInventory) {
                UtilString.get(Messages.MODIFIED_CHEST).setVariables(chest).hex().sendMessage(p);
            }
        });

        return item;
    }

    public static GUIItem EconomyItem(Player player, SingleGUI gui, ActiveChestShop active) {
        ItemStack stack = OwnerStacks.EconomyItem(active);
        ChestShop chest = active.getChest();
        EconomySwitchItemConfig config = Configs.getEconomyItemConfig();

        GUIItem item = new GUIItem(stack);
        item.onItemClick(e -> {
            String COOLDOWN_CODE = Config.PLUGIN_NAME+"OwnerItems-EconomySwitch-Cooldown"+chest.getUUID();
            long COOLDOWN = 2;
            if (Cooldown.startAndIsOnCooldown(COOLDOWN_CODE, COOLDOWN)) {
                List<VariableArg> cooldownVariables = new ArrayList<>();
                cooldownVariables.add(new VariableArg("%cooldown%", Cooldown.showCooldownFormatted(COOLDOWN_CODE, COOLDOWN, UtilMethods.getTimeFormat())));
                UtilString.get(Messages.COOLDOWN).setVariables(cooldownVariables)
                        .setVariables(chest).hex().sendMessage(player);
                player.closeInventory();
                return;
            }

            boolean canSwitch = true;
            boolean haveSoldItems = false;
            boolean haveStockItems = false;
            for (Slot slot : chest.getSlots()) {
                if (!slot.getInfo().canSwitch()) {
                    canSwitch = false;
                    if (slot.getInfo().getType().equals(TradeType.SELL)) {
                        SellingInfo i = (SellingInfo) slot.getInfo();
                        if (i.getSoldItems() > 0) { haveSoldItems = true; }
                        if (i.getItemsForSale() > 0) { haveStockItems = true; }
                    }
                    break;
                }
            }

            if (!canSwitch) {
                switch (chest.getTradeType()) {
                    case BUY:
                        UtilString.get(Messages.COLLECT_ITEMS_TO_CHANGE).setVariables(chest)
                                .hex().sendMessage(player);
                        player.closeInventory();
                        break;
                    case SELL:
                        if (haveSoldItems) {
                            UtilString.get(Messages.COLLECT_COINS_TO_CHANGE).setVariables(chest)
                                    .hex().sendMessage(player);
                        }
                        if (haveStockItems) {
                            UtilString.get(Messages.COLLECT_ITEMS_TO_CHANGE).setVariables(chest)
                                    .hex().sendMessage(player);
                        }
                        player.closeInventory();
                        break;
                }
                return;
            }

            List<String> list = UtilMethods.getEconomyIDs(active);
            if (list.isEmpty()) return;

            String current = chest.getSelectedEconomy();
            int index = list.indexOf(current);
            String next = list.get((index + 1) % list.size());

            PlayerSwitchEconomyEvent switchEconomyEvent = new PlayerSwitchEconomyEvent(player, active, next);
            Bukkit.getServer().getPluginManager().callEvent(switchEconomyEvent);
            if (switchEconomyEvent.isCancelled()) return;

            chest.setSelectedEconomy(next);

            MoItemStack editor = item.getItemStackEditor();
            ArrayList<String> newLore = new ArrayList<>();
            newLore.add("&r");
            for (Economy eco : UtilMethods.getEconomies(active)) {
                String id = eco.getID();
                boolean activeEconomy = id.equalsIgnoreCase(next);
                String prefix = UtilString.get(eco.getPrefix()).removeColors().apply();
                newLore.add((activeEconomy ? " &a" : "&7") + "➛ " + prefix);
            }
            newLore.add("&r");
            newLore.add(UtilString.get(config.getSwitchMessage()).setVariables(chest).hex().apply());
            editor.setLore(newLore);

            updateSlots(gui, active);
            gui.refreshGUI(player);
            Set<Player> playersInventory = UtilMethods.closeInventoryPlayers(active.getLocation(), player);
            for (Player p : playersInventory) {
                UtilString.get(Messages.MODIFIED_CHEST).setVariables(chest).hex().sendMessage(p);
            }
        });
        return item;
    }

    public static GUIItem CloseItem(Player player) {
        ItemStack stack = OwnerStacks.getClose();

        GUIItem item = new GUIItem(stack);
        item.onItemClick(e -> {
            player.closeInventory();
        });
        return item;
    }


    public static GUIItem WhitePanelItem() {
        ItemStack stack = OwnerStacks.WhitePanelItem();
        return new GUIItem(stack);
    }

    public static GUIItem GrayPanelItem() {
        ItemStack stack = OwnerStacks.GrayPanelItem();
        return new GUIItem(stack);
    }

    public static GUIItem BackItem(Player player, ActiveChestShop active) {
        ItemStack stack = OwnerStacks.getBack();

        GUIItem item = new GUIItem(stack);
        item.onItemClick(e -> {
            ChestConfigurationMenu.open(player.getUniqueId(), active);
        });
        return item;
    }

    public static @NotNull GUIItem StatusSwitch(Player player, SingleGUI gui, ActiveChestShop active) {
        ItemStack stack = OwnerStacks.StatusSwitch(active);
        ChestShop chest = active.getChest();
        ChestStatusSwitchItemConfig config = Configs.getChestStatusItemConfig();
        String openPrefix = UtilString.get(ChestStatus.OPEN.getPrefix()).setVariables(chest).removeColors().apply();
        String closedPrefix = UtilString.get(ChestStatus.CLOSED.getPrefix()).setVariables(chest).removeColors().apply();
        String switchMessage = UtilString.get(config.getSwitchMessage()).setVariables(chest).apply();

        GUIItem item = new GUIItem(stack);
        item.onItemClick(e -> {

            String COOLDOWN_CODE = Config.PLUGIN_NAME+"OwnerItems-StatusSwitch-Cooldown"+chest.getUUID();
            long COOLDOWN = 2;
            if (Cooldown.startAndIsOnCooldown(COOLDOWN_CODE, COOLDOWN)) {
                List<VariableArg> cooldownVariables = new ArrayList<>();
                cooldownVariables.add(new VariableArg("%cooldown%", Cooldown.showCooldownFormatted(COOLDOWN_CODE, COOLDOWN, UtilMethods.getTimeFormat())));
                UtilString.get(Messages.COOLDOWN).setVariables(cooldownVariables)
                        .setVariables(chest).hex().sendMessage(player);
                player.closeInventory();
                return;
            }

            List<ChestStatus> types = ChestStatus.getTypes();
            // Recalled getStatus() to get the last updated TradeType
            int index = types.indexOf(chest.getStatus());
            ChestStatus next = types.get((index + 1) % types.size());

            PlayerSwitchStatusEvent switchStatusEvent = new PlayerSwitchStatusEvent(player, active, next);
            Bukkit.getPluginManager().callEvent(switchStatusEvent);
            if (switchStatusEvent.isCancelled()) return;

            if (next.equals(ChestStatus.CLOSED)) {
                Set<Player> playersInventory = UtilMethods.closeInventoryPlayers(active.getLocation(), player);
                for (Player p : playersInventory) {
                    UtilString.get(Messages.CLOSED_CHEST).setVariables(chest).hex().sendMessage(p);
                }
            }

            chest.setStatus(next);

            MoItemStack editor = item.getItemStackEditor();
            editor.setLore("&r",
                    (next == ChestStatus.OPEN ? " &a➛ "+openPrefix : "&7➛ "+openPrefix),
                    (next == ChestStatus.CLOSED ? " &a➛ "+closedPrefix : "&7➛ "+closedPrefix),
                    "&r",
                    switchMessage
            );
            gui.refreshGUI(player);


        });

        return item;
    }


}
