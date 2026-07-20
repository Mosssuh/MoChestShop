package mv.mossuh.mochestshop.EVENTS;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class ProtectionChestEvent implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBurnChest(BlockBurnEvent event) {
        Location location = event.getBlock().getLocation();

        if (ChestShopAPI.getManagers().getChests().hasChest(location)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onExplodeChest(EntityExplodeEvent event) {
        List<Block> blocks = event.blockList();
        for (Block b : blocks) {
            Location loc = b.getLocation();

            if  (ChestShopAPI.getManagers().getChests().hasChest(loc)) {
                event.setCancelled(true);
            }
        }
    }
}
