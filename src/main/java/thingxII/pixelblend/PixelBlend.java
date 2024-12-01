package thingxII.pixelblend;

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
import thingxII.pixelblend.Effects.*;
import thingxII.pixelblend.Config.StatusItemsConfig;
import thingxII.pixelblend.ItemEffects.StatusEffectItems.BlazePowderBurn;
import thingxII.pixelblend.ItemEffects.StatusEffectItems.PufferPoison;
import thingxII.pixelblend.Effects.StoragePokemon.PlayerPokemonStorage;
import thingxII.pixelblend.Effects.StoragePokemon.StoragePokemonInteraction;
import thingxII.pixelblend.Effects.PeriodicDropping.PeriodicDropping;

@Mod("pixelblend")
@Mod.EventBusSubscriber(modid = "pixelblend")
public class PixelBlend {

    public static final Logger LOGGER = LogManager.getLogger();

    public PixelBlend() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StatusItemsConfig.SPEC, "pixelblend/StatusItems.toml");

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
        MinecraftForge.EVENT_BUS.register(PowerFurnace.class);
    }

}
