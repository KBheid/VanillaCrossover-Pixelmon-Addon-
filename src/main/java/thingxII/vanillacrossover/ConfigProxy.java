package thingxII.vanillacrossover;

import com.pixelmonmod.pixelmon.api.config.api.yaml.YamlConfigFactory;
import thingxII.vanillacrossover.Config.DoubleGrowthConfig;
import thingxII.vanillacrossover.Config.HarvestEffectConfig;
import thingxII.vanillacrossover.Config.PeriodicDroppingConfig;

import java.io.IOException;

public class ConfigProxy {
    private static HarvestEffectConfig harvestEffectConfig;
    private static PeriodicDroppingConfig periodicDroppingConfig;
    private static DoubleGrowthConfig doubleGrowthConfig;

    ConfigProxy() { }

    public static void reload() {
        try {
            harvestEffectConfig = YamlConfigFactory.getInstance(HarvestEffectConfig.class);
            periodicDroppingConfig = YamlConfigFactory.getInstance(PeriodicDroppingConfig.class);
            doubleGrowthConfig = YamlConfigFactory.getInstance(DoubleGrowthConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HarvestEffectConfig getHarvestEffectConfig() { return harvestEffectConfig; }
    public static PeriodicDroppingConfig getPeriodicDroppingConfig() { return periodicDroppingConfig; }
    public static DoubleGrowthConfig getDoubleGrowthConfig() { return doubleGrowthConfig; }
}
