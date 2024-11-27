package thingxII.vanillacrossover.Config;

import Core.Config.StatScalarConfig;
import Core.Config.PredicateConfig;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/vanillacrossover/Bounce.yml")
public class BounceConfig extends AbstractYamlConfig {
    private List<EntityConfig> configs = Collections.singletonList(new EntityConfig());

    BounceConfig() { }

    public List<EntityConfig> getConfigs() { return configs; }

    @ConfigSerializable
    public static class EntityConfig {
        private PredicateConfig predicate = new PredicateConfig(new ArrayList<>(), new ArrayList<>(), Collections.singletonList("SPOINK"));
        private float minUpwardForce = 0.75f;
        private float maxUpwardForce = 2.0f;
        private float minForwardForce = 2.0f;
        private float maxForwardForce = 7.0f;

        private StatScalarConfig scalarCalculation = new StatScalarConfig(0f, 1f,
                new StatScalarConfig.PokemonStatConfig(-1, -1, -1, 200 , -1, -1),
                new StatScalarConfig.PokemonStatConfig(-1, -1, -1, 0, -1, -1),
                new StatScalarConfig.PixelmonStatConfig());

        EntityConfig() { }

        public PredicateConfig getPredicate() { return predicate; }
        public float getMinUpwardForce() { return minUpwardForce; }
        public float getMaxUpwardForce() { return maxUpwardForce; }
        public float getMinForwardForce() { return minForwardForce; }
        public float getMaxForwardForce() { return maxForwardForce; }
        public StatScalarConfig getScalarCalculation() { return scalarCalculation; }
    }
}
