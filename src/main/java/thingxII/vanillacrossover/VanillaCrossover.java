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
import thingxII.vanillacrossover.Effects.*;
import thingxII.vanillacrossover.Config.StatusItemsConfig;
import thingxII.vanillacrossover.ItemEffects.StatusEffectItems.BlazePowderBurn;
import thingxII.vanillacrossover.ItemEffects.StatusEffectItems.PufferPoison;
import thingxII.vanillacrossover.Effects.StoragePokemon.PlayerPokemonStorage;
import thingxII.vanillacrossover.Effects.StoragePokemon.StoragePokemonInteraction;
import thingxII.vanillacrossover.Effects.PeriodicDropping.PeriodicDropping;

@Mod("vanillacrossover")
@Mod.EventBusSubscriber(modid = "vanillacrossover")
public class VanillaCrossover {

    public static final Logger LOGGER = LogManager.getLogger();

    public VanillaCrossover() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StatusItemsConfig.SPEC, "vanillacrossover/StatusItems.toml");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(PixelmonEntityTracker.class);
        bus.addListener(this::commonSetup);

        bus.register(ContainerRegistration.class);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        ConfigProxy.reload();

        if (StatusItemsConfig.allowStatusItems) {
            MinecraftForge.EVENT_BUS.register(BlazePowderBurn.class);
            MinecraftForge.EVENT_BUS.register(PufferPoison.class);
        }

        MinecraftForge.EVENT_BUS.register(PlayerPokemonStorage.class);
        MinecraftForge.EVENT_BUS.register(StoragePokemonInteraction.class);
        MinecraftForge.EVENT_BUS.register(PeriodicDropping.class);
        MinecraftForge.EVENT_BUS.register(DoubleCropGrowth.class);
        MinecraftForge.EVENT_BUS.register(HarvestCrops.class);
        MinecraftForge.EVENT_BUS.register(CreateAndPlantSeeds.class);
        MinecraftForge.EVENT_BUS.register(BounceOnRightClick.class);
        MinecraftForge.EVENT_BUS.register(SwapPositionOwnerInDanger.class);
        MinecraftForge.EVENT_BUS.register(ApplyPeriodicEffect.class);
    }

}
