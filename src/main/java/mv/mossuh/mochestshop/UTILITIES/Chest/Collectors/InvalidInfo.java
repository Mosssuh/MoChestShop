package mv.mossuh.mochestshop.UTILITIES.Chest.Collectors;

import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;

public class InvalidInfo implements TradeInfo {
    private static final TradeType tradeType = TradeType.NONE;

    public InvalidInfo() {}

    @Override
    public TradeType getType() { return tradeType; }

    @Override
    public boolean hasTraded() {
        return false;
    }

    @Override
    public boolean canSwitch() {
        return false;
    }
}
