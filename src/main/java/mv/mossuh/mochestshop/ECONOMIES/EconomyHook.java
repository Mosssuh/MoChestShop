package mv.mossuh.mochestshop.ECONOMIES;

import java.util.UUID;

public interface EconomyHook {
    String getPlugin();
    String getEconomy();
    void setEconomy(String economy);
    double getAmount(UUID uuid);
    void removeAmount(UUID uuid, double amount);
    void addAmount(UUID uuid, double amount);
    boolean hasAmount(UUID uuid, double amount);
}
