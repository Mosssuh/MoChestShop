package mv.mossuh.mochestshop.API.Events;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSwitchTradeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    Player player;
    ActiveChestShop active = new ActiveChestShop();
    TradeType type = TradeType.NONE;
    private boolean cancelled = false;

    public PlayerSwitchTradeEvent(Player player, ActiveChestShop active, TradeType type) {
        this.player = player;
        if (active != null) { this.active = active; }
        if (type != null) { this.type = type; }
    }

    public Player getPlayer() { return player; }
    public ActiveChestShop getActive() { return active; }
    public TradeType getType() { return type; }

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
