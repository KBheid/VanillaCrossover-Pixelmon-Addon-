package thingxII.vanillacrossover.Config;

import Core.Config.PredicateConfig;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/vanillacrossover/SwapPositionWithOwnerInDanger.yml")
public class SwapPositionOwnerInDangerConfig extends AbstractYamlConfig {
    private List<EntityConfig> configs = Collections.singletonList(new EntityConfig());

    SwapPositionOwnerInDangerConfig() { }

    public List<EntityConfig> getConfigs() { return configs; }

    @ConfigSerializable
    public static class EntityConfig {
        private PredicateConfig predicate = new PredicateConfig(Collections.singletonList("Ally Switch"), new ArrayList<>(), new ArrayList<>());
        private int cooldown = 3 * 60 * 20;

        EntityConfig() { }

        public PredicateConfig getPredicate() { return predicate; }
        public int getCooldown() { return cooldown; }
    }
}
