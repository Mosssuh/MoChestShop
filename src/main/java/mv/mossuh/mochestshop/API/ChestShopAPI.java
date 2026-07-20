package mv.mossuh.mochestshop.API;

import mv.mossuh.mochestshop.MANAGERS.ChestShopManagers;

public class ChestShopAPI {
    private static final ChestShopManagers managers = new ChestShopManagers();

    public static ChestShopManagers getManagers() { return managers; }
}
