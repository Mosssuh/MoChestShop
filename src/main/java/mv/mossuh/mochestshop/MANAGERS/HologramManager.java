package mv.mossuh.mochestshop.MANAGERS;

import mv.mossuh.mochestshop.HOLOGRAM.HologramHook;

public class HologramManager {
    private static HologramHook hologramHook;

    public HologramHook getHologramHook() { return hologramHook; }
    public static void setHologramHook(HologramHook h) { hologramHook = h; }

}
