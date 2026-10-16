package mv.mossuh.mochestshop.ECONOMIES.Hooks;

import mv.mossuh.mochestshop.ECONOMIES.EconomyHook;
import org.black_ixx.playerpoints.PlayerPointsAPI;

import java.util.UUID;

public class PlayerPointsEconomyHook implements EconomyHook {
    private PlayerPointsAPI api;
    public void setAPI(PlayerPointsAPI api) {
        this.api = api;
    }

    private static final String PLUGIN = "PlayerPoints";
    private String economy = "points";

    @Override
    public String getPlugin() {
        return PLUGIN;
    }

    @Override
    public String getEconomy() {
        return economy;
    }

    @Override
    public void setEconomy(String economy) {
        this.economy = economy;
    }

    @Override
    public double getAmount(UUID uuid) {
        return api.look(uuid);
    }

    @Override
    public void removeAmount(UUID uuid, double amount) {
        api.take(uuid, (int) amount);
    }

    @Override
    public void addAmount(UUID uuid, double amount) {
        api.give(uuid, (int) amount);
    }

    @Override
    public boolean hasAmount(UUID uuid, double amount) {
        return api.look(uuid) >= amount;
    }
}
