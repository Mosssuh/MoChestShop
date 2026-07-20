package mv.mossuh.mochestshop.UTILITIES.Enchantments;

import java.util.ArrayList;
import java.util.List;

public class Enchantments {
    private List<Enchant> enchantments = new ArrayList<>();

    public Enchantments(List<String> enchantmentsStrings) {
        if (enchantmentsStrings != null && !enchantmentsStrings.isEmpty()) {
            for (String enchantmentString : enchantmentsStrings) {
                String[] enchantmentParts = enchantmentString.split(":", 2);
                String name = enchantmentParts[0];
                int level = -1;
                if (enchantmentParts.length == 2) {
                    level = Integer.parseInt(enchantmentParts[1]);
                }
                this.enchantments.add(new Enchant(name, level));
            }
        }
    }

    public List<Enchant> getEnchants() {
        return this.enchantments;
    }


    public int getEnchantLevel(String enchantment) {
        for (Enchant e : enchantments) {
            if (e.getEnchantName().equalsIgnoreCase(enchantment)) {
                return e.getLevel();
            }
        }
        return -1;
    }

    public boolean hasEnchant(String enchantment) {
        for (Enchant e : enchantments) {
            if (e.getEnchantName().equalsIgnoreCase(enchantment)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEnchant() {
        if (!enchantments.isEmpty()) {
            return true;
        }
        return false;
    }
}

