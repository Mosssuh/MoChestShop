package mv.mossuh.mochestshop.MENUS.ItemStacks;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig.ChestStatusSwitchItemConfig;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig.EconomySwitchItemConfig;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig.NormalItemConfig;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig.TradeSwitchItemConfig;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.TradeInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.Economies.Economy;
import mv.mossuh.mochestshop.UTILITIES.Enums.ChestStatus;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mochestshop.UTILITIES.ItemInfo;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.ITEM.MoEnchant;
import mv.mossuh.mocore.ITEM.MoItemStack;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OwnerStacks {

    public static ItemStack getClose() {
        NormalItemConfig config = Configs.getCloseItemConfig();
        ItemInfo itemInfo = config.getInfo();
        String material = itemInfo.getMaterial();
        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.BARRIER, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        stack.setName(UtilString.get(config.getInfo().getName()).apply());
        for (String l : itemInfo.getLore()) {
            stack.addLoreLine(UtilString.get(l)
                    .hex().apply());
        }

        List<MoEnchant> enchants = UtilMethods.getFormattedEnchants(itemInfo.getEnchantments());
        for (MoEnchant e : enchants) {
            stack.addEnchantment(e);
        }
        for (String f : itemInfo.getFlags()) {
            ItemFlag flag = ItemFlag.valueOf(f);
            stack.addFlag(flag);
        }
        return stack.obtain();
    }
    public static ItemStack getBack() {
        NormalItemConfig config = Configs.getBackItemConfig();
        ItemInfo itemInfo = config.getInfo();
        String material = itemInfo.getMaterial();
        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.ARROW, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        stack.setName(UtilString.get(config.getInfo().getName()).apply());
        for (String l : itemInfo.getLore()) {
            stack.addLoreLine(UtilString.get(l)
                    .hex().apply());
        }

        List<MoEnchant> enchants = UtilMethods.getFormattedEnchants(itemInfo.getEnchantments());
        for (MoEnchant e : enchants) {
            stack.addEnchantment(e);
        }
        for (String f : itemInfo.getFlags()) {
            ItemFlag flag = ItemFlag.valueOf(f);
            stack.addFlag(flag);
        }
        return stack.obtain();
    }

    public static ItemStack getPurchasingRemoveAmount(ChestShop chest, Slot activeSlot, int amount) {
        NormalItemConfig config = Configs.getPurchasingRemoveAmountItemConfig();
        ItemInfo itemInfo = config.getInfo();

        String material = itemInfo.getMaterial();

        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        TradeInfo tradeInfo = activeSlot.getInfo();

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%amount%", amount+""));

        stack.setName(UtilString.get(config.getInfo().getName()).setTradeInfo(tradeInfo).setVariables(variables).setChestShop(chest).apply());
        for (String l : itemInfo.getLore()) {
            stack.addLoreLine(UtilString.get(l).setTradeInfo(tradeInfo).setVariables(variables)
                    .setChestShop(chest).hex().apply());
        }

        List<MoEnchant> enchants = UtilMethods.getFormattedEnchants(itemInfo.getEnchantments());
        for (MoEnchant e : enchants) {
            stack.addEnchantment(e);
        }
        for (String f : itemInfo.getFlags()) {
            ItemFlag flag = ItemFlag.valueOf(f);
            stack.addFlag(flag);
        }
        return stack.obtain();
    }

    public static ItemStack getPurchasingAssignPrice(ChestShop chest, Slot activeSlot) {
        NormalItemConfig config = Configs.getAssignPurchasePriceItemConfig();
        ItemInfo itemInfo = config.getInfo();

        String material = itemInfo.getMaterial();

        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.SIGN, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }
        TradeInfo tradeInfo = activeSlot.getInfo();

        stack.setName(UtilString.get(config.getInfo().getName()).setTradeInfo(tradeInfo).setChestShop(chest).apply());
        for (String l : itemInfo.getLore()) {
            stack.addLoreLine(UtilString.get(l).setTradeInfo(tradeInfo)
                    .setChestShop(chest).hex().apply());
        }

        List<MoEnchant> enchants = UtilMethods.getFormattedEnchants(itemInfo.getEnchantments());
        for (MoEnchant e : enchants) {
            stack.addEnchantment(e);
        }
        for (String f : itemInfo.getFlags()) {
            ItemFlag flag = ItemFlag.valueOf(f);
            stack.addFlag(flag);
        }
        return stack.obtain();
    }

    public static ItemStack getSellAssignPrice(ChestShop chest, Slot activeSlot) {
        NormalItemConfig config = Configs.getAssignSalePriceItemConfig();
        ItemInfo itemInfo = config.getInfo();

        String material = itemInfo.getMaterial();

        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.SIGN, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        TradeInfo tradeInfo = activeSlot.getInfo();

        stack.setName(UtilString.get(config.getInfo().getName()).setTradeInfo(tradeInfo).setChestShop(chest).apply());
        for (String l : itemInfo.getLore()) {
            stack.addLoreLine(UtilString.get(l).setTradeInfo(tradeInfo)
                    .setChestShop(chest).hex().apply());
        }

        List<MoEnchant> enchants = UtilMethods.getFormattedEnchants(itemInfo.getEnchantments());
        for (MoEnchant e : enchants) {
            stack.addEnchantment(e);
        }
        for (String f : itemInfo.getFlags()) {
            ItemFlag flag = ItemFlag.valueOf(f);
            stack.addFlag(flag);
        }
        return stack.obtain();
    }

    public static ItemStack getItemsForSale(ChestShop chest, Slot activeSlot) {
        NormalItemConfig config = Configs.getItemsForSaleItemConfig();
        ItemInfo itemInfo = config.getInfo();

        String material = itemInfo.getMaterial();

        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.CHEST, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        TradeInfo tradeInfo = activeSlot.getInfo();

        stack.setName(UtilString.get(config.getInfo().getName()).setTradeInfo(tradeInfo).setChestShop(chest).apply());
        for (String l : itemInfo.getLore()) {
            stack.addLoreLine(UtilString.get(l).setTradeInfo(tradeInfo)
                    .setChestShop(chest).hex().apply());
        }

        List<MoEnchant> enchants = UtilMethods.getFormattedEnchants(itemInfo.getEnchantments());
        for (MoEnchant e : enchants) {
            stack.addEnchantment(e);
        }
        for (String f : itemInfo.getFlags()) {
            ItemFlag flag = ItemFlag.valueOf(f);
            stack.addFlag(flag);
        }
        return stack.obtain();
    }

    public static ItemStack getWithdrawCurrency(ChestShop chest, Slot activeSlot) {
        NormalItemConfig config = Configs.getSellingWithdrawItemConfig();
        ItemInfo itemInfo = config.getInfo();

        String material = itemInfo.getMaterial();

        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        TradeInfo tradeInfo = activeSlot.getInfo();

        stack.setName(UtilString.get(config.getInfo().getName()).setTradeInfo(tradeInfo).setChestShop(chest).apply());
        for (String l : itemInfo.getLore()) {
            stack.addLoreLine(UtilString.get(l).setTradeInfo(tradeInfo)
                    .setChestShop(chest).hex().apply());
        }

        List<MoEnchant> enchants = UtilMethods.getFormattedEnchants(itemInfo.getEnchantments());
        for (MoEnchant e : enchants) {
            stack.addEnchantment(e);
        }
        for (String f : itemInfo.getFlags()) {
            ItemFlag flag = ItemFlag.valueOf(f);
            stack.addFlag(flag);
        }

        return stack.obtain();
    }

    public static ItemStack UnlockedSlotItem(int slot) {
        NormalItemConfig config = Configs.getUnLockedItemConfig();
        ItemInfo itemInfo = config.getInfo();
        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        String material = itemInfo.getMaterial();
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%slot%", slot+""));

        stack.setName(UtilString.get(config.getInfo().getName()).setVariables(variables).apply());
        for (String l : itemInfo.getLore()) {
            stack.addLoreLine(UtilString.get(l).setVariables(variables)
                    .hex().apply());
        }

        List<MoEnchant> enchants = UtilMethods.getFormattedEnchants(itemInfo.getEnchantments());
        for (MoEnchant e : enchants) {
            stack.addEnchantment(e);
        }
        for (String f : itemInfo.getFlags()) {
            ItemFlag flag = ItemFlag.valueOf(f);
            stack.addFlag(flag);
        }
        return stack.obtain();
    }

    public static ItemStack LockedSlotItem(int slot) {
        NormalItemConfig config = Configs.getLockedItemConfig();
        ItemInfo itemInfo = config.getInfo();
        String material = itemInfo.getMaterial();

        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%slot%", slot+""));

        stack.setName(UtilString.get(config.getInfo().getName()).setVariables(variables).apply());
        for (String l : itemInfo.getLore()) {
            stack.addLoreLine(UtilString.get(l).setVariables(variables)
                    .hex().apply());
        }

        List<MoEnchant> enchants = UtilMethods.getFormattedEnchants(itemInfo.getEnchantments());
        for (MoEnchant e : enchants) {
            stack.addEnchantment(e);
        }
        for (String f : itemInfo.getFlags()) {
            ItemFlag flag = ItemFlag.valueOf(f);
            stack.addFlag(flag);
        }
        return stack.obtain();
    }

    public static ItemStack TradeSwitch(ActiveChestShop active) {
        ChestShop chest = active.getChest();
        TradeSwitchItemConfig config = Configs.getTradeItemConfig();
        String material = config.getMaterial();
        byte data = config.getData();
        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.PAPER, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        TradeType currentType = chest.getTradeType();

        String buyPrefix = UtilString.get(TradeType.BUY.getPrefix()).setChestShop(chest).removeColors().apply();
        String sellPrefix = UtilString.get(TradeType.SELL.getPrefix()).setChestShop(chest).removeColors().apply();
        String switchMessage = UtilString.get(config.getSwitchMessage()).setChestShop(chest).apply();

        stack.setName(UtilString.get(config.getName()).setChestShop(chest).hex().apply());
        stack.setLore(
                "&r",
                (currentType == TradeType.SELL ? " &a➛ "+sellPrefix : "&7➛ "+sellPrefix),
                (currentType == TradeType.BUY ? " &a➛ "+buyPrefix : "&7➛ "+buyPrefix),
                "&r",
                switchMessage
        );
        return stack.obtain();
    }

    public static ItemStack EconomyItem(ActiveChestShop active) {
        ChestShop chest = active.getChest();
        EconomySwitchItemConfig config = Configs.getEconomyItemConfig();
        String material = config.getMaterial();
        byte data = config.getData();
        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.MAGMA_CREAM, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        String selected = chest.getSelectedEconomy();
        List<Economy> economies = UtilMethods.getEconomies(active);

        stack.setName(UtilString.get(config.getName()).setChestShop(chest).hex().apply());
        stack.addLoreLine("&r");
        for (Economy e : economies) {
            String id = e.getID();
            boolean activeEconomy = id.equalsIgnoreCase(selected);
            String prefix = UtilString.get(e.getPrefix()).removeColors().apply();
            stack.addLoreLine((activeEconomy ? " &a" : "&7") + "➛ " + prefix);
        }
        stack.addLoreLine("&r");
        stack.addLoreLine(UtilString.get(config.getSwitchMessage()).setChestShop(chest).hex().apply());
        return stack.obtain();
    }

    public static ItemStack WhitePanelItem() {
        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        stack = new MoItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        stack.setName("&r");
        return stack.obtain();
    }

    public static ItemStack GrayPanelItem() {
        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        stack = new MoItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        stack.setName("&r");
        return stack.obtain();
    }

    public static ItemStack StatusSwitch(ActiveChestShop active) {
        ChestStatusSwitchItemConfig config = Configs.getChestStatusItemConfig();
        String material = config.getMaterial();
        byte data = config.getData();
        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.LIME_DYE, (byte) 0, 1);
        } else {
            stack = new MoItemStack(Material.matchMaterial(material), 1);
        }

        ChestShop chest = active.getChest();
        ChestStatus currentStatus = chest.getStatus();

        String openPrefix = UtilString.get(ChestStatus.OPEN.getPrefix()).setChestShop(chest).removeColors().apply();
        String closedPrefix = UtilString.get(ChestStatus.CLOSED.getPrefix()).setChestShop(chest).removeColors().apply();
        String switchMessage = UtilString.get(config.getSwitchMessage()).setChestShop(chest).apply();

        stack.setName(UtilString.get(config.getName()).setChestShop(chest).hex().apply());
        stack.setLore("&r",
                (currentStatus == ChestStatus.OPEN ? " &a➛ "+openPrefix : "&7➛ "+openPrefix),
                (currentStatus == ChestStatus.CLOSED ? " &a➛ "+closedPrefix : "&7➛ "+closedPrefix),
                "&r",
                switchMessage
        );
        return stack.obtain();
    }
}
