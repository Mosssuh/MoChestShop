package mv.mossuh.mochestshop.CONFIGS;

import mv.mossuh.mochestshop.UTILITIES.UtilString;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class Messages {

    // private static final Map<String, String> STRING_MESSAGES = new ConcurrentHashMap<>();
    // private static final Map<String, List<String>> LIST_MESSAGES = new ConcurrentHashMap<>();

    // ===== STRINGS =====
    public static String NO_PERMISSION;
    public static String INVALID_PLAYER;
    public static String INVALID_CHEST;
    public static String INVALID_QUANTITY;
    public static String INVALID_PRICE;

    public static String COOLDOWN;
    public static String MODIFIED_CHEST;
    public static String CLOSED_CHEST;
    public static String IS_NOT_OWNER;
    public static String IS_NEXT_CHEST;
    public static String CHEST_SHOP_CLOSED;
    public static String DONT_TRADE_ITEM;

    public static String GIVE_CHEST_SENDER;
    public static String GIVE_CHEST_RECEIVER;

    public static String PLAYER_PLACE_CHEST;
    public static String PLAYER_BREAK_CHEST;

    public static String SET_SLOTS;

    public static String CHEST_REMOVED_ITEM;
    public static String SLOT_NO_ITEMS_COLLECT;
    public static String SLOT_COLLECT_ITEMS;
    public static String SLOT_NO_COINS_COLLECT;
    public static String SLOT_COLLECT_COINS;
    public static String COLLECT_ITEMS_TO_CHANGE;
    public static String COLLECT_COINS_TO_CHANGE;

    public static String ITEM_ALREADY_ADDED;
    public static String ITEM_TAKEN_SLOTS;
    public static String SLOT_ITEM_SELECTED;

    public static String COLLECT_ITEMS_NOT_ENOUGH_SPACE;

    public static String PURCHASING_CLIENT_NO_ENOUGH_ITEMS;
    public static String PURCHASING_OWNER_NO_ENOUGH_COINS;
    public static String PURCHASING_CLIENT_SOLD_ITEMS;
    public static String PURCHASING_OWNER_PURCHASED_ITEMS;

    public static String SELLING_OWNER_NO_ENOUGH_ITEMS;
    public static String SELLING_CLIENT_NO_ENOUGH_COINS;
    public static String SELLING_CLIENT_PURCHASED_ITEMS;
    public static String SELLING_OWNER_SOLD_ITEMS;
    public static String SELLING_CLIENT_NO_ENOUGH_SPACE;

    public static String ASSIGN_PRICE_WRONG;
    public static String ASSIGNED_PRICE;
    public static List<String> ASSIGN_PRICE;

    public static String ASSIGN_TRADE_QUANTITY_WRONG;
    public static List<String> ASSIGN_TRADE_QUANTITY;

    public static List<String> ASSIGN_QUANTITY;
    public static String ASSIGN_QUANTITY_WRONG;

    public static String CHEST_CLOSED_REQUIRED;
    public static String SELLING_ITEMS_ADD_STOCK_NO_ENOUGH;
    public static String SELLING_ITEMS_ADDED_STOCK;
    public static String SELLING_ITEMS_WITHDRAW_STOCK_NO_ENOUGH;
    public static String SELLING_ITEMS_WITHDRAWN_STOCK;
    public static String SELLING_ITEMS_WITHDRAW_NO_ENOUGH_SPACE;

    public static String SLOT_TOKEN_CLAIMED;
    public static String SLOT_TOKEN_SENDER_MESSAGE;
    public static String SLOT_TOKEN_RECEIVER_MESSAGE;
    public static String SLOT_TOKEN_MAX_UNLOCKED;

    // ===== LOAD =====
    public static void load() {
        FileConfiguration config = Configs.getMessagesConfig().getConfig();

        for (Field field : Messages.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;

            String key = field.getName();

            try {
                if (field.getType() == String.class) {
                    String value = config.getString(key);
                    field.set(null, value);
                }
                else if (field.getType() == List.class) {
                    List<String> list = config.getStringList(key);
                    field.set(null, list);
                }
            } catch (IllegalAccessException ignored) {
                UtilString.get(Config.PREFIX + " &cFailed to load message " + key).hex().sendMessageInConsole();
            }
        }
    }
}

