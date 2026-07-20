package mv.mossuh.mochestshop.DATABASE;

import mv.mossuh.mochestshop.CHESTS.ActiveChestShop;
import mv.mossuh.mochestshop.CHESTS.ChestShop;
import mv.mossuh.mochestshop.CONFIGS.Chest.ChestShopConfig;
import mv.mossuh.mochestshop.CONFIGS.Config;
import mv.mossuh.mochestshop.CONFIGS.Configs;
import mv.mossuh.mochestshop.MANAGERS.ActiveChestManager;
import mv.mossuh.mochestshop.MoChestShop;
import mv.mossuh.mochestshop.UTILITIES.Chest.Slot;
import mv.mossuh.mochestshop.UTILITIES.Enums.ChestStatus;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import mv.mossuh.mochestshop.UTILITIES.UtilMethods;
import mv.mossuh.mochestshop.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.*;
import java.util.*;

public class ChestShopDatabase {
    private static MoChestShop main = MoChestShop.getInstance();

    public static void executeVacuum() {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try (Connection connection = main.getConnection()){
                if (connection == null) return;
                Statement statement = connection.createStatement();
                statement.execute("VACUUM");
            } catch (SQLException e) {
                UtilString.get(Config.PREFIX + " &4[DATABASE] &cDatabase is busy, VACUUM skipped.").hex().sendMessageInConsole();
            }
        });
    }

    public static void registerDatabaseInCache() {
        int activeChests = 0;
        try (Connection connection = main.getConnection()){
            if (connection == null) return;
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS ChestShops (chest_uuid VARCHAR(50) PRIMARY KEY NOT NULL, " +
                    "chest_owner_uuid VARCHAR(50) NOT NULL, " +
                    "chest_section VARCHAR(255) NOT NULL, " +
                    "chest_location TEXT NOT NULL, " +
                    "chest_trade_type VARCHAR(20) NOT NULL DEFAULT 'SELL', " +
                    "chest_status VARCHAR(20) NOT NULL DEFAULT 'OPEN', " +
                    "chest_unlocked_slots INTEGER NOT NULL DEFAULT 0, " +
                    "chest_economies TEXT, " +
                    "chest_selected_economy VARCHAR(100), " +
                    "chest_slots_items TEXT, " +
                    "chest_slots_data TEXT)");

            ResultSet result = statement.executeQuery("SELECT * FROM ChestShops");
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("chest_uuid"));
                UUID ownerUUID = UUID.fromString(result.getString("chest_owner_uuid"));
                String section = result.getString("chest_section");
                if (section == null) {
                    UtilString.get(Config.PREFIX + " &4[DATABASE] &cConfig Section of the ChestShop " + uuid + " is invalid").hex().sendMessageInConsole();
                    continue;
                }

                // Location format: <world>=<x>=<y>=<z>
                String locatioString = result.getString("chest_location");
                if (locatioString == null) {
                    UtilString.get(Config.PREFIX + " &4[DATABASE] &cLocation of the ChestShop " + uuid + " is invalid").hex().sendMessageInConsole();
                    continue;
                }
                String[] locationSplit = locatioString.split(";=;", 4);
                if (locationSplit.length < 4) {
                    UtilString.get(Config.PREFIX + " &4[DATABASE] &cLocation of the ChestShop " + uuid + " is invalid: " + locatioString).hex().sendMessageInConsole();
                    continue;
                }
                String world = locationSplit[0];
                double x = Double.parseDouble(locationSplit[1]);
                double y = Double.parseDouble(locationSplit[2]);
                double z = Double.parseDouble(locationSplit[3]);
                Location location = new Location(Bukkit.getWorld(world), x, y, z);

                TradeType tradeType = TradeType.fromString(result.getString("chest_trade_type"));
                ChestStatus chestStatus = ChestStatus.fromString(result.getString("chest_status"));
                int unlockedSlots = result.getInt("chest_unlocked_slots");
                List<String> economies = new ArrayList<>();
                String economiesString = result.getString("chest_economies");
                if (economiesString != null) {
                    economies.addAll(Arrays.asList(result.getString("chest_economies").split(", ")));
                }
                String selectedEconomy = result.getString("chest_selected_economy");

                String slotsItemsSerialized = result.getString("chest_slots_items");
                String slotsDataSerialized = result.getString("chest_slots_data");

                List<Slot> slots = UtilMethods.getSlots(slotsItemsSerialized, slotsDataSerialized);
                ChestShopConfig config = Configs.getChestShop(section);
                if (!config.isValid()) {
                    UtilString.get(Config.PREFIX + " &4[DATABASE] &cConfig Section " + config.getSection() +  " of the ChestShop " + uuid + " is invalid.").hex().sendMessageInConsole();
                    continue;
                }
                ChestShop chest = new ChestShop(uuid, ownerUUID, config, tradeType, chestStatus, unlockedSlots, economies, selectedEconomy, slots);
                ActiveChestShop active = new ActiveChestShop(chest, location);
                ActiveChestManager.addChest(active);
                activeChests += 1;
            }
            UtilString.get(Config.PREFIX + " &aLoaded " + activeChests + " active chests in the server!").hex().sendMessageInConsole();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void registerCacheInDatabase(Set<ActiveChestShop> actives) {
        try (Connection connection = main.getConnection()) {
            if (connection == null) return;
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS ChestShops (chest_uuid VARCHAR(50) PRIMARY KEY NOT NULL, " +
                    "chest_owner_uuid VARCHAR(50) NOT NULL, " +
                    "chest_section VARCHAR(255) NOT NULL, " +
                    "chest_location TEXT NOT NULL, " +
                    "chest_trade_type VARCHAR(20) NOT NULL DEFAULT 'SELL', " +
                    "chest_status VARCHAR(20) NOT NULL DEFAULT 'OPEN', " +
                    "chest_unlocked_slots INTEGER NOT NULL DEFAULT 0, " +
                    "chest_economies TEXT, " +
                    "chest_selected_economy VARCHAR(100), " +
                    "chest_slots_items TEXT, " +
                    "chest_slots_data TEXT)");

            if (actives.isEmpty()) return;

            connection.setAutoCommit(false);

            try (PreparedStatement prepared = connection.prepareStatement("INSERT OR REPLACE INTO ChestShops VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")){
                for (ActiveChestShop active : actives) {
                    if (!active.isValid()) continue;
                    ChestShop chest = active.getChest();
                    UUID uuid = chest.getUUID();
                    UUID ownerUUID = chest.getOwner();
                    ChestShopConfig config = chest.getConfig();
                    String section = config.getSection();
                    Location location = active.getLocation();
                    if (location == null)  {
                        UtilString.get(Config.PREFIX + " &4[DATABASE] &cThe location of the ChestShop " + uuid + " is invalid.").hex().sendMessageInConsole();
                        continue;
                    }
                    World world = location.getWorld();
                    if (world == null) {
                        UtilString.get(Config.PREFIX + " &4[DATABASE] &cThe world of the ChestShop " + uuid + " is invalid.").hex().sendMessageInConsole();
                        continue;
                    }
                    String locationString = location.getWorld().getName()+";=;"+location.getX()+";=;"+location.getY()+";=;"+location.getZ();
                    String tradeType = chest.getTradeType().name();
                    String chestStatus = chest.getStatus().name();
                    int unlockedSlots = chest.getUnlockedSlots();
                    String economies = String.join(", ", chest.getEconomies());
                    String selectedEconomy = chest.getSelectedEconomy();
                    List<Slot> slots = chest.getSlots();
                    String serializeItems = Slot.serializeItemStack(slots);
                    String serializeSlotsData = Slot.serializeSlotData(slots);

                    prepared.setString(1, uuid.toString());
                    prepared.setString(2, ownerUUID.toString());
                    prepared.setString(3, section);
                    prepared.setString(4, locationString);
                    prepared.setString(5, tradeType);
                    prepared.setString(6, chestStatus);
                    prepared.setInt(7, unlockedSlots);
                    prepared.setString(8, economies);
                    prepared.setString(9, selectedEconomy);
                    prepared.setString(10, serializeItems);
                    prepared.setString(11, serializeSlotsData);

                    prepared.addBatch();
                }

                prepared.executeBatch();
                connection.commit();
                statement.close();
            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
