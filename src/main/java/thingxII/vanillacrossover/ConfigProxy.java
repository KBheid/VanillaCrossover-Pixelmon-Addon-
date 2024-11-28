package thingxII.vanillacrossover;

import com.pixelmonmod.pixelmon.api.config.api.yaml.YamlConfigFactory;
import thingxII.vanillacrossover.Config.*;

import java.io.IOException;

public class ConfigProxy {
    private static HarvestEffectConfig harvestEffectConfig;
    private static PeriodicDroppingConfig periodicDroppingConfig;
    private static DoubleGrowthConfig doubleGrowthConfig;
    private static StorageConfig storageConfig;
    private static CreateAndPlantSeedConfig createAndPlantSeedConfig;
    private static SwapPositionOwnerInDangerConfig swapPositionOwnerInDangerConfig;
    private static BounceConfig bounceConfig;
    private static PeriodicEffectConfig periodicEffectConfig;
    private static PowerFurnaceConfig powerFurnaceConfig;

    ConfigProxy() { }

    public static void reload() {
        try {
            harvestEffectConfig = YamlConfigFactory.getInstance(HarvestEffectConfig.class);
            periodicDroppingConfig = YamlConfigFactory.getInstance(PeriodicDroppingConfig.class);
            doubleGrowthConfig = YamlConfigFactory.getInstance(DoubleGrowthConfig.class);
            storageConfig = YamlConfigFactory.getInstance(StorageConfig.class);
            createAndPlantSeedConfig = YamlConfigFactory.getInstance(CreateAndPlantSeedConfig.class);
            swapPositionOwnerInDangerConfig = YamlConfigFactory.getInstance(SwapPositionOwnerInDangerConfig.class);
            bounceConfig = YamlConfigFactory.getInstance(BounceConfig.class);
            periodicEffectConfig = YamlConfigFactory.getInstance(PeriodicEffectConfig.class);
            powerFurnaceConfig = YamlConfigFactory.getInstance(PowerFurnaceConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HarvestEffectConfig getHarvestEffectConfig() { return harvestEffectConfig; }
    public static PeriodicDroppingConfig getPeriodicDroppingConfig() { return periodicDroppingConfig; }
    public static DoubleGrowthConfig getDoubleGrowthConfig() { return doubleGrowthConfig; }
    public static StorageConfig getStorageConfig() { return storageConfig; }
    public static CreateAndPlantSeedConfig getCreateAndPlantSeedConfig() { return createAndPlantSeedConfig; }
    public static SwapPositionOwnerInDangerConfig getSwapPositionOwnerInDangerConfig() { return swapPositionOwnerInDangerConfig; }
    public static BounceConfig getBounceConfig() { return bounceConfig; }
    public static PeriodicEffectConfig getPeriodicEffectConfig() { return periodicEffectConfig; }
    public static PowerFurnaceConfig getPowerFurnaceConfig() { return powerFurnaceConfig; }
}
