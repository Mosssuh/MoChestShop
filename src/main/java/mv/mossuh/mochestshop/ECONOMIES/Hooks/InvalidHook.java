package mv.mossuh.mochestshop.ECONOMIES.Hooks;

import mv.mossuh.mochestshop.ECONOMIES.EconomyHook;

import java.util.UUID;

public class InvalidHook implements EconomyHook {
    private static final String plugin = "INVALID";
    private String economy = "";


    @Override
    public String getPlugin() {
        return plugin;
    }

    @Override
    public String getEconomy() {
        return economy;
    }

    @Override
    public void setEconomy(String economy) {
        this.economy = "";
    }

    @Override
    public double getAmount(UUID uuid) {
        return 0;
    }

    @Override
    public void removeAmount(UUID uuid, double amount) { }

    @Override
    public void addAmount(UUID uuid, double amount) { }

    @Override
    public boolean hasAmount(UUID uuid, double amount) {
        return false;
    }
}
