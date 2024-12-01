package thingxII.pixelblend.Config;

import Core.Config.PredicateConfig;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/pixelblend/CreateAndPlantSeeds.yml")
public class CreateAndPlantSeedConfig extends AbstractYamlConfig {
    private List<EntityConfig> configs = Collections.singletonList(new EntityConfig());

    CreateAndPlantSeedConfig() { }

    public List<EntityConfig> getConfigs() { return configs; }

    @ConfigSerializable
    public static class EntityConfig {
        private PredicateConfig predicate = new PredicateConfig(new ArrayList<>(), Collections.singletonList("Seed Sower"), new ArrayList<>());
        private int range = 1;

        EntityConfig() { }

        public PredicateConfig getPredicate() { return predicate; }
        public int getRange() { return range; }
    }
}
