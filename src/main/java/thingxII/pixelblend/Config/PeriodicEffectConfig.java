package thingxII.pixelblend.Config;

import Core.Config.PredicateConfig;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import thingxII.pixelblend.PixelBlend;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/pixelblend/PeriodicEffects.yml")
public class PeriodicEffectConfig extends AbstractYamlConfig {
    private List<EntityConfig> configs = Arrays.asList(
            // Torrent effect
            new EntityConfig(
                    new PredicateConfig(new ArrayList<>(), Collections.singletonList("Torrent"), new ArrayList<>()),
                    15,
                    "DOLPHINS_GRACE",
                    1,
                    8,
                    false,
                    6
            ),
            // Easter egg: Stantler regeneration so players occasionally get hearts :) Thanks Bubby
            new EntityConfig(
                    new PredicateConfig(new ArrayList<>(), new ArrayList<>(), Collections.singletonList("STANTLER")),
                    100,
                    "REGENERATION",
                    5,
                    1,
                    true,
                    -1
            )
    );

    PeriodicEffectConfig() { }

    public List<EntityConfig> getConfigs() { return configs; }

    @ConfigSerializable
    public static class EntityConfig {
        private PredicateConfig predicate = new PredicateConfig(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        private int cooldownSeconds = 60;
        private String effect = "SPEED";
        private int level = 0;
        private int durationSeconds = 10;
        private boolean applyDirectlyToOwner = true;
        private float applyRange = -1;

        EntityConfig() {}
        EntityConfig(PredicateConfig predicate, int cooldownSeconds, String effect, int level, int durationSeconds, boolean applyDirectlyToOwner, float applyRange) {
            this.predicate = predicate;
            this.cooldownSeconds = cooldownSeconds;
            this.effect = effect;
            this.level = level;
            this.durationSeconds = durationSeconds;
            this.applyDirectlyToOwner = applyDirectlyToOwner;
            this.applyRange = applyRange;
        }

        public PredicateConfig getPredicate() { return predicate; }
        public int getCooldownSeconds() { return cooldownSeconds; }
        public Effect getEffect() {
            Field field = null;
            try {
                field = Effects.class.getField(effect);
                return (Effect) field.get(null);
            } catch (Exception e) {
                PixelBlend.LOGGER.error("Malformed Effect in PeriodicEffects.yml: " + effect);
            }
            return null;
        }
        public int getLevel() { return level; }
        public int getDurationSeconds() { return durationSeconds; }
        public boolean getApplyDirectlyToOwner() { return applyDirectlyToOwner; }
        public float getApplyRange() { return applyRange; }
    }
}
