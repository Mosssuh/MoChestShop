package mv.mossuh.mochestshop.EVENTS;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import mv.mossuh.mochestshop.API.Events.PlayerSelectItemEvent;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.MENUS.GUIItems.OwnerItems;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.TradeInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.Nbt.NBTSlotToken;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.EVENTS.ItemSelectEvent.ItemSelectEvent;
import mv.mossuh.mocore.GUI.Holders.SingleGUI;
import mv.mossuh.mocore.GUI.Items.GUIItem;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

public class SelectItemEvent implements Listener {

    @EventHandler
    public void selectItem(ItemSelectEvent event) {
        Player player = event.getPlayer();
        ItemStack selected = event.getSelectedItem();
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder == null) return;
        if (selected == null || selected.getType().equals(Material.AIR)) return;

        InventoryView view = player.getOpenInventory();
        InventoryHolder chestHolder = view.getTopInventory().getHolder();
        InventoryHolder playerHolder = view.getBottomInventory().getHolder();

        if (chestHolder == null || playerHolder == null) return;

        if (!(chestHolder instanceof SingleGUI)) return;
        if (!(playerHolder instanceof Player)) return;

        if (!(holder instanceof Player)) return;
        event.setCancelled(true);

        SingleGUI gui = (SingleGUI) chestHolder;
        Location location = gui.getLocation();

        ActiveChestShop active = ChestShopAPI.getManagers().getChests().getChest(location);
        if (!active.isValid()) return;
        if (NBTSlotToken.isSlotToken(selected)) return;
        ChestShop chest = active.getChest();

        String COOLDOWN_CODE = Config.PLUGIN_NAME+"SelectItem-Cooldown"+chest.getUUID();
        long COOLDOWN = 2;
        if (Cooldown.startAndIsOnCooldown(COOLDOWN_CODE, COOLDOWN)) {
            List<VariableArg> cooldownVariables = new ArrayList<>();
            cooldownVariables.add(new VariableArg("%cooldown%", Cooldown.showCooldownFormatted(COOLDOWN_CODE, COOLDOWN, UtilMethods.getTimeFormat())));
            UtilString.get(Messages.COOLDOWN).setVariables(cooldownVariables)
                    .setVariables(chest).hex().sendMessage(player);
            player.closeInventory();
            return;
        }

        if (!chest.hasEmptySlot()) {
            UtilString.get(Messages.ITEM_TAKEN_SLOTS).setVariables(chest).hex().sendMessage(player);
            player.closeInventory();
            return;
        }

        if (ChestShop.isChestShop(selected)) {
            UtilString.get(Messages.DONT_TRADE_ITEM).setVariables(chest).hex().sendMessage(player);
            player.closeInventory();
            return;
        }

        if (chest.isInShop(selected)) {
            UtilString.get(Messages.ITEM_ALREADY_ADDED).setVariables(chest).hex().sendMessage(player);
            player.closeInventory();
            return;
        }

        int slotPosition = chest.getEmptySlot();
        int invPosition = UtilMethods.getInvPosition(slotPosition);

        PlayerSelectItemEvent selectEvent = new PlayerSelectItemEvent(player, active, slotPosition, selected);
        Bukkit.getServer().getPluginManager().callEvent(selectEvent);
        if (selectEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        TradeInfo info = ChestShop.getDefaultTradeInfo(chest);
        ItemStack clone = selected.clone();
        clone.setAmount(1);
        Slot slot = new Slot(slotPosition, clone, info);
        chest.addSlot(slot);

        GUIItem item = OwnerItems.SelectedSlotItem(gui, active, slot);
        gui.setItem(item, invPosition);
        gui.refreshGUI(player);

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%slot%", slotPosition+""));
        UtilString.get(Messages.SLOT_ITEM_SELECTED).setVariables(variables)
                .setVariables(chest).hex().sendMessage(player);
    }
}
