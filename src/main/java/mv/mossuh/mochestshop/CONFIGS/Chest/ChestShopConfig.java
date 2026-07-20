package mv.mossuh.mochestshop.CONFIGS.Chest;

import mv.mossuh.mochestshop.UTILITIES.ItemInfo;

import java.util.ArrayList;
import java.util.List;

public class ChestShopConfig {
    private String section = "invalid";
    private ItemInfo itemInfo = new ItemInfo();
    private int unlockedSlots = 0;
    private List<String> economies = new ArrayList<>();

    public ChestShopConfig(String section, ItemInfo itemInfo, Integer unlockedSlots, List<String> economies) {
        if (section != null) { this.section = section; }
        if (unlockedSlots != null) {
            if (unlockedSlots < 0) {
                this.unlockedSlots = 0;
            } else if (unlockedSlots > 10) {
                this.unlockedSlots = 10;
            } else {
                this.unlockedSlots = unlockedSlots;
            }
        }

        if (itemInfo != null) this.itemInfo = itemInfo;
        if (economies != null) { this.economies = economies; }
    }

    public ChestShopConfig() {}

    public boolean isValid() { return !section.equals("invalid") && !itemInfo.getMaterial().equals("AIR"); }
    public String getSection() { return section; }
    public boolean isSection(String section) {
        return this.section.equalsIgnoreCase(section);
    }
    public ItemInfo getItemInfo() { return itemInfo; }
    public int getUnlockedSlots() { return unlockedSlots; }


    public void setUnlockedSlots(Integer slots) {
        if (slots == null) {
            this.unlockedSlots = 0;
            return;
        };
        if (slots > 10) {
            this.unlockedSlots = 10;
            return;
        }
        if (slots < 0) {
            this.unlockedSlots = 0;
            return;
        }

        this.unlockedSlots = slots;
    }

    public void addUnlockedSlots(Integer slots) {
        if (slots == null) {
            return;
        };
        int newSlots = this.unlockedSlots + slots;
        if (newSlots > 10) {
            this.unlockedSlots = 10;
            return;
        }
        if (newSlots < 0) {
            this.unlockedSlots = 0;
            return;
        }
        this.unlockedSlots = newSlots;
    }

    public List<String> getEconomies() { return economies; }
    public boolean hasEconomy(String economy) {
        for (String e : economies) {
            if (e.equalsIgnoreCase(economy)) {
                return true;
            }
        }
        return false;
    }
}
