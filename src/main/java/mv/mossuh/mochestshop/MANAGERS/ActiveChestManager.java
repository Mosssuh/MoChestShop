package mv.mossuh.mochestshop.MANAGERS;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class ActiveChestManager {
    private static final Set<ActiveChestShop> chests = new HashSet<>();

    public Set<ActiveChestShop> getChests() { return chests; }
    public Set<ActiveChestShop> getChests(UUID owner) {
        Iterator<ActiveChestShop> iterator = chests.iterator();
        Set<ActiveChestShop> list = new HashSet<>();
        while (iterator.hasNext()) {
            ActiveChestShop chest = iterator.next();
            if (chest.getChest().getOwner().equals(owner)) {
                list.add(chest);
            }
        }
        return list;
    }

    public ActiveChestShop getChest(Location location) {
        for (ActiveChestShop chest : chests) {
            if (chest.getLocation().equals(location)) {
                return chest;
            }
        }
        return new ActiveChestShop();
    }

    public boolean hasChest(Location location) {
        for (ActiveChestShop chest : chests) {
            if (chest.getLocation().equals(location)) {
                return true;
            }
        }
        return false;
    }

    public static void addChest(ActiveChestShop chest) {
        chests.add(chest);
    }

    public static ActiveChestShop removeChest(UUID uuid) {
        Iterator<ActiveChestShop> iterator = chests.iterator();
        while (iterator.hasNext()) {
            ActiveChestShop chest = iterator.next();
            if (chest.getChest().getUUID().equals(uuid)) {
                iterator.remove();
                return chest;
            }
        }
        return new ActiveChestShop();
    }
}
