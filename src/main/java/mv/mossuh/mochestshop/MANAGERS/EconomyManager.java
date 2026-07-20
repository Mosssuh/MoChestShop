package mv.mossuh.mochestshop.MANAGERS;

import mv.mossuh.mochestshop.ECONOMIES.EconomyHook;
import mv.mossuh.mochestshop.ECONOMIES.Hooks.InvalidHook;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EconomyManager {
    private static final Map<String, EconomyHook> economies = new ConcurrentHashMap<>();

    public static void setEconomy(String id, EconomyHook hook) {
        economies.put(id, hook);
    }

    public boolean hasEconomy(String id) {
        for (String i : economies.keySet()) {
            if (i.equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public EconomyHook getEconomy(String id) {
        for (String i : economies.keySet()) {
            if (i.equalsIgnoreCase(id)) {
                return economies.get(i);
            }
        }
        return new InvalidHook();
    }
}
