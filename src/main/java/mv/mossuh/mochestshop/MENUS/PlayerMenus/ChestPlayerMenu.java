package mv.mossuh.mochestshop.MENUS.PlayerMenus;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.MENUS.GUIItems.OwnerItems;
import mv.mossuh.mochestshop.MENUS.GUIItems.PlayerItems;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.GUI.Holders.SingleGUI;
import mv.mossuh.mocore.GUI.Items.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChestPlayerMenu {

    public static void open(UUID uuid, ActiveChestShop active) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        ChestShop chest = active.getChest();
        Location location = active.getLocation();

        UUID ownerUUID = chest.getOwner();
        OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUUID);

        String guiName = UtilString.get(Configs.getGuiNameConfig()).setPlaceholders(owner).setDefaultPlayerVariables(owner).hex().apply();
        SingleGUI gui = new SingleGUI(6, guiName, location);
        gui.onGUIClick((e) -> {
            e.setCancelled(true);
        });

        GUIItem white = OwnerItems.WhitePanelItem();
        gui.setItem(white, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 51, 52, 53);
        GUIItem gray = OwnerItems.GrayPanelItem();
        gui.setItem(gray, 10, 11, 12, 13, 14, 15, 16, 19, 25, 28, 34, 37, 38, 39, 40, 41, 42, 43);

        int[] slotsPosition = UtilMethods.getChestInvPositions();

        int slotPos = 1;
        for (int pos : slotsPosition) {
            Slot slot = chest.getSlot(slotPos);
            if (slot.isValid()) {
                if (slot.hasItem()) {
                    GUIItem item = PlayerItems.SlotItem(gui, active, slot);
                    gui.setItem(pos, item);
                } else {
                    GUIItem item = PlayerItems.NoItemInTradeItem();
                    gui.setItem(pos, item);
                }
            } else {
                GUIItem item = PlayerItems.NoItemInTradeItem();
                gui.setItem(pos, item);
            }
            slotPos += 1;
        }

        GUIItem close = OwnerItems.CloseItem(player);
        gui.setItem(49, close);

        player.openInventory(gui.getInventory());
    }
}
