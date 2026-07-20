package mv.mossuh.mochestshop.HOLOGRAM;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;

public interface HologramHook {
    String getPlugin();

    void refresh(ActiveChestShop active);

    void spawn(ActiveChestShop active);

    void remove(ActiveChestShop active);
}
