package thingxII.vanillacrossover.Effects.StoragePokemon;

import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import thingxII.vanillacrossover.Config.ChestablePokemonConfig;
import thingxII.vanillacrossover.VanillaCrossover;

import java.util.ArrayList;

public class StoragePokemonInteraction {
    private final static ArrayList<RegistryValue<Species>> ChestableSpecies = new ArrayList<>();

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event) {
        for (String species : ChestablePokemonConfig.chestableSpecies) {
            RegistryValue<Species> foundSpecies = PixelmonSpecies.fromName(species.toUpperCase());
            if (foundSpecies != null && foundSpecies.getValue().isPresent()) {
                ChestableSpecies.add(foundSpecies);
            }
            else {
                VanillaCrossover.LOGGER.error("Cannot resolve a Chestable Species (malformed config file): " + species);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void playerInteract(PlayerInteractEvent.EntityInteract event) {

        if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
            return;
        }

        ItemStack itemStack = event.getItemStack();
        Entity entity = event.getTarget();
        Item item = itemStack.getItem();

        if (event.getCancellationResult() == ActionResultType.CONSUME)
        {
            return;
        }

        if (!(entity instanceof PixelmonEntity)) {
            return;
        }

        PixelmonEntity pixelmonEntity = (PixelmonEntity) entity;
        Pokemon pokemon = pixelmonEntity.getPokemon();

        // Ensure that this player is the pokemon's owner lol
        if (pokemon == null || pokemon.getOwnerPlayerUUID() != event.getPlayer().getUUID()) {
            return;
        }

        boolean foundChestableSpecies = false;
        // Look for a chestable species
        for (RegistryValue<Species> checkedSpecies : ChestableSpecies) {
            if (checkedSpecies.getValue().isPresent()) {
                if (pokemon.getSpecies().is(checkedSpecies.getValue().get())) {
                    // We found it, woo
                    foundChestableSpecies = true;
                    break;
                }
            }
        }

        if (!foundChestableSpecies) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        PlayerPokemonStorage allPokemonStorageForPlayer = PlayerPokemonStorage.getStorageFor(player);

        if (!allPokemonStorageForPlayer.hasStorageForMon(pokemon)) {
            if (itemStack.getItem() != Items.CHEST) {
                return;
            }

            // If the storage was just created, we need a chest
            if (!(item instanceof BlockItem)) {
                return;
            }

            BlockItem blockItem = (BlockItem) item;
            if (!(blockItem.getBlock().is(Blocks.CHEST))) {
                return;
            }

            // Take the chest
            if (!player.isCreative()) {
                event.getItemStack().shrink(1);
            }

            // Play a place chest sound
            event.getWorld().playSound(null, entity.blockPosition(), SoundEvents.MULE_CHEST, SoundCategory.BLOCKS, 1.0F, 1.0f);

            // Try to set the pokemon's palette. May fail, and that's OK.
            trySetPalette(pokemon);

            // TODO: Kinda ugly, but we're just creating the storage here...
            allPokemonStorageForPlayer.getOrCreateStorageForMon(pokemon);

            // This is the sole action that can occur
            event.setCanceled(true);
            return;
        }

        if (!player.isCrouching()) {
            return;
        }

        // TODO: Similarly, this is just getting it, no creation happens here
        PokemonStorage selectedPokemonStorage = allPokemonStorageForPlayer.getOrCreateStorageForMon(pokemon);
        selectedPokemonStorage.updateReference(pokemon);

        // Recalculate size. If the size got smaller, we might need to spill some items:
        selectedPokemonStorage.calculateSize();
        selectedPokemonStorage.ExecutePendingSpillAt(player.level, pixelmonEntity.getX(), pixelmonEntity.getY(), pixelmonEntity.getZ());

        open(player, selectedPokemonStorage);

        // SFX
        event.getWorld().playSound(null, entity.blockPosition(), SoundEvents.BARREL_OPEN, SoundCategory.BLOCKS, 1.0F, 1.0f);

        // This is the sole action that can occur
        event.setCanceled(true);
    }


    public static void open(ServerPlayerEntity player, PokemonStorage storage) {
        INamedContainerProvider containerProvider = new SimpleNamedContainerProvider((a, b, c) -> new PokemonStorageContainer(a, player.inventory, storage), new TranslationTextComponent(""));
        NetworkHooks.openGui(player, containerProvider, storage::writeToBuffer);
    }

    private static void trySetPalette(Pokemon pokemon) {

        String paletteToTry;
        if (!pokemon.getPalette().getName().equals("none")) {
            paletteToTry = ChestablePokemonConfig.palettePrefix + "_" + pokemon.getPalette().getName();
        }
        else {
            paletteToTry = ChestablePokemonConfig.palettePrefix;
        }

        if (pokemon.hasPalette(paletteToTry)) {
            pokemon.setPalette(paletteToTry);
        }
        else {
            VanillaCrossover.LOGGER.info("Tried to set a chestable mon's palette, but the palette was not found (no palette change occurred - this is not an issue!)");
            VanillaCrossover.LOGGER.info("See this Pokemon's definition if it should have a custom palette: " + pokemon.getSpecies() + ":" + paletteToTry);
        }
    }
}
