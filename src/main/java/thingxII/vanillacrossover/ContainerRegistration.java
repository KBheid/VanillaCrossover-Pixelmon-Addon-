package thingxII.vanillacrossover;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import thingxII.vanillacrossover.StoragePokemon.PokemonStorageContainer;
import thingxII.vanillacrossover.StoragePokemon.PokemonStorageScreen;

public class ContainerRegistration {
    public static ContainerType<PokemonStorageContainer> STORAGE_POKEMON_CONTAINER_TYPE;

    @SubscribeEvent
    public static void bindScreens(FMLClientSetupEvent event) {
        ScreenManager.register(STORAGE_POKEMON_CONTAINER_TYPE, PokemonStorageScreen::new);
    }

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        STORAGE_POKEMON_CONTAINER_TYPE = IForgeContainerType.create(PokemonStorageContainer::new);
        STORAGE_POKEMON_CONTAINER_TYPE.setRegistryName("storage_pokemon_container");
        event.getRegistry().register(STORAGE_POKEMON_CONTAINER_TYPE);
    }
}
