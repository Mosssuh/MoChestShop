package mv.mossuh.mochestshop.COMMANDS;

import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.UTILITIES.SlotToken.SlotTokenCreator;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mocore.UTILITIES.UsefulString;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SlotTokenCommands {
    /*

    /mochestshop give <player> <chest> <amount>
    /mochestshop giveall <player> <chest> <amount>

     */

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 1) {
            if (strings[1].equalsIgnoreCase("give")) {
                // /mochestshop token give <player>
                if (UsefulString.get("mochestshop.admin").hasPermission(sender)) {
                    if (strings.length >= 3) {
                        String playerName = strings[2];
                        Player player = Bukkit.getPlayer(playerName);
                        if (!UsefulMethods.isOnline(player)) {
                            UsefulString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                            return;
                        }
                        ItemStack token = SlotTokenCreator.createSlotToken();

                        UtilMethods.giveItem(player, token, 1, player.getLocation());

                        UtilString.get(Messages.SLOT_TOKEN_SENDER_MESSAGE).setVariables(player).hex().sendMessage(sender);
                        UtilString.get(Messages.SLOT_TOKEN_RECEIVER_MESSAGE).setVariables(player).hex().sendMessage(player);
                    } else {
                        UsefulString.get("&cUse: /mochestshop token give <player>").hex().setPlaceholders(sender).sendMessage(sender);
                    }
                } else {
                    UsefulString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        } else {
            UsefulString.get("&cUse: /mochestshop token give <player>").hex().setPlaceholders(sender).sendMessage(sender);
        }
    }
}
