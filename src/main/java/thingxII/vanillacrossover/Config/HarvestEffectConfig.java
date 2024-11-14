package thingxII.vanillacrossover.Config;

import Core.PredicateConfigSerializer;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/vanillacrossover/HarvestEffect.yml")
public class HarvestEffectConfig extends AbstractYamlConfig {
    private List<HarvestPokemonConfiguration>  config = Collections.singletonList(new HarvestPokemonConfiguration());

    HarvestEffectConfig() { }

    public List<HarvestPokemonConfiguration> getConfigs() { return config; }

    @ConfigSerializable
    public static class HarvestPokemonConfiguration {
        private PredicateConfigSerializer predicate = new PredicateConfigSerializer(new ArrayList<>(), Collections.singletonList("Harvest"), new ArrayList<>());
        private int cooldown = 20;

        public HarvestPokemonConfiguration() { }

        public PredicateConfigSerializer getPredicate() { return predicate; }
        public int getCooldown() { return cooldown; }
    }
}
