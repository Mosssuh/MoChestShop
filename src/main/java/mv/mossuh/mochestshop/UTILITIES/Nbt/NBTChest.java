package mv.mossuh.mochestshop.UTILITIES.Nbt;

import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.Enums.ChestStatus;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mocore.NBT.NBTMethods;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class NBTChest {
    public static void setHeadTexture(ItemStack itemStack, String value) {
        NBTMethods.setHeadTexture(itemStack, value);
    }

    public static void setUUID(ItemStack itemStack) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "UUID", UUID.randomUUID().toString());
    }
    public static void setUUID(ItemStack itemStack, UUID uuid) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "UUID", uuid.toString());
    }
    public static void setOwner(ItemStack itemStack, UUID uuid) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "OwnerUUID", uuid.toString());
    }
    public static void setUnlockedSlots(ItemStack itemStack, int slots) {
        NBTMethods.setInteger(itemStack, Config.PLUGIN_NAME + "UnlockedSlots", slots);
    }
    public static void setTradeType(ItemStack itemStack, TradeType type) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "TradeType", type.name());
    }
    public static void setChestStatus(ItemStack itemStack, ChestStatus status) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "ChestStatus", status.name());
    }
    public static void setEconomy(ItemStack itemStack, String economy) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "Economy", economy.toLowerCase());
    }
    public static void setEconomies(ItemStack itemStack, List<String> economies) {
        String list = String.join("::", economies);
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "Economies", list);
    }
    public static void setUnique(ItemStack itemStack) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME+"UniqueID", UUID.randomUUID().toString());
    }
    public static void setConfigSection(ItemStack itemStack, String section) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "ConfigSection", section.toLowerCase());
    }

    public static void setSlots(ItemStack itemStack, List<Slot> slots) {
        String serializedItems = Slot.serializeItemStack(slots);
        String serializedSlotsData = Slot.serializeSlotData(slots);
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "SerializedSlotsItemStack", serializedItems);
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "SerializedSlotsData", serializedSlotsData);
    }



    public static UUID getUUID(ItemStack itemStack) {
        String uuidString = NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "UUID");
        if (uuidString == null) { return null; }
        return UUID.fromString(uuidString);
    }
    public static UUID getOwner(ItemStack itemStack) {
        String uuidString = NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "OwnerUUID");
        if (uuidString == null) { return null; }
        return UUID.fromString(uuidString);
    }
    public static int getUnlockedSlots(ItemStack itemStack) {
        return NBTMethods.getInteger(itemStack, Config.PLUGIN_NAME + "UnlockedSlots");
    }
    public static TradeType getTradeType(ItemStack itemStack) {
        String string = NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "TradeType");
        return TradeType.fromString(string);
    }
    public static ChestStatus getChestStatus(ItemStack itemStack) {
        String string = NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "ChestStatus");
        return ChestStatus.fromString(string);
    }
    public static String getEconomy(ItemStack itemStack) {
       return NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "Economy");
    }
    public static List<String> getEconomies(ItemStack itemStack) {
        String economies = NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "Economies");
        String[] split = economies.split("::");
        List<String> list =  new ArrayList<>();
        Collections.addAll(list, split);
        return list;
    }
    public static Boolean isUnique(ItemStack itemStack) {
        String id = NBTMethods.getString(itemStack, Config.PLUGIN_NAME+"UniqueID");
        return id != null;
    }
    public static String getConfigSection(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "ConfigSection");
    }
    public static List<Slot> getSlots(ItemStack itemStack) {
        String serializedItems = NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "SerializedSlotsItemStack");
        String serializedSlotsData = NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "SerializedSlotsData");
        return UtilMethods.getSlots(serializedItems, serializedSlotsData);
    }
}
