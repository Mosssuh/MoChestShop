package mv.mossuh.mochestshop.UTILITIES.Chest.Collectors;

import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;

public interface TradeInfo {
    TradeType getType();
    boolean hasTraded();
    boolean canSwitch();
}
