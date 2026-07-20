package mv.mossuh.mochestshop.API.Events;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSwitchEconomyEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    Player player;
    ActiveChestShop active = new ActiveChestShop();
    String economyID;
    private boolean cancelled = false;

    public PlayerSwitchEconomyEvent(Player player, ActiveChestShop active, String economy) {
        this.player = player;
        if (active != null) { this.active = active; }
        this.economyID = economy;
    }

    public Player getPlayer() { return player; }
    public ActiveChestShop getActive() { return active; }
    public String getEconomyID() { return economyID; }

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
