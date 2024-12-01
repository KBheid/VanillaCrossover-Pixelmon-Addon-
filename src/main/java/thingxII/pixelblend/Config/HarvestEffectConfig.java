package thingxII.pixelblend.Config;

import Core.Config.PredicateConfig;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/pixelblend/HarvestEffect.yml")
public class HarvestEffectConfig extends AbstractYamlConfig {
    private List<HarvestPokemonConfiguration>  config = Collections.singletonList(new HarvestPokemonConfiguration());

    HarvestEffectConfig() { }

    public List<HarvestPokemonConfiguration> getConfigs() { return config; }

    @ConfigSerializable
    public static class HarvestPokemonConfiguration {
        private PredicateConfig predicate = new PredicateConfig(new ArrayList<>(), Collections.singletonList("Harvest"), new ArrayList<>());
        private int cooldown = 20;

        public HarvestPokemonConfiguration() { }

        public PredicateConfig getPredicate() { return predicate; }
        public int getCooldown() { return cooldown; }
    }
}
