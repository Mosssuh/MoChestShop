package mv.mossuh.mochestshop.UTILITIES.Enums;

import java.util.ArrayList;
import java.util.List;

public enum TradeType {
    BUY("Buy"),
    SELL("Sell"),
    NONE("None");

    private String prefix = "";
    TradeType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public static TradeType fromString(String s) {
        for (TradeType type : TradeType.values()) {
            if (type.name().equalsIgnoreCase(s)) {
                return type;
            }
        }
        return TradeType.NONE;
    }

    public static List<TradeType> getTypes() {
        List<TradeType> types = new ArrayList<>();
        for (TradeType type : TradeType.values()) {
            if (type.equals(TradeType.NONE)) continue;
            types.add(type);
        }
        return types;
    }
}
