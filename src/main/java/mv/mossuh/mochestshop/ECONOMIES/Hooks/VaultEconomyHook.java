package mv.mossuh.mochestshop.ECONOMIES.Hooks;

import mv.mossuh.mochestshop.ECONOMIES.EconomyHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class VaultEconomyHook implements EconomyHook {
    private Economy eco;
    public void setProvider(Economy e) { eco = e; }


    private static final String plugin = "Vault";
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
        this.economy = eco.getName();
    }

    @Override
    public double getAmount(UUID uuid) {
        OfflinePlayer player = Bukkit.getPlayer(uuid);
        if (player == null) return 0;
        return eco.getBalance(player);
    }

    @Override
    public void removeAmount(UUID uuid, double amount) {
        OfflinePlayer player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        eco.withdrawPlayer(player, amount);
    }

    @Override
    public void addAmount(UUID uuid, double amount) {
        OfflinePlayer player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        eco.depositPlayer(player, amount);
    }

    @Override
    public boolean hasAmount(UUID uuid, double amount) {
        OfflinePlayer player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        double coins = eco.getBalance(player);
        return coins >= amount;
    }
}
