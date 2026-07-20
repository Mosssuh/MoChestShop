package mv.mossuh.mochestshop.API.Events;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerOpenShopEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    Player player;
    ActiveChestShop active = new ActiveChestShop();
    private boolean cancelled = false;

    public PlayerOpenShopEvent(Player player, ActiveChestShop active) {
        if (active != null) { this.active = active; }
        this.player = player;
    }

    public ActiveChestShop getActive() { return active; }

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
