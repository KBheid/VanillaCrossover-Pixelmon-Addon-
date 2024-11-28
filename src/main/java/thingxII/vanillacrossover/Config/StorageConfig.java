package thingxII.vanillacrossover.Config;

import Core.Config.PredicateConfig;
import Core.Config.StatIntegerConfig;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/vanillacrossover/Storage.yml")
public class StorageConfig extends AbstractYamlConfig {
    private String palettePrefix = "basket";
    private List<EntityConfig> configs = Arrays.asList(
            new EntityConfig(
                    new PredicateConfig(new ArrayList<>(), new ArrayList<>(), Arrays.asList("MUDBRAY", "MUDSDALE")),
                    "minecraft:barrel",
                    false,
                    new StatIntegerConfig(1,
                        new StatIntegerConfig.PokemonStatConfig(
                            126,
                            true,
                            -1,
                            false,
                            -1,
                            false,
                            -1,
                            false,
                            -1,
                            false,
                            -1,
                            false
                            ), new StatIntegerConfig.PokemonStatConfig(
                            15,
                            true,
                            -1,
                            false,
                            -1,
                            false,
                            -1,
                            false,
                            -1,
                            false,
                            -1,
                            false
                    ), new StatIntegerConfig.PixelmonStatConfig(
                            5,
                            true,
                            -1,
                            false,
                            false))
            ),
            new EntityConfig(
                    new PredicateConfig(new ArrayList<>(), Collections.singletonList("Cheek Pouch"), new ArrayList<>()),
                    null,
                    true,
                    new StatIntegerConfig(1, new StatIntegerConfig.PokemonStatConfig(), new StatIntegerConfig.PokemonStatConfig(), new StatIntegerConfig.PixelmonStatConfig())
            )
    );

    StorageConfig() { }

    public String getPalettePrefix() { return palettePrefix; }
    public List<EntityConfig> getConfigs() { return configs; }

    @ConfigSerializable
    public static class EntityConfig {
        private PredicateConfig predicate = new PredicateConfig(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        private String item = "";
        private boolean togglePaletteWhenEmpty = false;
        private StatIntegerConfig sizeCalculation = new StatIntegerConfig(1, new StatIntegerConfig.PokemonStatConfig(), new StatIntegerConfig.PokemonStatConfig(), new StatIntegerConfig.PixelmonStatConfig());

        EntityConfig() { }
        EntityConfig(PredicateConfig predicate, String item, boolean togglePaletteWhenEmpty, StatIntegerConfig sizeCalculation) {
            this.predicate = predicate;
            this.item = item;
            this.togglePaletteWhenEmpty = togglePaletteWhenEmpty;
            this.sizeCalculation = sizeCalculation;
        }

        public PredicateConfig getPredicate() { return predicate; }
        public Item getItem() {
            if (item == null || item.isEmpty()) {
                return null;
            }
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
        };
        public boolean getTogglePaletteWhenEmpty() { return togglePaletteWhenEmpty; }
        public StatIntegerConfig getSizeCalculation() { return sizeCalculation; }
    }
}
