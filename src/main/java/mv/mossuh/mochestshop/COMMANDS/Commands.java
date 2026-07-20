package mv.mossuh.mochestshop.COMMANDS;

import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mocore.UTILITIES.UsefulString;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
    /*

    /mochestshop give <player> <chest> <amount>
    /mochestshop token give <player>
    /mochestshop setslots <slots>

     */

    public static String switchRole = "normal";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (strings.length > 0) {
            switch (strings[0].toLowerCase()) {
                case "give":
                    GiveChestCommands.onCommand(sender,  strings);
                    break;
                case "setslots":
                    SetChestCommands.onCommand(sender, strings);
                    break;
                case "token":
                    SlotTokenCommands.onCommand(sender, strings);
                    break;
                case "roleswitch":
                    switch (switchRole) {
                        case "normal":
                            switchRole = "owner";
                            break;
                        case "owner":
                            switchRole = "player";
                            break;
                        case "player":
                            switchRole = "normal";
                            break;
                    }
                    UsefulString.get(Config.PREFIX + " &aSwitched to " + switchRole).hex().sendMessage(sender);
                    break;
            }
        } else {
            if (UsefulMethods.hasPermission(sender, "mochestshop.admin")) {
                UsefulString.get("&r").hex().sendMessage(sender);
                UsefulString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                UsefulString.get("&r").hex().sendMessage(sender);
                UsefulString.get("&e/mochestshop give <player> <chest> <amount>").hex().sendMessage(sender);
                UsefulString.get("&e/mochestshop token give <player>").hex().sendMessage(sender);
                UsefulString.get("&e/mochestshop setslots <slots> &c# Chest In Hand").hex().sendMessage(sender);
                UsefulString.get("&r").hex().sendMessage(sender);
                UsefulString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                UsefulString.get("&r").hex().sendMessage(sender);
            }
        }
        return false;
    }
}
