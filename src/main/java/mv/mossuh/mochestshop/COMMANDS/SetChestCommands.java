package mv.mossuh.mochestshop.COMMANDS;

import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.UTILITIES.Chest.ChestCreator;
import mv.mossuh.mochestshop.UTILITIES.Nbt.NBTChest;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulString;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SetChestCommands {
    /*

    /mochestshop setslots <slots>
    /mochestshop seteconomies <economies...>

     */

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 1) {
            if (strings[0].equalsIgnoreCase("setslots")) {
                // /mochestshop setslots <slots>
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    UUID uuid = player.getUniqueId();
                    if (UsefulString.get("mochestshop.admin").hasPermission(sender)) {
                        String slots = strings[1];
                        if (!UsefulString.get(slots).isNumeric()) {
                            UsefulString.get(Messages.INVALID_QUANTITY).hex().sendMessage(sender);
                            return;
                        }

                        ItemStack stack = UtilMethods.getItemInHand(player);
                        ChestShop chest = ChestShop.getChestShop(stack);
                        if (!chest.isValid()) {
                            UsefulString.get(Messages.INVALID_CHEST).hex().sendMessage(sender);
                        }

                        int amount = Integer.parseInt(slots);
                        if (amount > 10) {
                            amount = 10;
                        } else if (amount < 0) {
                            amount = 0;
                        }

                        chest.setUnlockedSlots(amount);
                        NBTChest.setUnlockedSlots(stack, amount);
                        ChestCreator.updateChestLore(uuid, chest, stack);

                        List<VariableArg> variables = new ArrayList<>();
                        variables.add(new VariableArg("%unlocked_slots%", slots));
                        UtilString.get(Messages.SET_SLOTS).setVariables(variables).setVariables(chest).setPlaceholders(player).hex().sendMessage(sender);
                    } else {
                        UsefulString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                    }
                }
            }
        }
    }
}
