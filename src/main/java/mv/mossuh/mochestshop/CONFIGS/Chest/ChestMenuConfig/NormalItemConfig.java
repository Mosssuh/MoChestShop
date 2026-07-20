package mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig;

import mv.mossuh.mochestshop.UTILITIES.ItemInfo;

public class NormalItemConfig {
    private ItemInfo info = new ItemInfo();

    public NormalItemConfig(ItemInfo info) {
        if (info != null) { this.info = info; }
    }
    public NormalItemConfig() {}
    public ItemInfo getInfo() { return info; }
}
