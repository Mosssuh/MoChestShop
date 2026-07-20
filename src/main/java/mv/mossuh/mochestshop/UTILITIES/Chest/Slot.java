package mv.mossuh.mochestshop.UTILITIES.Chest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.InvalidInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.PurchasingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.SellingInfo;
import mv.mossuh.mochestshop.UTILITIES.Chest.Collectors.TradeInfo;
import mv.mossuh.mochestshop.UTILITIES.Enums.TradeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class Slot {
    private int slot = 0;
    private ItemStack item = new ItemStack(Material.AIR);
    private TradeInfo info = new InvalidInfo();

    public Slot(Integer slot, ItemStack item, TradeInfo info) {
        if (slot != null) this.slot = slot;
        if (item != null) this.item = item;
        if (info != null) { this.info = info; }
    }

    public Slot() {}

    public int getSlot() { return slot; }
    public ItemStack getItem() { return item; }
    public void removeItem() {
        this.item = new ItemStack(Material.AIR);
    }
    public TradeInfo getInfo() { return info; }
    public void setInfo(TradeType type) {
        switch (type) {
            case BUY:
                this.info = new PurchasingInfo();
                break;
            case SELL:
                this.info = new SellingInfo();
                break;
        }
    }

    public boolean isValid() { return slot != 0; }
    public boolean hasItem() { return !item.getType().equals(Material.AIR); }

    public static String serializeSlotData(List<Slot> slots) {
        JsonArray array = new JsonArray();

        if (slots == null || slots.isEmpty()) {
            return array.toString();
        }

        for (Slot slot : slots) {
            if (!slot.hasItem()) continue;

            JsonObject obj = new JsonObject();
            obj.addProperty("slot", slot.getSlot());

            TradeInfo info = slot.getInfo();

            if (info instanceof PurchasingInfo) {
                PurchasingInfo p = (PurchasingInfo) info;
                obj.addProperty("type", "BUY");
                obj.addProperty("price", p.getPurchasingPrice());
                obj.addProperty("purchased_items", p.getPurchasedItems());
            } else if (info instanceof SellingInfo) {
                SellingInfo s = (SellingInfo) info;
                obj.addProperty("type", "SELL");
                obj.addProperty("price", s.getSellingPrice());
                obj.addProperty("sold_items", s.getSoldItems());
                obj.addProperty("items_for_sale", s.getItemsForSale());
            }

            array.add(obj);
        }

        return array.toString();
    }

    public static Map<Integer, TradeInfo> deserializeSlotData(String json) {
        Map<Integer, TradeInfo> map = new HashMap<>();

        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();

        for (JsonElement el : array) {
            JsonObject obj = el.getAsJsonObject();

            int slot = obj.get("slot").getAsInt();
            String type = obj.get("type").getAsString();

            double price = obj.get("price").getAsDouble();

            TradeInfo info;

            if (type.equals("BUY")) {
                PurchasingInfo p = new PurchasingInfo();
                int purchasedItems = obj.get("purchased_items").getAsInt();
                p.setPurchasingPrice(price);
                p.setPurchasedItems(purchasedItems);
                info = p;
            } else {
                SellingInfo s = new SellingInfo();
                int soldItems = obj.get("sold_items").getAsInt();
                int itemsForSale = obj.get("items_for_sale").getAsInt();
                s.setSellingPrice(price);
                s.setSoldItems(soldItems);
                s.setItemsForSale(itemsForSale);
                info = s;
            }

            map.put(slot, info);
        }

        return map;
    }


    public static String serializeItemStack(List<Slot> slots) {
        if (slots == null || slots.isEmpty()) {
            return null;
        }

        try {
            ItemStack[] items = new ItemStack[slots.size()];
            for (int i = 0; i < slots.size(); i++) {
                Slot slot = slots.get(i);
                ItemStack item = slot.getItem();
                items[i] = item;
            }

            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(bytesOut);
            out.writeObject(items);
            out.flush();
            out.close();
            return Base64.getEncoder().encodeToString(bytesOut.toByteArray());
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static ItemStack[] deserializeItemStack(String base64) {
        try {
            byte[] data = Base64Coder.decodeLines(base64);
            ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
            BukkitObjectInputStream in = new BukkitObjectInputStream(bytesIn);

            ItemStack[] items = (ItemStack[]) in.readObject();
            in.close();

            return items;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }
}
