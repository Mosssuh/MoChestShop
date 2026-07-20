package mv.mossuh.mochestshop.CHESTS;

import org.bukkit.Location;

public class ActiveChestShop {
    private ChestShop chest = new ChestShop();
    Location location;

    public ActiveChestShop(ChestShop chest, Location location) {
        if (chest != null) { this.chest = chest; }
        this.location = location;
    }

    public ActiveChestShop() {}

    public ChestShop getChest() { return chest; }
    public Location getLocation() { return location; }

    public boolean isValid() {
        return chest.isValid() && location != null;
    }
}
