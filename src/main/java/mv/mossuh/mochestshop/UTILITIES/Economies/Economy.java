package mv.mossuh.mochestshop.UTILITIES.Economies;

import mv.mossuh.mocore.UTILITIES.UsefulString;

public class Economy {
    private String applicator = "invalid";
    private String economy = "invalid";
    private String id = "invalid";
    private String prefix = "";

    public Economy(String applicator, String economy, String id, String prefix) {
        if (applicator != null) { this.applicator = applicator.toLowerCase(); }
        if (economy != null) { this.economy = economy.toLowerCase(); }
        if (id != null) { this.id = id.toLowerCase(); }
        if (prefix != null) { this.prefix = UsefulString.get(prefix).hex().apply(); }
    }
    public Economy() {}

    public String getApplicator() { return applicator; }
    public String getEconomy() { return economy; }
    public String getID() { return id; }
    public String getPrefix() { return prefix; }

    public boolean isValid() {
        return !applicator.equals("invalid") && !economy.equals("invalid") && !id.equals("invalid");
    }
}
