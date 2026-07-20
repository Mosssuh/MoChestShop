package mv.mossuh.mochestshop.MANAGERS;

public class ChestShopManagers {
    private static final ActiveChestManager chests = new ActiveChestManager();
    private static final EconomyManager economies = new EconomyManager();
    private static final HologramManager holograms = new HologramManager();

    public ActiveChestManager getChests() { return chests; }
    public EconomyManager getEconomies() { return economies; }
    public HologramManager getHolograms() { return holograms; }
}
