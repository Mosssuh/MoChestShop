package mv.mossuh.mochestshop.UTILITIES.Enums;

import java.util.ArrayList;
import java.util.List;

public enum ChestStatus {
    OPEN("Open"),
    CLOSED("Closed"),
    NONE("None");

    private String prefix = "";
    ChestStatus(String prefix) {
        if (prefix != null) { this.prefix = prefix; }
    }

    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) {
        if (prefix != null) { this.prefix = prefix; }
    }

    public static ChestStatus fromString(String s) {
        for (ChestStatus type : ChestStatus.values()) {
            if (type.name().equalsIgnoreCase(s)) {
                return type;
            }
        }
        return ChestStatus.NONE;
    }

    public static List<ChestStatus> getTypes() {
        List<ChestStatus> types = new ArrayList<>();
        for (ChestStatus type : ChestStatus.values()) {
            if (type.equals(ChestStatus.NONE)) continue;
            types.add(type);
        }
        return types;
    }

}
