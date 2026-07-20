package mv.mossuh.mochestshop.UTILITIES.Nbt;

import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mocore.NBT.NBTMethods;
import org.bukkit.inventory.ItemStack;

public class NBTSlotToken {

    public static void setSlotToken(ItemStack itemStack) {
        NBTMethods.setBoolean(itemStack, Config.PLUGIN_NAME + "-SlotToken", true);
    }

    public static boolean isSlotToken(ItemStack itemStack) {
        return NBTMethods.getBoolean(itemStack, Config.PLUGIN_NAME + "-SlotToken");
    }
}
