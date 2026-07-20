package mv.mossuh.mochestshop.UTILITIES;

import me.clip.placeholderapi.PlaceholderAPI;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestShopConfig;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.TradeInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilString {

    private String string;
    public UtilString(String string) {
        this.string = string;
    }

    public static UtilString get(String string) {
        return new UtilString(string);
    }

    public UtilString hex() {
        if (string != null && !string.isEmpty()) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                String hexCode = string.substring(matcher.start(), matcher.end());
                String replaceSharp = hexCode.replace('#', 'x');

                char[] ch = replaceSharp.toCharArray();
                StringBuilder builder = new StringBuilder("");
                for (char c : ch) {
                    builder.append("&" + c);
                }

                string = string.replace(hexCode, builder.toString());
                matcher = pattern.matcher(string);
            }
            string = ChatColor.translateAlternateColorCodes('&', string);
        }
        return this;
    }

    public UtilString replaceString(String target, String replacement) {
        if (string != null && !string.isEmpty()) {
            string = string.replace(target, replacement);
        }
        return this;
    }

    public UtilString setPlaceholders(Player player) {
        if (string != null && !string.isEmpty()) {
            if (player != null && player.isOnline()) {
                string = PlaceholderAPI.setPlaceholders(player, string);
            }
        }
        return this;
    }

    public UtilString setPlaceholders(CommandSender sender) {
        if (string != null && !string.isEmpty()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                string = PlaceholderAPI.setPlaceholders(player, string);
            }
        }
        return this;
    }

    public UtilString setPlaceholders(OfflinePlayer player) {
        if (string != null && !string.isEmpty()) {
            if (player != null) {
                string = PlaceholderAPI.setPlaceholders(player, string);
            }
        }
        return this;
    }

    public UtilString setPlaceholders(UUID uuid) {
        if (string != null && !string.isEmpty()) {
            if (uuid != null) {
                if (Bukkit.getPlayer(uuid) != null) {
                    Player player = Bukkit.getPlayer(uuid);
                    string = PlaceholderAPI.setPlaceholders(player, string);
                } else {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    string = PlaceholderAPI.setPlaceholders(player, string);
                }
            }
        }
        return this;
    }

    public UtilString removeColors() {
        if (string != null && !string.isEmpty()) {
            string = string.replace('&', '§');
            string = ChatColor.stripColor(string);
        }
        return this;
    }


    public boolean evaluateString() {
        if (string != null && !string.isEmpty()) {
            return UsefulMethods.evaluateString(string);
        }
        return false;
    }
    public boolean isNumeric() {
        if (string != null && !string.isEmpty()) {
            try {
                Double.parseDouble(string);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public boolean hasPermission(CommandSender sender) {
        if (string != null && !string.isEmpty()) {
            if (sender != null) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    return player.hasPermission(string);
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPermission(Player player) {
        if (string != null && !string.isEmpty()) {
            if (player != null) {
                return player.hasPermission(string);
            }
        }
        return false;
    }

    public boolean hasPermission(UUID uuid) {
        if (string != null && !string.isEmpty()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                return player.hasPermission(string);
            }
        }
        return false;
    }


    public UtilString setVariables(List<VariableArg> variables) {
        if (string != null && !string.isEmpty() && !variables.isEmpty()) {
            for (VariableArg variable : variables) {
                if (variable.isVariable() && variable.isValue()) {
                    String variableString = variable.getVariable();
                    String valueString = variable.getValue();
                    if (string.contains(variableString)) {
                        string = string.replace(variableString, valueString);
                    }
                }
            }
        }
        return this;
    }

    public UtilString setDefaultPlayerVariables(Player player) {
        if (string != null && !string.isEmpty()) {
            if (player != null && player.isOnline()) {
                string = string.replace("%player%", player.getName());
            }
        }
        return this;
    }

    public UtilString setDefaultPlayerVariables(OfflinePlayer player) {
        if (string != null && !string.isEmpty()) {
            if (player != null) {
                string = string.replace("%player%", Objects.requireNonNull(player.getName()));
            }
        }
        return this;
    }

    public UtilString setDefaultNumberRandomVariable() {
        if (string != null && !string.isEmpty()) {
            if (string.contains("{random_")) {
                Pattern pattern = Pattern.compile("\\{random_(\\d+)-(\\d+)}");
                Matcher matcher = pattern.matcher(string);
                if (matcher.find()) {
                    double min = Integer.parseInt(matcher.group(1));
                    double max = Integer.parseInt(matcher.group(2));
                    string = string.replace(matcher.group(0), UsefulMethods.random(min, max) + "");
                }
            }
        }
        return this;
    }

    public UtilString setMathPlaceholder() {
        string = UsefulMethods.setMathPlaceholder(string);
        return this;
    }

    public UtilString setChangeOutputPlaceholder() {
        string = UsefulMethods.setChangeOutputPlaceholder(string);
        return this;
    }

    public UtilString setArgs(CommandArgs commandArgs) {
        if (string != null && !string.isEmpty()) {
            if (commandArgs.hasArgs()) {
                List<String> args = commandArgs.getArgs();
                for (int i = 0; i < args.size(); i++) {
                    string = string.replace("%args_" + (i+1) + "%", args.get(i));
                }
            }
        }
        return this;
    }


    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public UtilString setChestShopConfig(ChestShopConfig config) {
        if (string != null && !string.isEmpty()) {
            setVariables(UtilMethods.getChestShopConfigVariables(config));
        }
        return this;
    }

    public UtilString setChestShop(ChestShop chest) {
        if (string != null && !string.isEmpty()) {
            setVariables(UtilMethods.getChestShopVariables(chest));
        }
        return this;
    }

    public UtilString setTradeInfo(TradeInfo info) {
        List<VariableArg> variables = new ArrayList<>();
        if (string != null && !string.isEmpty()) {
            if (info != null) {
                variables.addAll(UtilMethods.getSlotVariables(info));
            }
        }
        setVariables(variables);
        return this;
    }

    public UtilString setSlot(Slot slot) {
        List<VariableArg> variables = new ArrayList<>();
        if (string != null && !string.isEmpty()) {
            if (slot != null && slot.isValid()) {
                variables.addAll(UtilMethods.getSlotVariables(slot));
            }
        }
        setVariables(variables);
        return this;
    }

    public UtilString setItemStack(ItemStack item) {
        if (string != null && !string.isEmpty()) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return this;

            List<VariableArg> variables = new ArrayList<>();
            variables.add(new VariableArg("%item_name%", meta.getDisplayName()));
            if (meta.getLore() != null && meta.hasLore()) {
                List<String> lore = meta.getLore();
                variables.add(new VariableArg("%item_lore%", meta.getLore().toString()));

                int pos = 1;
                for (String l : lore) {
                    variables.add(new VariableArg("%item_lore_" + pos + "%", l));
                }
            }

            setVariables(variables);
        }
        return this;
    }


    public String apply() {
        return string;
    }

    public void sendMessage(CommandSender sender) {
        if (string != null && !string.isEmpty()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(string);
            } else {
                Bukkit.getConsoleSender().sendMessage(string);
            }
        }
    }

    public void sendMessage(Player player) {
        if (string != null && !string.isEmpty()) {
            if (player != null && player.isOnline()) {
                player.sendMessage(string);
            }
        }
    }

    public void sendMessage(UUID uuid) {
        if (string != null && !string.isEmpty()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.sendMessage(string);
            }
        }
    }

    public void sendMessageInConsole() {
        if (string != null && !string.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(string);
        }
    }

    public void sendMessageToOnlinePlayers() {
        if (string != null && !string.isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(string);
            }
        }
    }

}
