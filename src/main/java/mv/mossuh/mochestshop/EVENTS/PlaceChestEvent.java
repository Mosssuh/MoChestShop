package mv.mossuh.mochestshop.EVENTS;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.HOLOGRAM.HologramHook;
import mv.mossuh.mochestshop.MANAGERS.ActiveChestManager;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class PlaceChestEvent implements Listener {

    @EventHandler
    public void place(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        ChestShop chest = ChestShop.getChestShop(item);
        if (!chest.isValid()) return;
        Block placed = event.getBlock();
        Location location = placed.getLocation();

        for (BlockFace face : Arrays.asList(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST)) {
            Block neighbor = placed.getRelative(face);
            if (neighbor.getType() == Material.CHEST) {
                event.setCancelled(true);
                UtilString.get(Messages.IS_NEXT_CHEST).hex().sendMessage(player);
                return;
            }
        }

        ActiveChestShop active = new ActiveChestShop(chest, location);
        ActiveChestManager.addChest(active);

        HologramHook hologram = ChestShopAPI.getManagers().getHolograms().getHologramHook();
        if (hologram != null) {
            hologram.spawn(active);
        }
        UtilString.get(Messages.PLAYER_PLACE_CHEST).setVariables(chest).setPlaceholders(player).hex().sendMessage(player);
    }
}
