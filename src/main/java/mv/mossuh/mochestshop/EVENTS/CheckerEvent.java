package mv.mossuh.mochestshop.EVENTS;

import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.HOLOGRAM.HOLOGRAMS.DecentHologramsHook;
import mv.mossuh.mochestshop.HOLOGRAM.HologramRefreshTask;
import mv.mossuh.mochestshop.MANAGERS.EconomyManager;
import mv.mossuh.mochestshop.ECONOMIES.EconomyHook;
import mv.mossuh.mochestshop.ECONOMIES.Hooks.*;
import mv.mossuh.mochestshop.MANAGERS.HologramManager;
import mv.mossuh.mochestshop.MoChestShop;
import mv.mossuh.mochestshop.UTILITIES.Economies.Economy;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.EVENTS.PluginCheckerEvent;
import mv.mossuh.mocore.UTILITIES.UsefulString;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;

public class CheckerEvent implements Listener {

    @EventHandler
    public void onCheck(PluginCheckerEvent event) {
        PluginType type  = event.getPluginType();

        // Another Checks
        if (otherPlugins(type)) return;

        // Economies Hook
        Set<Economy> economies = Configs.getEconomies(type.name());
        if(economies.isEmpty()) return;

        Map<String, EconomyHook> hooks = new HashMap<>();

        for (Economy e : economies) {
            String id = e.getID();
            String economy = e.getEconomy();
            EconomyHook hook = new InvalidHook();

           if (type.equals(PluginType.Vault)) {
                VaultEconomyHook vh = new VaultEconomyHook();
                RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = MoChestShop.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
                if (rsp == null) {
                    UtilString.get(Config.PREFIX + " &2HOOK! &cError when detecting Registered Economy with ID " + id + " from Vault.")
                            .hex().sendMessageInConsole();
                    return;
                }
                net.milkbowl.vault.economy.Economy econ = rsp.getProvider();
                vh.setProvider(econ);
                hook = vh;
            }

            if (hook.getPlugin().equals("INVALID")) { continue; }

            UtilString.get(Config.PREFIX + " &2HOOK! &aDetected economy with ID " + id + ". Registering...")
                    .hex().sendMessageInConsole();
            hooks.put(id, hook);
        }

        for (String id : hooks.keySet()) {
            EconomyManager.setEconomy(id, hooks.get(id));
        }

        UtilString.get(Config.PREFIX + " &aDetected " + type.name() + ", used as soft-depend. Enabling classes.").hex().sendMessageInConsole();
    }


    public boolean otherPlugins(PluginType type) {
        if (type.equals(PluginType.DecentHolograms)) {
            UsefulString.get("&8[" + Config.PREFIX + "&8] &aDetected "+ type.name() +", used as soft-depend.").hex().sendMessageInConsole();
            if (Config.HOLOGRAM_PLUGIN.equalsIgnoreCase("DecentHolograms")) {
                HologramManager.setHologramHook(new DecentHologramsHook());
                HologramRefreshTask.start();
                UsefulString.get("&8[" + Config.PREFIX + "&8] &aUsing "+ type.name() +" as holograms for ChestShops.").hex().sendMessageInConsole();
            }
            return true;
        }
        return false;
    }

}
