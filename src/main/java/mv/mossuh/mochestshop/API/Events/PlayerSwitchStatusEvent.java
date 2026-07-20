package mv.mossuh.mochestshop.API.Events;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.UTILITIES.Enums.ChestStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSwitchStatusEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    Player player;
    ActiveChestShop active = new ActiveChestShop();
    ChestStatus status = ChestStatus.NONE;
    private boolean cancelled = false;

    public PlayerSwitchStatusEvent(Player player, ActiveChestShop active, ChestStatus status) {
        this.player = player;
        if (active != null) { this.active = active; }
        if (status != null) { this.status = status; }
    }

    public Player getPlayer() { return player; }
    public ActiveChestShop getActive() { return active; }
    public ChestStatus getStatus() { return status; }

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
