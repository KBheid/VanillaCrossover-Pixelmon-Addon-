package thingxII.vanillacrossover;

import Core.PixelmonEntityTracker;
import com.pixelmonmod.pixelmon.Pixelmon;
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
import thingxII.vanillacrossover.Config.ChestablePokemonConfig;
import thingxII.vanillacrossover.Config.PeriodicDroppingConfig;
import thingxII.vanillacrossover.Config.StatusItemsConfig;
import thingxII.vanillacrossover.PeriodicDropping.PeriodicDropping;
import thingxII.vanillacrossover.StatusEffectItems.BlazePowderBurn;
import thingxII.vanillacrossover.StatusEffectItems.PufferPoison;
import thingxII.vanillacrossover.StoragePokemon.PlayerPokemonStorage;
import thingxII.vanillacrossover.StoragePokemon.StoragePokemonInteraction;

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
    }

}
