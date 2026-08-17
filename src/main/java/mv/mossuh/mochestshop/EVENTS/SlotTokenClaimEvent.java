package mv.mossuh.mochestshop.EVENTS;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import mv.mossuh.mochestshop.API.Events.PlayerClaimTokenEvent;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.UTILITIES.Nbt.NBTSlotToken;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.EVENTS.ItemSelectEvent.ItemSelectEvent;
import mv.mossuh.mocore.GUI.Holders.SingleGUI;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SlotTokenClaimEvent implements Listener {

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
        ChestShop chest = active.getChest();

        if (!NBTSlotToken.isSlotToken(selected)) {
            return;
        }

        String COOLDOWN_CODE = Config.PLUGIN_NAME+"SelectSlotToken-Cooldown"+chest.getUUID();
        long COOLDOWN = 2;
        if (Cooldown.startAndIsOnCooldown(COOLDOWN_CODE, COOLDOWN)) {
            List<VariableArg> cooldownVariables = new ArrayList<>();
            cooldownVariables.add(new VariableArg("%cooldown%", Cooldown.showCooldownFormatted(COOLDOWN_CODE, COOLDOWN, UtilMethods.getTimeFormat())));
            UtilString.get(Messages.COOLDOWN).setVariables(cooldownVariables)
                    .setVariables(chest).hex().sendMessage(player);
            player.closeInventory();
            return;
        }

        int slots = chest.getUnlockedSlots();
        if (slots == 10) {
            UtilString.get(Messages.SLOT_TOKEN_MAX_UNLOCKED).setVariables(chest).hex().sendMessage(player);
            player.closeInventory();
            return;
        }

        int newSlot = slots + 1;
        PlayerClaimTokenEvent claimEvent = new PlayerClaimTokenEvent(player, active, newSlot);
        Bukkit.getServer().getPluginManager().callEvent(claimEvent);
        if (claimEvent.isCancelled()) return;

        UtilMethods.removeItem(player, selected, 1);
        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%slot%", newSlot+""));

        chest.setUnlockedSlots(newSlot);
        player.closeInventory();
        UtilString.get(Messages.SLOT_TOKEN_CLAIMED).setVariables(variables).setVariables(chest).hex().sendMessage(player);
    }
}
