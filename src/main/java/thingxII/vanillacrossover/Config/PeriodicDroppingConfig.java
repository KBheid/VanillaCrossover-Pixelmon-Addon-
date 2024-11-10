package thingxII.vanillacrossover.Config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = "vanillacrossover", bus = Mod.EventBusSubscriber.Bus.MOD)
public class PeriodicDroppingConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> DROPPING_SPECIES;

    static {
        DROPPING_SPECIES = BUILDER
            .comment("The format for drops should be: POKEMONNAME#secondsForDrop [ modid:itemType|minDropped|maxDropped,modid:itemType|minDropped|maxDropped,etc. ]")
            .comment("Spaces/tabs are optional outside of the name and drop ticks (but encouraged for readability)")
            .define("Species Periodic Drops", Arrays.asList(
                    "TRUBBISH#100 \t [ minecraft:glass_bottle|1|2, minecraft:potato|1|3, minecraft:carrot|1|3, minecraft:beetroot|1|3, minecraft:rotten_flesh|1|5, minecraft:bone|1|3, minecraft:leather|1|3, minecraft:bread|1|2, minecraft:bucket|1|1, minecraft:charcoal|2|3, minecraft:chicken|1|1, minecraft:clay_ball|1|3, minecraft:cobweb|1|2, minecraft:damaged_anvil|1|1, minecraft:dead_bush|1|1, minecraft:egg|1|2, minecraft:flower_pot|1|1, minecraft:glowstone_dust|2|3, minecraft:gold_nugget|1|2, minecraft:lead|1|1, minecraft:mutton|1|1, minecraft:netherite_scrap|1|1, minecraft:podzol|1|3, minecraft:player_head|1|1, minecraft:poisonous_potato|1|1, minecraft:porkchop|1|2, minecraft:redstone|1|3, minecraft:shulker_shell|1|1, minecraft:slime_ball|2|3, minecraft:sponge|1|2, minecraft:stick|3|4, minecraft:string|2|3, minecraft:suspicious_stew|1|1, minecraft:zombie_head|1|1, minecraft:fermented_spider_eye|1|8 ]",
                    "GARBODOR#60  \t [ minecraft:glass_bottle|1|2, minecraft:potato|1|3, minecraft:carrot|1|3, minecraft:beetroot|1|3, minecraft:rotten_flesh|1|5, minecraft:bone|1|3, minecraft:leather|1|3, minecraft:bread|1|2, minecraft:bucket|1|1, minecraft:charcoal|2|3, minecraft:chicken|1|1, minecraft:clay_ball|1|3, minecraft:cobweb|1|2, minecraft:chipped_anvil|1|1, minecraft:dead_bush|1|1, minecraft:egg|1|2, minecraft:flower_pot|1|1, minecraft:glowstone_dust|2|3, minecraft:gold_nugget|1|2, minecraft:lead|1|1, minecraft:mutton|1|1, minecraft:netherite_scrap|1|1, minecraft:podzol|1|3, minecraft:player_head|1|1, minecraft:poisonous_potato|1|1, minecraft:porkchop|1|2, minecraft:redstone|1|3, minecraft:shulker_shell|1|1, minecraft:slime_ball|2|3, minecraft:sponge|1|2, minecraft:stick|3|4, minecraft:string|2|3, minecraft:suspicious_stew|1|1, minecraft:zombie_head|1|1, minecraft:fermented_spider_eye|1|8, minecraft:netherite_scrap|1|1, minecraft:shulker_shell|1|1, minecraft:sponge|1|2 ]"),
                o -> o instanceof String);
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static List<? extends String> periodicDrops;

    @SubscribeEvent
    static void onLoad(final ModConfig.ModConfigEvent event) {
        periodicDrops = DROPPING_SPECIES.get();
    }
}
