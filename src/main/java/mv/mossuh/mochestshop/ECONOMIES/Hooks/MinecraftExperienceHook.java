package mv.mossuh.mochestshop.ECONOMIES.Hooks;

import mv.mossuh.mochestshop.ECONOMIES.EconomyHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MinecraftExperienceHook implements EconomyHook {
    private static final String plugin = "Minecraft";
    private String economy = "experience";

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
        this.economy = economy;
    }

    @Override
    public double getAmount(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return 0;
        
        return player.getTotalExperience();
    }

    @Override
    public void removeAmount(UUID uuid, double amount) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        double exp = player.getTotalExperience();
        int newExp = (int) (exp - amount);
        player.setTotalExperience(newExp);
    }

    @Override
    public void addAmount(UUID uuid, double amount) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        double exp = player.getTotalExperience();
        int newExp = (int) (exp - amount);
        player.setTotalExperience(newExp);
    }

    @Override
    public boolean hasAmount(UUID uuid, double amount) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;

        double exp = player.getTotalExperience();
        return exp >=  amount;
    }
}
