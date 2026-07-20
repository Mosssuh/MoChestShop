package mv.mossuh.mochestshop.COMMANDS;

import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.UTILITIES.Chest.ChestCreator;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mocore.UTILITIES.UsefulString;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GiveChestCommands {
    /*

    /mochestshop give <player> <chest> <amount>
    /mochestshop giveall <player> <chest> <amount>

     */

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 1) {
            if (strings[0].equalsIgnoreCase("give")) {
                // /mochestshop give <player> <chest> <amount>
                if (UsefulString.get("mochestshop.admin").hasPermission(sender)) {
                    if (strings.length >= 4) {
                        String playerName = strings[1];
                        Player player = Bukkit.getPlayer(playerName);
                        String section = strings[2];
                        String amountString = strings[3];
                        if (!UsefulMethods.isOnline(player)) {
                            UsefulString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                            return;
                        }
                        if (!Configs.hasChestShop(section)) {
                            UsefulString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                            return;
                        }
                        if (!UsefulString.get(amountString).isNumeric()) {
                            UsefulString.get(Messages.INVALID_QUANTITY).hex().sendMessage(sender);
                            return;
                        }

                        UUID uuid = player.getUniqueId();
                        int amount = Integer.parseInt(amountString);

                        for (int i = 0; i < amount; i++) {
                            ItemStack item = ChestCreator.fromSection(section, uuid);
                            player.getInventory().addItem(item);
                        }

                        List<VariableArg> variables = new ArrayList<>();
                        variables.add(new VariableArg("%amount%", amountString));
                        variables.add(new VariableArg("%section%", section));
                        UtilString.get(Messages.GIVE_CHEST_SENDER).setVariables(variables).setDefaultPlayerVariables(player).hex().sendMessage(sender);
                        UtilString.get(Messages.GIVE_CHEST_RECEIVER).setVariables(variables).setDefaultPlayerVariables(player).hex().sendMessage(player);
                    } else {
                        UsefulString.get("&cUse: /mochestshop give <player> <chest> <amount>").hex().setPlaceholders(sender).sendMessage(sender);
                    }
                } else {
                    UsefulString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
