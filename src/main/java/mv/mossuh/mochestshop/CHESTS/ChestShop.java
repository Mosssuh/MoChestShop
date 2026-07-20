package mv.mossuh.mochestshop.CHESTS;

import mv.mossuh.mochestshop.CONFIGS.Chest.ChestShopConfig;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.InvalidInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.PurchasingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.SellingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.TradeInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.Enums.ChestStatus;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mochestshop.UTILITIES.Nbt.NBTChest;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChestShop {
    private UUID uuid;
    private UUID owner;
    private ChestShopConfig config = new ChestShopConfig();
    private TradeType trade = TradeType.BUY;
    private ChestStatus status = ChestStatus.OPEN;
    private int unlockedSlots = 0;
    private List<String> economies = new ArrayList<>();
    private String economy = "@invalid";
    private List<Slot> slots = new ArrayList<>();

    public ChestShop(UUID uuid, UUID owner, ChestShopConfig config, TradeType trade, ChestStatus status, Integer unlockedSlots, List<String> economies, String economy, List<Slot> slots) {
        this.uuid = uuid;
        this.owner = owner;
        if  (config != null) { this.config = config; }
        if (trade != null) { this.trade = trade; }
        if (status != null) { this.status = status; }
        if (unlockedSlots != null) { this.unlockedSlots = unlockedSlots; }
        if (economies != null) { this.economies = economies; }
        if (economy != null) {
            if (economy.isEmpty()) {
                if (economies != null) {
                    this.economy = economies.stream().findFirst().orElse("@invalid");
                }
            } else {
                this.economy = economy.toLowerCase();
            }
        }
        if (slots != null) { this.slots = slots; }
    }

    public ChestShop() {}

    public UUID getUUID() { return uuid; }
    public UUID getOwner() { return owner; }
    public boolean isOwner(UUID uuid) {
        return this.owner.equals(uuid);
    }
    public ChestShopConfig getConfig() { return config; }
    public TradeType getTradeType() { return trade; }
    public ChestStatus getStatus() { return status; }
    public void setStatus(ChestStatus status) {
        if (status != null) { this.status = status; }
    }
    public int getUnlockedSlots() { return unlockedSlots; }
    public void setUnlockedSlots(int slots) {
        if (slots < 0) {
            slots = 0;
        } else if (slots > 10) {
            slots = 10;
        }
        this.unlockedSlots = slots;
    }
    public List<String> getEconomies() { return economies; }
    public void addEconomy(String economy) {
        for (String e : economies) {
            if (e.equalsIgnoreCase(economy)) {
                return;
            }
        }
        economies.add(economy.toLowerCase());
    }
    public void removeEconomy(String economy) {
        economies.removeIf(e -> e.equalsIgnoreCase(economy));
    }

    public String getSelectedEconomy() { return economy; }
    public void setSelectedEconomy(String economy) {
        if (economy != null) { this.economy = economy.toLowerCase(); }
    }

    public void setTradeType(TradeType type) {
        if (type == null) return;
        this.trade = type;
        for (Slot s : slots) {
            s.setInfo(type);
        }
    }
    public List<Slot> getSlots() { return slots; }
    public Slot getSlot(int slot) {
        for (Slot s : slots) {
            if (s.getSlot() == slot) return s;
        }
        return new Slot();
    }
    public void setSlot(int slot, Slot slotData) {
        if (slot <= 0 || slot > 10) return;
        if (slotData == null) return;

        slots.removeIf(s -> s.getSlot() == slot);
        slots.add(slotData);
    }

    public void removeSlot(int slot) {
        if (slot <= 0 || slot > 10) return;

        slots.removeIf(s -> s.getSlot() == slot);
    }
    public boolean hasEmptySlot() {
        if (slots.size() < unlockedSlots) return true;

        for (Slot s : slots) {
            if (!s.hasItem()) {
                return true;
            }
        }
        return false;
    }
    public int getEmptySlot() {
        List<Integer> positions = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        for (Slot s : slots) {
            if (s.hasItem()) {
                positions.remove(Integer.valueOf(s.getSlot()));
            }
        }

        positions.removeIf(pos -> pos > unlockedSlots);

        if (!positions.isEmpty()) {
            return positions.get(0);
        }

        return 0;
    }
    public void addSlot(Slot slotData) {
        if (slots.size() < unlockedSlots) {
            slots.add(slotData);
            return;
        }

        for (Slot s : slots) {
            if (!s.hasItem()) {
                slots.add(slotData);
                return;
            }
        }
    }

    public boolean isInShop(ItemStack item) {
        for (Slot slot : slots) {
            if (slot.getItem().isSimilar(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid() {
        return uuid != null && owner != null;
    }


    public static boolean isChestShop(ItemStack item) {
        UUID uuid = NBTChest.getUUID(item);
        UUID owner = NBTChest.getOwner(item);
        return uuid != null && owner != null;
    }

    public static ChestShop getChestShop(ItemStack item) {
        UUID uuid = NBTChest.getUUID(item);
        UUID owner = NBTChest.getOwner(item);
        if (uuid != null && owner != null) {
            String section = NBTChest.getConfigSection(item);
            ChestShopConfig config = Configs.getChestShop(section);
            TradeType tradeType = NBTChest.getTradeType(item);
            ChestStatus status = NBTChest.getChestStatus(item);
            int unlockedSlots = NBTChest.getUnlockedSlots(item);
            List<String> economies = NBTChest.getEconomies(item);
            String economy = NBTChest.getEconomy(item);

            List<Slot> slots = NBTChest.getSlots(item);
            return new ChestShop(uuid, owner, config, tradeType, status, unlockedSlots, economies, economy, slots);
        }
        return new ChestShop();
    }

    public static TradeInfo getDefaultTradeInfo(ChestShop chest) {
        TradeType type = chest.getTradeType();
        switch (type) {
            case SELL:
                return new SellingInfo();
            case BUY:
                return new PurchasingInfo();
            case NONE:
            default:
                return new InvalidInfo();
        }
    }
}
