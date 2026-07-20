package mv.mossuh.mochestshop.UTILITIES;

import mv.mossuh.mochestshop.UTILITIES.Enchantments.Enchantments;
import mv.mossuh.mocore.UTILITIES.UsefulString;

import java.util.ArrayList;
import java.util.List;

public class ItemInfo {

    private String material = "AIR";
    private String name;
    private List<String> lore = new ArrayList<>();
    private Enchantments enchantments = new Enchantments(null);
    private List<String> flags = new ArrayList<>();
    private boolean unbreakable = false;
    private boolean unique = false;
    public ItemInfo(String material, String name, List<String> lore, Enchantments enchantments, List<String> flags, Boolean unbreakable, Boolean unique) {
        if (material != null) { this.material = material; }
        if (name != null) { this.name = UsefulString.get(name).hex().apply(); }
        List<String> newLore = new ArrayList<>();
        if (lore != null) { for (String line : lore) { newLore.add(UsefulString.get(line).hex().apply()); } }
        this.lore = newLore;
        if (enchantments != null) { this.enchantments = enchantments; }
        if (flags != null) { this.flags = flags; }
        if (unbreakable != null) { this.unbreakable = unbreakable; }
        if (unique != null) { this.unique = unique; }
    }

    public ItemInfo() {}

    public String getMaterial() {
        return material;
    }
    public String getName() {
        return name;
    }
    public boolean hasName() {
        return name != null;
    }
    public List<String> getLore() {
        return lore;
    }
    public boolean hasLore() {
        return lore != null && !lore.isEmpty();
    }
    public Enchantments getEnchantments() {
        return enchantments;
    }
    public List<String> getFlags() {
        return flags;
    }

    public boolean hasFlag() {
        return flags != null && !flags.isEmpty();
    }
    public boolean isUnbreakable() {
        return unbreakable;
    }

    public boolean isUnique() {
        return unique;
    }
}
