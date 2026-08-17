package mv.mossuh.mochestshop.UTILITIES.SlotToken;

import mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig.NormalItemConfig;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.UTILITIES.Enchantments.Enchant;
import mv.mossuh.mochestshop.UTILITIES.Enchantments.Enchantments;
import mv.mossuh.mochestshop.UTILITIES.ItemInfo;
import mv.mossuh.mochestshop.UTILITIES.Nbt.NBTSlotToken;
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

public class SlotTokenCreator {

    public static ItemStack createSlotToken() {
        NormalItemConfig config = Configs.getSlotTokenConfig();
        ItemInfo itemInfo = config.getInfo();

        ItemStack itemStack = new ItemStack(Material.AIR, 1);
        if (ServerVersion.isAtLeast(ServerVersion.MC1_9)) {
            itemStack = new ItemStack(Material.MAGMA_CREAM, 1);
        } else {
            itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial("MAGMA_CREAM")), 1, (short) 0, (byte) 0);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();

        Enchantments enchantments = itemInfo.getEnchantments();
        List<Enchant> enchants = itemInfo.getEnchantments().getEnchants();

        List<String> flags = itemInfo.getFlags();

        if (itemInfo.hasName()) {
            String name = UtilString.get(itemInfo.getName()).hex().apply();
            itemMeta.setDisplayName(name);
        }
        if (itemInfo.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String line : itemInfo.getLore()) {
                lore.add(UtilString.get(line).hex().apply());
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
        NBTSlotToken.setSlotToken(itemStack);

        itemStack.setAmount(1);
        return itemStack;
    }
}
