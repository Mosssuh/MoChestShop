package mv.mossuh.mochestshop.HOLOGRAM.HOLOGRAMS;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.HOLOGRAM.HologramHook;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DecentHologramsHook implements HologramHook {
    @Override
    public String getPlugin() {
        return "DecentHolograms";
    }

    @Override
    public void refresh(ActiveChestShop active) {
        if (!active.isValid()) return;
        Location location = active.getLocation();

        Location newLocation = getHologramLocation(location);
        String name = Config.PLUGIN_NAME + "=" + newLocation.getWorld().getName() + "=" + newLocation.getBlockX() + "=" + newLocation.getBlockY() + "=" + newLocation.getBlockZ();

        Hologram hologram = DHAPI.getHologram(name);
        if (hologram == null) {
            spawn(active);
            return;
        }

        ChestShop chest = active.getChest();
        UUID owner = chest.getOwner();

        List<String> text = Config.HOLOGRAM_MESSAGE;
        List<String> newText = new ArrayList<>();
        for (String line : text) {
            if (line.contains("%chest_items_list%")) {
                newText.addAll(UtilMethods.getListSlotFormat(owner, chest.getSlots()));
            } else {
                String l = UtilString.get(line).setChestShop(chest).setPlaceholders(owner).hex().apply();
                newText.add(l);
            }
        }

        HologramPage page = hologram.getPage(0);

        if (page == null) return;

        List<HologramLine> lines = page.getLines();
        for (int i = 0; i < lines.size(); i++) {
            HologramLine line = lines.get(i);
            line.setText(newText.get(i));
        }

        DHAPI.updateHologram(name);
    }

    @Override
    public void spawn(ActiveChestShop active) {
        if (!active.isValid()) return;
        Location location = active.getLocation();
        Location newLocation = getHologramLocation(location);

        String name = Config.PLUGIN_NAME + "=" + newLocation.getWorld().getName() + "=" + newLocation.getBlockX() + "=" + newLocation.getBlockY() + "=" + newLocation.getBlockZ();
        if (DHAPI.getHologram(name) != null) {
            return;
        }

        ChestShop chest = active.getChest();
        UUID owner = chest.getOwner();

        List<String> text =  Config.HOLOGRAM_MESSAGE;
        List<String> newText = new ArrayList<>();
        for (String line : text) {
            if (line.contains("%chest_items_list%")) {
                newText.addAll(UtilMethods.getListSlotFormat(owner, chest.getSlots()));
            } else {
                String l = UtilString.get(line).setChestShop(chest).setPlaceholders(owner).hex().apply();
                newText.add(l);
            }
        }

        DHAPI.createHologram(name, newLocation, newText);
    }

    @Override
    public void remove(ActiveChestShop active) {
        if (!active.isValid()) return;
        Location location = active.getLocation();
        Location newLocation = getHologramLocation(location);

        String name = Config.PLUGIN_NAME + "=" + newLocation.getWorld().getName() + "=" + newLocation.getBlockX() + "=" + newLocation.getBlockY() + "=" + newLocation.getBlockZ();
        DHAPI.removeHologram(name);
    }

    private Location getHologramLocation(Location location) {
        return new Location(location.getWorld(), location.getX()+0.5, location.getY()+1.5, location.getZ()+0.5);
    }
}
