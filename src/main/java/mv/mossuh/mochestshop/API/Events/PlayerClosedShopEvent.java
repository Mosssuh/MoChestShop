package mv.mossuh.mochestshop.API.Events;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerClosedShopEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    ActiveChestShop activeChestShop = new ActiveChestShop();
    private boolean cancelled = false;

    public PlayerClosedShopEvent(ActiveChestShop active) {
        if (active != null) { this.activeChestShop = active; }
    }

    public ActiveChestShop getActiveChestShop() { return activeChestShop; }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
