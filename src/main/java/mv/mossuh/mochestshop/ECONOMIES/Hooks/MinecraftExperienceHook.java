package mv.mossuh.mochestshop.ECONOMIES.Hooks;

import mv.mossuh.mochestshop.ECONOMIES.EconomyHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MinecraftExperienceHook implements EconomyHook {

    private static final String PLUGIN = "Minecraft";

    private String economy = "experience";

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
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return 0;

        return player.getTotalExperience();
    }

    @Override
    public void removeAmount(UUID uuid, double amount) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        int newExperience = (int) Math.max(
                0,
                player.getTotalExperience() - amount
        );

        setExperience(player, newExperience);
    }

    @Override
    public void addAmount(UUID uuid, double amount) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        int newExperience = (int) (
                player.getTotalExperience() + amount
        );

        setExperience(player, newExperience);
    }

    @Override
    public boolean hasAmount(UUID uuid, double amount) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;

        return player.getTotalExperience() >= amount;
    }

    private void setExperience(Player player, int experience) {
        experience = Math.max(0, experience);
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);

        player.giveExp(experience);
    }
}
