package thingxII.vanillacrossover.Config;

import Core.Config.PredicateConfig;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import thingxII.vanillacrossover.Effects.PeriodicDropping.PeriodicDropData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/vanillacrossover/PeriodicDrops.yml")
public class PeriodicDroppingConfig extends AbstractYamlConfig {
    private List<SpecConfig> config;

    PeriodicDroppingConfig() {
        config = Arrays.asList(
                new SpecConfig(new PredicateConfig(new ArrayList<>(), new ArrayList<>(), Arrays.asList("TRUBBISH")),
                        100,
                        Arrays.asList(
                                new DropConfig("minecraft:glass_bottle", 1, 2),
                                new DropConfig("minecraft:potato", 1, 3),
                                new DropConfig("minecraft:carrot", 1, 3),
                                new DropConfig("minecraft:beetroot", 1, 3),
                                new DropConfig("minecraft:rotten_flesh", 1, 5),
                                new DropConfig("minecraft:bone", 1, 3),
                                new DropConfig("minecraft:leather", 1, 3),
                                new DropConfig("minecraft:bread", 1, 2),
                                new DropConfig("minecraft:bucket", 1, 1),
                                new DropConfig("minecraft:charcoal", 2, 3),
                                new DropConfig("minecraft:chicken", 1, 1),
                                new DropConfig("minecraft:clay_ball", 1, 3),
                                new DropConfig("minecraft:cobweb", 1, 2),
                                new DropConfig("minecraft:damaged_anvil", 1, 1),
                                new DropConfig("minecraft:dead_bush", 1, 1),
                                new DropConfig("minecraft:egg", 1, 2),
                                new DropConfig("minecraft:flower_pot", 1, 1),
                                new DropConfig("minecraft:glowstone_dust", 2, 3),
                                new DropConfig("minecraft:gold_nugget", 1, 2),
                                new DropConfig("minecraft:lead", 1, 1),
                                new DropConfig("minecraft:mutton", 1, 1),
                                new DropConfig("minecraft:netherite_scrap", 1, 1),
                                new DropConfig("minecraft:podzol", 1, 3),
                                new DropConfig("minecraft:player_head", 1, 1),
                                new DropConfig("minecraft:poisonous_potato", 1, 1),
                                new DropConfig("minecraft:porkchop", 1, 2),
                                new DropConfig("minecraft:redstone", 1, 3),
                                new DropConfig("minecraft:shulker_shell", 1, 1),
                                new DropConfig("minecraft:slime_ball", 2, 3),
                                new DropConfig("minecraft:sponge", 1, 2),
                                new DropConfig("minecraft:stick", 3, 4),
                                new DropConfig("minecraft:string", 2, 3),
                                new DropConfig("minecraft:suspicious_stew", 1, 1),
                                new DropConfig("minecraft:zombie_head", 1, 1),
                                new DropConfig("minecraft:fermented_spider_eye", 1, 8)
                        )
                ),
                new SpecConfig(new PredicateConfig(new ArrayList<>(), new ArrayList<>(), Arrays.asList("GARBODOR")),
                        60,
                        Arrays.asList(
                                new DropConfig("minecraft:glass_bottle", 1, 2),
                                new DropConfig("minecraft:potato", 1, 3),
                                new DropConfig("minecraft:carrot", 1, 3),
                                new DropConfig("minecraft:beetroot", 1, 3),
                                new DropConfig("minecraft:rotten_flesh", 1, 5),
                                new DropConfig("minecraft:bone", 1, 3),
                                new DropConfig("minecraft:leather", 1, 3),
                                new DropConfig("minecraft:bread", 1, 2),
                                new DropConfig("minecraft:bucket", 1, 1),
                                new DropConfig("minecraft:charcoal", 2, 3),
                                new DropConfig("minecraft:chicken", 1, 1),
                                new DropConfig("minecraft:clay_ball", 1, 3),
                                new DropConfig("minecraft:cobweb", 1, 2),
                                new DropConfig("minecraft:chipped_anvil", 1, 1),
                                new DropConfig("minecraft:dead_bush", 1, 1),
                                new DropConfig("minecraft:egg", 1, 2),
                                new DropConfig("minecraft:flower_pot", 1, 1),
                                new DropConfig("minecraft:glowstone_dust", 2, 3),
                                new DropConfig("minecraft:gold_nugget", 1, 2),
                                new DropConfig("minecraft:lead", 1, 1),
                                new DropConfig("minecraft:mutton", 1, 1),
                                new DropConfig("minecraft:netherite_scrap", 1, 1),
                                new DropConfig("minecraft:netherite_scrap", 1, 1),
                                new DropConfig("minecraft:podzol", 1, 3),
                                new DropConfig("minecraft:player_head", 1, 1),
                                new DropConfig("minecraft:poisonous_potato", 1, 1),
                                new DropConfig("minecraft:porkchop", 1, 2),
                                new DropConfig("minecraft:redstone", 1, 3),
                                new DropConfig("minecraft:shulker_shell", 1, 1),
                                new DropConfig("minecraft:shulker_shell", 1, 1),
                                new DropConfig("minecraft:slime_ball", 2, 3),
                                new DropConfig("minecraft:sponge", 1, 2),
                                new DropConfig("minecraft:sponge", 1, 2),
                                new DropConfig("minecraft:stick", 3, 4),
                                new DropConfig("minecraft:string", 2, 3),
                                new DropConfig("minecraft:suspicious_stew", 1, 1),
                                new DropConfig("minecraft:zombie_head", 1, 1),
                                new DropConfig("minecraft:fermented_spider_eye", 1, 8)
                        )
                )
        );
    }

    public List<SpecConfig> getConfig() { return config; }

    @ConfigSerializable
    public static class SpecConfig {
        private PredicateConfig predicate;
        private int cooldown;
        private List<DropConfig> drops;

        public SpecConfig() { }

        public SpecConfig(PredicateConfig predicate, int cooldown, List<DropConfig> drops) {
            this.predicate = predicate;
            this.cooldown = cooldown;
            this.drops = drops;
        }

        public PredicateConfig getPredicate() { return predicate; }
        public int getCooldown() { return cooldown; }
        public List<DropConfig> getDrops() { return drops; }

        public List<PeriodicDropData> getDropData() {
            ArrayList<PeriodicDropData> ret = new ArrayList<>();

            for (DropConfig drop : drops) {
                ret.add(new PeriodicDropData(ForgeRegistries.ITEMS.getValue(new ResourceLocation(drop.getItem())), drop.getMinQuantity(), drop.getMaxQuantity(), cooldown));
            }

            return ret;
        }
    }

    @ConfigSerializable
    public static class DropConfig {
        private String item;
        private int minQuantity;
        private int maxQuantity;

        public DropConfig() { }

        public DropConfig(String item, int minQuantity, int maxQuantity) {
            this.item = item;
            this.minQuantity = minQuantity;
            this.maxQuantity = maxQuantity;
        }

        public String getItem() { return item; }
        public int getMinQuantity() { return minQuantity; }
        public int getMaxQuantity() { return maxQuantity; }
    }
}
