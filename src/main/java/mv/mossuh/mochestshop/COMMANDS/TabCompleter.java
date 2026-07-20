package mv.mossuh.mochestshop.COMMANDS;

import mv.mossuh.mochestshop.CONFIGS.Chest.ChestShopConfig;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("give", "token", "setslots");
        } else if (args.length == 2) {
            switch (args[0]) {
                case "give":
                    return getPlayers();
                case "setslots":
                    return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
                case "token":
                    return Arrays.asList("give");
            }
        } else if (args.length == 3) {
            switch (args[0]) {
                case "give":
                    return getChestSections();
                case "token":
                    return getPlayers();
            }
        } else if (args.length == 4) {
            switch (args[0]) {
                case "give":
                    Arrays.asList("1", "5", "10", "20");
            }
        }
        return Collections.emptyList();
    }
    /*

    /mochestshop give <player> <chest> <amount>
    /mochestshop token give <player>
    /mochestshop setslots <slots>

     */

    public List<String> getChestSections() {
        List<ChestShopConfig> configs = new ArrayList<>(Configs.getChestsShop());
        List<String> names = new ArrayList<>();
        for (ChestShopConfig config : configs) {
            names.add(config.getSection());
        }
        return names;
    }

    public List<String> getPlayers() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        List<String> names = new ArrayList<>();
        for (Player p : players) {
            names.add(p.getName());
        }
        return names;
    }
}
