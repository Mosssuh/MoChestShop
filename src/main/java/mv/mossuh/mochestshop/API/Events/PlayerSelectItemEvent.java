package mv.mossuh.mochestshop.API.Events;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerSelectItemEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Player player;
    private ActiveChestShop active = new ActiveChestShop();
    private int slot = 0;
    private ItemStack selected;
    public PlayerSelectItemEvent(Player player, ActiveChestShop active, int slot, ItemStack selected) {
        this.player = player;
        if (active != null) { this.active = active; }
        this.slot = slot;
        this.selected = selected;
    }

    public Player getPlayer() { return player; }
    public ActiveChestShop getActive() { return active; }
    public int getSlot() { return slot; }
    public ItemStack getSelected() { return selected; }

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
