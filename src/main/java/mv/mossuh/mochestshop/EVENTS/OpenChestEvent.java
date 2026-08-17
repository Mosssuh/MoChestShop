package mv.mossuh.mochestshop.EVENTS;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import mv.mossuh.mochestshop.API.Events.PlayerOpenShopEvent;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.COMMANDS.Commands;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.MENUS.PlayerMenus.ChestPlayerMenu;
import mv.mossuh.mochestshop.MENUS.OwnerMenus.ChestConfigurationMenu;
import mv.mossuh.mochestshop.UTILITIES.Enums.ChestStatus;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class OpenChestEvent implements Listener {

    @EventHandler
    public void openChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block clicked = event.getClickedBlock();
        if (clicked == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Location location = clicked.getLocation();
        ActiveChestShop activeChest = ChestShopAPI.getManagers().getChests().getChest(location);
        if (!activeChest.isValid()) return;
        event.setCancelled(true);

        String switchRole = Commands.switchRole;

        ChestShop chest = activeChest.getChest();
        UUID owner = chest.getOwner();

        switch (switchRole) {
            case "player":
                if (chest.getStatus().equals(ChestStatus.CLOSED)) {
                    UtilString.get(Messages.CHEST_SHOP_CLOSED).setVariables(chest).setVariables(player).setPlaceholders(owner)
                            .hex().sendMessage(player);
                    return;
                }

                if (callOpenShopEvent(player, activeChest)) return;

                ChestPlayerMenu.open(uuid, activeChest);
                return;
            case "owner":
                if (callOpenShopEvent(player, activeChest)) return;

                ChestConfigurationMenu.open(uuid, activeChest);
                return;
            default:
                break;
        }

        if (uuid.equals(owner)) {
            if (callOpenShopEvent(player, activeChest)) return;

            ChestConfigurationMenu.open(uuid, activeChest);
        } else {
            if (chest.getStatus().equals(ChestStatus.CLOSED)) {
                UtilString.get(Messages.CHEST_SHOP_CLOSED).setVariables(chest).setVariables(player).setPlaceholders(owner)
                        .hex().sendMessage(player);
                return;
            }
            if (callOpenShopEvent(player, activeChest)) return;

            ChestPlayerMenu.open(uuid, activeChest);
        }

    }

    public boolean callOpenShopEvent(Player player, ActiveChestShop active) {
        PlayerOpenShopEvent openShopEvent = new PlayerOpenShopEvent(player, active);
        Bukkit.getServer().getPluginManager().callEvent(openShopEvent);
        return openShopEvent.isCancelled();
    }
}
