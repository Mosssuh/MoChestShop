package mv.mossuh.mochestshop.UTILITIES.Chest.Collectors;

import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;

public class PurchasingInfo implements TradeInfo
{
    private static final TradeType tradeType = TradeType.BUY;
    private double purchasingPrice = 0;
    private int purchasedItems = 0;

    public PurchasingInfo(Double purchasingPrice, Integer purchasedItems) {
        if (purchasedItems != null) { this.purchasedItems = purchasedItems; }
        if (purchasingPrice != null) {
            this.purchasingPrice = Math.max(purchasingPrice, 0);
        }
    }
    public PurchasingInfo() {
    }

    @Override
    public TradeType getType() { return tradeType; }
    @Override
    public synchronized boolean hasTraded() {
        return purchasedItems > 0;
    }

    @Override
    public synchronized boolean canSwitch() {
        return purchasedItems <= 0;
    }

    public synchronized int getPurchasedItems() { return purchasedItems; }
    public synchronized void addPurchasedItems(int amount) {
        this.purchasedItems += amount;
    }
    public synchronized void removePurchasedItems(int amount) {
        this.purchasedItems -= amount;
    }

    public synchronized void setPurchasingPrice(Double price) {
        this.purchasingPrice = Math.max(price, 0);
    }
    public synchronized double getPurchasingPrice() { return purchasingPrice; }
    public synchronized void setPurchasedItems(int amount) { this.purchasedItems = amount; }

}
