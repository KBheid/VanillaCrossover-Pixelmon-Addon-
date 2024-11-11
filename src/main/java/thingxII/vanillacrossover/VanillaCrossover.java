package thingxII.vanillacrossover;

import Core.PixelmonEntityTracker;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thingxII.vanillacrossover.AbilityEffects.Harvest_HarvestCrops;
import thingxII.vanillacrossover.AbilityEffects.Overgrow_DoubleCropGrowth;
import thingxII.vanillacrossover.AbilityEffects.SeedSower_PlantSeeds;
import thingxII.vanillacrossover.Config.ChestablePokemonConfig;
import thingxII.vanillacrossover.Config.PeriodicDroppingConfig;
import thingxII.vanillacrossover.Config.StatusItemsConfig;
import thingxII.vanillacrossover.ItemEffects.StatusEffectItems.BlazePowderBurn;
import thingxII.vanillacrossover.ItemEffects.StatusEffectItems.PufferPoison;
import thingxII.vanillacrossover.PokemonEffects.BounceEffect;
import thingxII.vanillacrossover.PokemonEffects.StoragePokemon.PlayerPokemonStorage;
import thingxII.vanillacrossover.PokemonEffects.StoragePokemon.StoragePokemonInteraction;
import thingxII.vanillacrossover.PokemonEffects.PeriodicDropping.PeriodicDropping;

@Mod("vanillacrossover")
@Mod.EventBusSubscriber(modid = "vanillacrossover")
public class VanillaCrossover {

    public static final Logger LOGGER = LogManager.getLogger();

    public VanillaCrossover() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ChestablePokemonConfig.SPEC, "vanillacrossover/ChestablePokemon.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StatusItemsConfig.SPEC, "vanillacrossover/StatusItems.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PeriodicDroppingConfig.SPEC, "vanillacrossover/PeriodicDropping.toml");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(PixelmonEntityTracker.class);
        // Pixelmon.EVENT_BUS.register(PixelmonEntityTracker.class);
        bus.addListener(this::commonSetup);

        bus.register(ContainerRegistration.class);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {

        if (ChestablePokemonConfig.allowChestablePokemon && !ChestablePokemonConfig.chestableSpecies.isEmpty()) {
            MinecraftForge.EVENT_BUS.register(PlayerPokemonStorage.class);
            MinecraftForge.EVENT_BUS.register(StoragePokemonInteraction.class);
        }

        if (StatusItemsConfig.allowStatusItems) {
            MinecraftForge.EVENT_BUS.register(BlazePowderBurn.class);
            MinecraftForge.EVENT_BUS.register(PufferPoison.class);
        }

        MinecraftForge.EVENT_BUS.register(PeriodicDropping.class);
        MinecraftForge.EVENT_BUS.register(Overgrow_DoubleCropGrowth.class);
        MinecraftForge.EVENT_BUS.register(Harvest_HarvestCrops.class);
        MinecraftForge.EVENT_BUS.register(SeedSower_PlantSeeds.class);
        MinecraftForge.EVENT_BUS.register(BounceEffect.class);
    }

}
