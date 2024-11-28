package thingxII.vanillacrossover.Config;

import Core.Config.PredicateConfig;
import Core.Config.StatScalarConfig;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/vanillacrossover/PowerFurnaceEffect.yml")
public class PowerFurnaceConfig extends AbstractYamlConfig {
    private List<EntityConfig> configs = Collections.singletonList(new EntityConfig(
            new PredicateConfig(new ArrayList<>(), Collections.singletonList("Blaze"), new ArrayList<>()),
            30.0f,
            80.0f,
            8.0f,
            25.0f,
            new StatScalarConfig(0.0f, 1.0f,
                    new StatScalarConfig.PokemonStatConfig(-1, -1, -1, -1, -1, 200),
                    new StatScalarConfig.PokemonStatConfig(-1, -1, -1, -1, -1, 0),
                    new StatScalarConfig.PixelmonStatConfig()),
            new StatScalarConfig(0.0f, 1.0f,
                    new StatScalarConfig.PokemonStatConfig(-1, -1, -1, 200, -1, -1),
                    new StatScalarConfig.PokemonStatConfig(-1, -1, -1, 0, -1, -1),
                    new StatScalarConfig.PixelmonStatConfig())
    ));

    PowerFurnaceConfig() { }

    public List<EntityConfig> getConfigs() { return configs; }

    @ConfigSerializable
    public static class EntityConfig {
        private PredicateConfig predicate = new PredicateConfig(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        private float minCooldownDuration = 30.0f;
        private float maxCooldownDuration = 80.0f;
        private float minFurnacePower = 8.0f;
        private float maxFurnacePower = 25.0f;
        private StatScalarConfig cooldownScalarCalculation = new StatScalarConfig();
        private StatScalarConfig powerScalarCalculation = new StatScalarConfig();

        EntityConfig() { }
        EntityConfig(PredicateConfig predicate, float minCooldownDuration, float maxCooldownDuration, float minFurnacePower, float maxFurnacePower, StatScalarConfig cooldownScalarCalculation, StatScalarConfig powerScalarCalculation) {
            this.predicate = predicate;
            this.minCooldownDuration = minCooldownDuration;
            this.maxCooldownDuration = maxCooldownDuration;
            this.minFurnacePower = minFurnacePower;
            this.maxFurnacePower = maxFurnacePower;
            this.cooldownScalarCalculation = cooldownScalarCalculation;
            this.powerScalarCalculation = powerScalarCalculation;
        }

        public PredicateConfig getPredicate() { return predicate; }
        public float getMinCooldownDuration() { return minCooldownDuration; }
        public float getMaxCooldownDuration() { return maxCooldownDuration; }
        public float getMinFurnacePower() { return minFurnacePower; }
        public float getMaxFurnacePower() { return maxFurnacePower; }
        public StatScalarConfig getCooldownScalarCalculation() { return cooldownScalarCalculation; }
        public StatScalarConfig getPowerScalarCalculation() { return powerScalarCalculation; }
    }
}
