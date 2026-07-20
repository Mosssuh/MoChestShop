package mv.mossuh.mochestshop.UTILITIES.Chest.Collectors;

import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;

public class SellingInfo implements TradeInfo {
    private static final TradeType tradeType = TradeType.SELL;
    private double sellingPrice = 0;
    private int soldItems = 0;
    private int saleItems = 0;

    public SellingInfo(Double sellingPrice, Integer saleItems, Integer soldItems) {
        if (soldItems != null) { this.soldItems = soldItems; }
        if (sellingPrice != null) {
            this.sellingPrice = Math.max(sellingPrice, 0);
        }
        if (saleItems != null) { this.saleItems = saleItems; }
    }

    public SellingInfo() {}

    @Override
    public TradeType getType() { return tradeType; }
    @Override
    public synchronized boolean hasTraded() {
        return soldItems > 0;
    }

    @Override
    public synchronized boolean canSwitch() {
        return soldItems <= 0 && saleItems <= 0;
    }

    public synchronized int getSoldItems() { return soldItems; }
    public synchronized void setSoldItems(int amount) { this.soldItems = amount; }
    public synchronized void addSoldItems(int amount) {
        this.soldItems += amount;
    }


    public synchronized void setSellingPrice(double price) {
        this.sellingPrice = price;
    }
    public synchronized double getSellingPrice() { return sellingPrice; }

    public synchronized int getItemsForSale() { return saleItems; }
    public synchronized void setItemsForSale(int amount) { this.saleItems = amount; }
    public synchronized void addItemsForSale(int amount) { this.saleItems += amount; }
    public synchronized void removeItemsForSale(int amount) {
        int a = this.saleItems - amount;
        if (a <= 0) {
            this.saleItems = 0;
            return;
        }
        this.saleItems = a;
    }
}
