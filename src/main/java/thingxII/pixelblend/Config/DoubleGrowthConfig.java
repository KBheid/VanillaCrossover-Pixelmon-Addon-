package thingxII.pixelblend.Config;

import Core.Config.PredicateConfig;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/pixelblend/DoubleGrowth.yml")
public class DoubleGrowthConfig extends AbstractYamlConfig {
    private List<DoubleGrowthPokemonConfiguration> configs = Collections.singletonList(new DoubleGrowthPokemonConfiguration());

    DoubleGrowthConfig() { }

    public List<DoubleGrowthPokemonConfiguration> getConfigs() { return configs; }

    @ConfigSerializable
    public static class DoubleGrowthPokemonConfiguration {
        private PredicateConfig predicate = new PredicateConfig(new ArrayList<>(), Collections.singletonList("Overgrow"), new ArrayList<>());
        private int range = 5;

        public DoubleGrowthPokemonConfiguration() { }

        public PredicateConfig getPredicate() { return predicate; }
        public int getRange() { return range; }
    }
}
