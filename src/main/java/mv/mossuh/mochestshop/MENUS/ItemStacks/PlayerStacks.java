package mv.mossuh.mochestshop.MENUS.ItemStacks;

import mv.mossuh.mocore.ITEM.MoEnchant;
import mv.mossuh.mocore.ITEM.MoItemStack;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig.NormalItemConfig;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.UTILITIES.ItemInfo;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerStacks {
    public static ItemStack NoItemInTradeItem() {
        NormalItemConfig config = Configs.getNoItemInTradeConfig();
        ItemInfo itemInfo = config.getInfo();
        String material = itemInfo.getMaterial();
        MoItemStack stack = new MoItemStack(Material.BARRIER, 1);
        if (material == null || material.isEmpty()) {
            stack = new MoItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
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
}
