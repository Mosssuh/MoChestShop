package mv.mossuh.mochestshop;

import mv.mossuh.mochestshop.API.ChestShopAPI;
import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.COMMANDS.Commands;
import mv.mossuh.mochestshop.COMMANDS.TabCompleter;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.CONFIGS.Messages;
import mv.mossuh.mochestshop.DATABASE.ChestShopDatabase;
import mv.mossuh.mochestshop.ECONOMIES.Hooks.MinecraftExperienceHook;
import mv.mossuh.mochestshop.EVENTS.*;
import mv.mossuh.mochestshop.MANAGERS.EconomyManager;
import mv.mossuh.mochestshop.UTILITIES.Economies.Economy;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import mv.mossuh.mocore.DATABASE.ConnectionMySQL;
import mv.mossuh.mocore.UTILITIES.UsefulString;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public final class MoChestShop extends JavaPlugin {

    private static MoChestShop instance;
    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();

    private ConnectionMySQL connection;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Configs.configureConfigs();
        Messages.load();

        if (Config.MYSQL_ENABLED) {
            this.connection = new ConnectionMySQL(Config.MYSQL_HOST, Config.MYSQL_PORT, Config.MYSQL_DATABASE, Config.MYSQL_USER, Config.MYSQL_PASSWORD);
        }

        registerOthers();
        registerEvents();
        registerCommands();

        ChestShopDatabase.registerDatabaseInCache();
        registerTasks();
        UsefulString.get(Config.PREFIX + " &aHas been enabled. Created by &bMossuh&a.").hex().sendMessageInConsole();
        UsefulString.get(Config.PREFIX + " &aVersion: " + version).hex().sendMessageInConsole();
    }

    @Override
    public void onDisable() {
        stopTasks();
        saveChests();

        UsefulString.get(Config.PREFIX + " &aHas been disabled. Created by &bMossuh&a.").hex().sendMessageInConsole();
        UsefulString.get(Config.PREFIX + " &aVersion: " + version).hex().sendMessageInConsole();
    }

    public static MoChestShop getInstance() { return instance; }

    public Connection getConnection() {
        if (Config.MYSQL_ENABLED) {
            return connection.getConnection();
        } else {
            try {
                return DriverManager.getConnection("jdbc:sqlite:" + Configs.getChestsData().getFile().getAbsolutePath());
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new OpenChestEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlaceChestEvent(), this);
        this.getServer().getPluginManager().registerEvents(new BreakChestEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ProtectionChestEvent(), this);
        this.getServer().getPluginManager().registerEvents(new SelectItemEvent(), this);
        this.getServer().getPluginManager().registerEvents(new SlotTokenClaimEvent(), this);
    }

    public void registerOthers() {
        this.getServer().getPluginManager().registerEvents(new CheckerEvent(), this);

        Set<Economy> economies = Configs.getEconomies("Minecraft");
        for (Economy e : economies) {
            String id = e.getID();
            String economy = e.getEconomy();
            boolean registered = false;

            if (economy.equalsIgnoreCase("experience")) {
                EconomyManager.setEconomy(id, new MinecraftExperienceHook());
                registered = true;
            }

            if (!registered) continue;
            UtilString.get(Config.PREFIX + " &2HOOK! &aDetected economy with ID " + id + ". Registering...")
                    .hex().sendMessageInConsole();
        }
    }



    private BukkitTask vacuumTask;
    private static Timer saveChestsTimer;

    public  void registerTasks() {
        long delayVacuum = 20L * 60 * 60;
        long delaySaver = Config.DATA_SAVE_INTERVAL * 1000;
        vacuumTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, ChestShopDatabase::executeVacuum, delayVacuum, delayVacuum);
        saveChestsTimer = new Timer();
        saveChestsTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                saveChests();
            }
        }, delaySaver, delaySaver);
    }

    public void stopTasks() {
        vacuumTask.cancel();
        saveChestsTimer.cancel();
    }

    public void saveChests() {
        Set<ActiveChestShop> snapshot = new HashSet<>(ChestShopAPI.getManagers().getChests().getChests());
        ChestShopDatabase.registerCacheInDatabase(snapshot);
        UtilString.get(Config.PREFIX + " &2[DATABASE] &aSaved ChestShops in database!").hex().sendMessageInConsole();
    }


    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("mochestshop")).setExecutor(new Commands());
        Objects.requireNonNull(this.getCommand("mochestshop")).setTabCompleter(new TabCompleter());
    }
}
