package mv.mossuh.mochestshop.CONFIGS;

import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Config {

    private static final Map<String, String> intervalStringMap = new ConcurrentHashMap<>();
    private static final Map<String, Double> intervalDoubleMap = new ConcurrentHashMap<>();
    private static final Map<String, Long> intervalLongMap = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> intervalBooleanMap = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> intervalListStringMap = new ConcurrentHashMap<>();
    private static final List<String> intervalsList = new ArrayList<>(Arrays.asList(
            "ECONOMY.PLUGIN", "ECONOMY.CURRENCY",

            "HOLOGRAM.PLUGIN", "HOLOGRAM.UPDATE_INTERVAL", "HOLOGRAM.MESSAGE",

            "ECONOMIES",

            "MYSQL_ENABLED", "MYSQL.HOST", "MYSQL.PORT", "MYSQL.DATABASE", "MYSQL.USER", "MYSQL.PASSWORD",

            "PREFIXES.BUY", "PREFIXES.SELL", "PREFIXES.OPEN", "PREFIXES.CLOSED", "LIST_ITEMS_FORMAT",
            "SELL_LIST_ITEMS_FORMAT", "BUY_LIST_ITEMS_FORMAT", "NO_ITEMS_FOR_TRADE"
    ));

    public static ServerVersion MIN_VERSION = ServerVersion.MC1_19;

    public static int MAX_DECIMAL = 3;
    public static String ECONOMY_PLUGIN;
    public static String ECONOMY_CURRENCY;
    public static String HOLOGRAM_PLUGIN;
    public static double HOLOGRAM_UPDATE_INTERVAL;
    public static List<String> HOLOGRAM_MESSAGE;
    public static String PREFIX = "&8[&bMoChestShop&8]";
    public static String PLUGIN_NAME = "MoChestShop";
    public static boolean MYSQL_ENABLED;
    public static String MYSQL_HOST;
    public static Integer MYSQL_PORT;
    public static String MYSQL_DATABASE;
    public static String MYSQL_USER;
    public static String MYSQL_PASSWORD;
    public static long DATA_SAVE_INTERVAL;

    public static String BUY_PREFIX;
    public static String SELL_PREFIX;
    public static String OPEN_PREFIX;
    public static String CLOSED_PREFIX;
    public static String BUY_LIST_ITEMS_FORMAT;
    public static String SELL_LIST_ITEMS_FORMAT;
    public static String NO_ITEMS_FOR_TRADE;


    private static void loadConfig() {
        ECONOMY_PLUGIN = new Config("ECONOMY.PLUGIN").getOrDefault("");
        ECONOMY_CURRENCY = new Config("ECONOMY.CURRENCY").getOrDefault("");
        HOLOGRAM_PLUGIN = new Config("HOLOGRAM.PLUGIN").getOrDefault("");
        HOLOGRAM_UPDATE_INTERVAL = new Config("HOLOGRAM.UPDATE_INTERVAL").getOrDefault(1.5);
        HOLOGRAM_MESSAGE = new Config("HOLOGRAM.MESSAGE").getOrDefault(new ArrayList<>());
        MYSQL_ENABLED = new Config("MYSQL_ENABLED").getOrDefault(false);
        MYSQL_HOST = new Config("MYSQL.HOST").getOrDefault("localhost");
        MYSQL_PORT = new Config("MYSQL.PORT").getOrDefault(0);
        MYSQL_DATABASE = new Config("MYSQL.DATABASE").getOrDefault("");
        MYSQL_USER = new Config("MYSQL.USER").getOrDefault("root");
        MYSQL_PASSWORD = new Config("MYSQL.PASSWORD").getOrDefault("");
        DATA_SAVE_INTERVAL = new Config("DATA_SAVE_INTERVAL").getOrDefault(300L);
        BUY_PREFIX = new Config("PREFIXES.BUY").getOrDefault("");
        SELL_PREFIX = new Config("PREFIXES.SELL").getOrDefault("");
        OPEN_PREFIX = new Config("PREFIXES.OPEN").getOrDefault("");
        CLOSED_PREFIX = new Config("PREFIXES.CLOSED").getOrDefault("");
        BUY_LIST_ITEMS_FORMAT = new Config("BUY_LIST_ITEMS_FORMAT").getOrDefault("");
        SELL_LIST_ITEMS_FORMAT = new Config("SELL_LIST_ITEMS_FORMAT").getOrDefault("");
        NO_ITEMS_FOR_TRADE = new Config("NO_ITEMS_FOR_TRADE").getOrDefault("");
    }



    private final String intervalPosition;

    public Config(String intervalPosition) {
        this.intervalPosition = intervalPosition;
    }


    public String getOrDefault(String defaultValue) {
        return intervalStringMap.getOrDefault(intervalPosition, defaultValue);
    }

    public int getOrDefault(int defaultValue) {
        if (intervalLongMap.containsKey(intervalPosition)) {
            return Math.toIntExact(intervalLongMap.get(intervalPosition));
        }
        return defaultValue;
    }

    public double getOrDefault(double defaultValue) {
        if (intervalDoubleMap.containsKey(intervalPosition)) {
            return intervalDoubleMap.get(intervalPosition);
        }
        return defaultValue;
    }

    public long getOrDefault(long defaultValue) {
        if (intervalLongMap.containsKey(intervalPosition)) {
            return intervalLongMap.get(intervalPosition);
        }
        return defaultValue;
    }

    public List<String> getOrDefault(List<String> defaultValue) {
        return intervalListStringMap.getOrDefault(intervalPosition, defaultValue);
    }

    public boolean getOrDefault(boolean defaultValue) {
        return intervalBooleanMap.getOrDefault(intervalPosition, defaultValue);
    }
    public static void load() {
        FileConfiguration mainConfig = Configs.getConfig().getConfig();

        for (String intervalConfig : intervalsList) {
            if (mainConfig.isString(intervalConfig)) {
                intervalStringMap.put(intervalConfig, mainConfig.getString(intervalConfig));
            } else if (mainConfig.isList(intervalConfig)) {
                intervalListStringMap.put(intervalConfig, mainConfig.getStringList(intervalConfig));
            } else if (mainConfig.isBoolean(intervalConfig)) {
                intervalBooleanMap.put(intervalConfig, mainConfig.getBoolean(intervalConfig));
            } else if (mainConfig.isDouble(intervalConfig)) {
                intervalDoubleMap.put(intervalConfig, mainConfig.getDouble(intervalConfig));
            } else if (mainConfig.isLong(intervalConfig) || mainConfig.isInt(intervalConfig)) {
                intervalLongMap.put(intervalConfig, mainConfig.getLong(intervalConfig));
            }
        }

        loadConfig();
    }
}
