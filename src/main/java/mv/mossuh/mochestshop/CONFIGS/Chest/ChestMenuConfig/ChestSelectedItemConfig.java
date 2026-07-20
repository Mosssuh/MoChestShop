package mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig;

import java.util.ArrayList;
import java.util.List;

public class ChestSelectedItemConfig {
    private List<String> purchasingLore = new ArrayList<>();
    private List<String> sellingLore = new ArrayList<>();

    public ChestSelectedItemConfig(List<String> purchasingLore, List<String> sellingLore) {
        if (purchasingLore != null) { this.purchasingLore = purchasingLore; }
        if (sellingLore != null) { this.sellingLore = sellingLore; }
    }
    public ChestSelectedItemConfig() {}

    public List<String> getPurchasingLore() { return purchasingLore; }
    public List<String> getSellingLore() { return sellingLore; }
}
