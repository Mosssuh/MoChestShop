package mv.mossuh.mochestshop.UTILITIES.Enchantments;

public class Enchant {
    private String enchantName;
    private int level;

    public Enchant(String enchantName, int level) {
        this.enchantName = enchantName;
        this.level = level;
    }

    public String getEnchantName() {
        return this.enchantName;
    }

    public int getLevel() {
        return this.level;
    }
}
