package mv.mossuh.mochestshop.EVENTS;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.MANAGERS.ActiveChestManager;
import mv.mossuh.mochestshop.UTILITIES.Chest.ChestCreator;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BreakChestEvent implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void breakChest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getBlock();
        Location loc = block.getLocation();

        ActiveChestShop active = ChestShopAPI.getManagers().getChests().getChest(loc);
        ChestShop chest = active.getChest();
        if (!active.isValid()) return;

        if (!active.getChest().isOwner(uuid)) {
            UtilString.get(Messages.IS_NOT_OWNER).setVariables(chest).hex().sendMessage(player);
            event.setCancelled(true);
            return;
        }

        UtilMethods.inventoryCloser(active.getLocation());

        ItemStack stack = ChestCreator.fromChestShop(chest);
        UtilMethods.giveItem(player, stack, loc);

        if (player.getGameMode() != GameMode.CREATIVE) {
            if (event.isDropItems()) {
                event.setDropItems(false);
            }
        }

        UUID chestUUID = chest.getUUID();
        UtilString.get(Messages.PLAYER_BREAK_CHEST).setVariables(chest).hex().sendMessage(player);
        ActiveChestManager.removeChest(chestUUID);
        ChestShopAPI.getManagers().getHolograms().getHologramHook().remove(active);
    }
}
