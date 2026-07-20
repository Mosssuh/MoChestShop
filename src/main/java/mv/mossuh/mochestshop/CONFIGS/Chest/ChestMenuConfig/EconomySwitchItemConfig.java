package mv.mossuh.mochestshop.CONFIGS.Chest.ChestMenuConfig;

public class EconomySwitchItemConfig {
    private String material = "AIR";
    private byte data = -1;
    private String name = "";
    private String switchMessage = "";

    public EconomySwitchItemConfig(String material, Byte data, String name, String switchMessage) {
        if (material != null) { this.material = material; }
        if (data != null) { this.data = data; }
        if (name != null) { this.name = name; }
        if (switchMessage != null) { this.switchMessage = switchMessage; }
    }
    public  EconomySwitchItemConfig() {}

    public String getMaterial() { return material; }
    public byte getData() { return data; }
    public String getName() { return name; }
    public String getSwitchMessage() { return switchMessage; }
}
