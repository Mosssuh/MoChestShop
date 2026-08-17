package mv.mossuh.mochestshop.UTILITIES;

import me.clip.placeholderapi.PlaceholderAPI;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestShopConfig;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.TradeInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mocore.UTILITIES.UsefulString;
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

public class UtilString extends UsefulString<UtilString> {


    protected UtilString(String string) {
        super(string);
    }

    public static UtilString get(String string) {
        return new UtilString(string);
    }

    public UtilString setVariables(Player player) {
        if (isString()) {
            if (player != null && player.isOnline()) {
                List<VariableArg> variables = new ArrayList<>();
                variables.add(new VariableArg("%player%", player.getName()));
                setVariables(variables);
            }
        }
        return this;
    }

    public UtilString setVariables(OfflinePlayer player) {
        if (isString()) {
            if (player != null) {
                List<VariableArg> variables = new ArrayList<>();
                variables.add(new VariableArg("%player%", player.getName()));
                setVariables(variables);
            }
        }
        return this;
    }

    public UtilString setVariables(ChestShopConfig config) {
        if (isString()) {
            setVariables(DefaultVariables.getChestShopConfigVariables(config));
        }
        return this;
    }

    public UtilString setVariables(ChestShop chest) {
        if (isString()) {
            setVariables(DefaultVariables.getChestShopVariables(chest));
        }
        return this;
    }

    public UtilString setVariables(TradeInfo info) {
        List<VariableArg> variables = new ArrayList<>();
        if (isString()) {
            if (info != null) {
                variables.addAll(DefaultVariables.getSlotVariables(info));
            }
        }
        setVariables(variables);
        return this;
    }

    public UtilString setVariables(Slot slot) {
        List<VariableArg> variables = new ArrayList<>();
        if (isString()) {
            if (slot != null && slot.isValid()) {
                variables.addAll(DefaultVariables.getSlotVariables(slot));
            }
        }
        setVariables(variables);
        return this;
    }

    public UtilString setVariables(ItemStack item) {
        if (isString()) {
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

}
