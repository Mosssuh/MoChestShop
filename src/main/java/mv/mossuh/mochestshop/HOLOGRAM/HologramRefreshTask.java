package mv.mossuh.mochestshop.HOLOGRAM;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.MoChestShop;
import org.bukkit.Bukkit;


public class HologramRefreshTask implements Runnable {

    private static final MoChestShop main = MoChestShop.getInstance();
    private static final int INTERVAL_TICKS = (int) (20 * Config.HOLOGRAM_UPDATE_INTERVAL);

    private static boolean started = false;

    public static void start() {
        if (started) return;
        started = true;

        Bukkit.getScheduler().runTaskTimer(
                main,
                new HologramRefreshTask(),
                0L,
                INTERVAL_TICKS
        );
    }

    @Override
    public void run() {
        for (ActiveChestShop a : ChestShopAPI.getManagers().getChests().getChests()) {
            if (a.isValid()) {
                ChestShopAPI.getManagers().getHolograms().getHologramHook().refresh(a);
            } else {
                ChestShopAPI.getManagers().getHolograms().getHologramHook().remove(a);
            }
        }
    }
}


