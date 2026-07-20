package mv.mossuh.mochestshop.API.Events;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerTradeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private ActiveChestShop active = new ActiveChestShop();
    private Player player = null;
    private TradeType tradeType = TradeType.NONE;
    private Slot tradedSlot = new Slot();
    private int amount = 0;
    private boolean cancelled = false;

    public PlayerTradeEvent(ActiveChestShop active, Player player, TradeType type, Slot tradedSlot, Integer amount) {
        if (active != null) { this.active = active; }
        this.player = player;
        if (type != null) { this.tradeType = type; }
        if (tradedSlot != null) { this.tradedSlot = tradedSlot; }
        if (amount != null) { this.amount = amount; }
    }

    public ActiveChestShop getActive() { return active; }
    public Player getPlayer() { return player; }
    public TradeType getTradeType() { return tradeType; }
    public Slot getTradedSlot() { return tradedSlot; }
    public int getTradedAmount() { return amount; }

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
