package mv.mossuh.mochestshop.API.Events;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerClaimTokenEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Player player;
    private ActiveChestShop active = new ActiveChestShop();
    private int slot = 0;
    public PlayerClaimTokenEvent(Player player, ActiveChestShop active, int slot) {
        this.player = player;
        if (active != null) { this.active = active; }
        this.slot = slot;
    }

    public Player getPlayer() { return player; }
    public ActiveChestShop getActive() { return active; }
    public int getSlot() { return slot; }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
