package mv.mossuh.mochestshop.UTILITIES.Chest;

import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestShopConfig;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.UTILITIES.Enchantments.Enchant;
import mv.mossuh.mochestshop.UTILITIES.Enchantments.Enchantments;
import mv.mossuh.mochestshop.UTILITIES.Enums.ChestStatus;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mochestshop.UTILITIES.ItemInfo;
import mv.mossuh.mochestshop.UTILITIES.Nbt.NBTChest;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChestCreator {

    public static ItemStack fromSection(String section, UUID uuid) {
        ChestShopConfig config = Configs.getChestShop(section);
        if (config.isValid()) {
            return fromMaterial(config, uuid);
        }
        return new ItemStack(Material.AIR, 1);
    }

    public static ItemStack fromChestShop(ChestShop chest) {
        UUID uuid = chest.getOwner();

        ItemStack itemStack = new ItemStack(Material.AIR, 1);
        if (ServerVersion.isAtLeast(ServerVersion.MC1_9)) {
            itemStack = new ItemStack(Material.CHEST, 1);
        } else {
            itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial("CHEST")), 1, (short) 0, (byte) 0);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();

        ChestShopConfig config = chest.getConfig();
        ItemInfo itemInfo = config.getItemInfo();

        Enchantments enchantments = itemInfo.getEnchantments();
        List<Enchant> enchants = itemInfo.getEnchantments().getEnchants();

        List<String> flags = itemInfo.getFlags();
        if (itemMeta != null) {
            if (itemInfo.hasName()) {
                String name = UtilString.get(itemInfo.getName()).setVariables(chest).setPlaceholders(uuid).hex().apply();
                itemMeta.setDisplayName(name);
            }
            if (itemInfo.hasLore()) {
                List<String> lore = new ArrayList<>();
                for (String line : itemInfo.getLore()) {
                    lore.add(UtilString.get(line).setVariables(chest).setPlaceholders(uuid).hex().apply());
                }
                itemMeta.setLore(lore);
            }

            if (enchantments.hasEnchant()) {
                for (Enchant enchant : enchants) {
                    String enchantName = enchant.getEnchantName();
                    int level = enchant.getLevel();

                    Enchantment enchantment = Enchantment.getByName(enchantName.toUpperCase());
                    if (enchantment != null) {
                        itemMeta.addEnchant(enchantment, level, true);
                    }
                }
            }

            if (itemInfo.hasFlag()) {
                for (String flag : flags) {
                    try {
                        itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                    } catch (IllegalArgumentException ignored) {

                    }
                }
            }
        }

        itemStack.setItemMeta(itemMeta);

        if (itemInfo.isUnique()) {
            NBTChest.setUnique(itemStack);
        }

        NBTChest.setUUID(itemStack, chest.getUUID());
        NBTChest.setOwner(itemStack, chest.getOwner());
        NBTChest.setUnlockedSlots(itemStack, chest.getUnlockedSlots());
        NBTChest.setEconomies(itemStack, chest.getEconomies());
        NBTChest.setEconomy(itemStack, chest.getSelectedEconomy());
        NBTChest.setTradeType(itemStack, chest.getTradeType());
        NBTChest.setChestStatus(itemStack, chest.getStatus());
        NBTChest.setConfigSection(itemStack, chest.getConfig().getSection());
        NBTChest.setSlots(itemStack, chest.getSlots());

        itemStack.setAmount(1);
        return itemStack;
    }

    private static ItemStack fromMaterial(ChestShopConfig config, UUID uuid) {
        ItemInfo itemInfo = config.getItemInfo();
        String section = config.getSection();

        ItemStack itemStack = new ItemStack(Material.AIR, 1);
        if (ServerVersion.isAtLeast(ServerVersion.MC1_9)) {
            itemStack = new ItemStack(Material.CHEST, 1);
        } else {
            itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial("CHEST")), 1, (short) 0, (byte) 0);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();

        Enchantments enchantments = itemInfo.getEnchantments();
        List<Enchant> enchants = itemInfo.getEnchantments().getEnchants();

        List<String> flags = itemInfo.getFlags();

        if (itemMeta != null) {
            if (itemInfo.hasName()) {
                String name = UtilString.get(itemInfo.getName()).setVariables(config).setPlaceholders(uuid).hex().apply();
                itemMeta.setDisplayName(name);
            }
            if (itemInfo.hasLore()) {
                List<String> lore = new ArrayList<>();
                for (String line : itemInfo.getLore()) {
                    lore.add(UtilString.get(line).setVariables(config).setPlaceholders(uuid).hex().apply());
                }
                itemMeta.setLore(lore);
            }


            if (enchantments.hasEnchant()) {
                for (Enchant enchant : enchants) {
                    String enchantName = enchant.getEnchantName();
                    int level = enchant.getLevel();

                    Enchantment enchantment = Enchantment.getByName(enchantName.toUpperCase());
                    if (enchantment != null) {
                        itemMeta.addEnchant(enchantment, level, true);
                    }
                }
            }

            if (itemInfo.hasFlag()) {
                for (String flag : flags) {
                    try {
                        itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                    } catch (IllegalArgumentException ignored) {

                    }
                }
            }
        }

        itemStack.setItemMeta(itemMeta);

        if (itemInfo.isUnique()) {
            NBTChest.setUnique(itemStack);
        }

        NBTChest.setUUID(itemStack);
        NBTChest.setOwner(itemStack, uuid);
        NBTChest.setUnlockedSlots(itemStack, config.getUnlockedSlots());
        NBTChest.setEconomies(itemStack, config.getEconomies());
        NBTChest.setEconomy(itemStack, config.getEconomies().stream().findFirst().orElse("invalid"));
        NBTChest.setTradeType(itemStack, TradeType.SELL);
        NBTChest.setChestStatus(itemStack, ChestStatus.OPEN);
        NBTChest.setConfigSection(itemStack, section);

        itemStack.setAmount(1);
        return itemStack;
    }

    public static void updateChestLore(UUID uuid, ChestShop chest, ItemStack itemStack) {
        ChestShopConfig config = chest.getConfig();
        ItemInfo itemInfo = config.getItemInfo();

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        Enchantments enchantments = itemInfo.getEnchantments();
        List<Enchant> enchants = itemInfo.getEnchantments().getEnchants();

        List<String> flags = itemInfo.getFlags();

        if (itemInfo.hasName()) {
            String name = UtilString.get(itemInfo.getName()).setVariables(chest).setPlaceholders(uuid).hex().apply();
            itemMeta.setDisplayName(name);
        }
        if (itemInfo.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String line : itemInfo.getLore()) {
                lore.add(UtilString.get(line).setVariables(chest).setPlaceholders(uuid).hex().apply());
            }
            itemMeta.setLore(lore);
        }


        if (enchantments.hasEnchant()) {
            for (Enchant enchant : enchants) {
                String enchantName = enchant.getEnchantName();
                int level = enchant.getLevel();

                Enchantment enchantment = Enchantment.getByName(enchantName.toUpperCase());
                if (enchantment != null) {
                    itemMeta.addEnchant(enchantment, level, true);
                }
            }
        }

        if (itemInfo.hasFlag()) {
            for (String flag : flags) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                } catch (IllegalArgumentException ignored) {

                }
            }
        }

        itemStack.setItemMeta(itemMeta);
    }
}
