package thingxII.vanillacrossover.PokemonEffects.PeriodicDropping;

import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.registries.ForgeRegistries;
import thingxII.vanillacrossover.Config.PeriodicDroppingConfig;
import Core.PixelmonEntityTracker;
import thingxII.vanillacrossover.VanillaCrossover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PeriodicDropping {

    public static final HashMap<Species, List<PeriodicDropData>> drops = new HashMap<>();

    private static Random random = null;
    private static PixelmonEntityTracker<Integer> tracker;

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        for (String entry : PeriodicDroppingConfig.periodicDrops) {
            // Step 1: Split by the first occurrence of '#' to separate name and drop interval
            String[] parts = entry.split("#", 2);
            if (parts.length < 2) {
                // Skip invalid entries
                VanillaCrossover.LOGGER.error("Cannot resolve a Periodic Drop data (malformed config file - cannot parse # symbol): " + entry);
                continue;
            }
            String pokemonName = parts[0].trim();
            String remaining = parts[1].trim();

            // Step 2: Separate the drop items list (everything after the first '[')
            int bracketStartIndex = remaining.indexOf('[');
            int bracketEndIndex = remaining.indexOf(']');
            if (bracketStartIndex == -1 || bracketEndIndex == -1) {
                // Skip invalid format if brackets are missing
                VanillaCrossover.LOGGER.error("Cannot resolve a Periodic Drop data (malformed config file - [ and ] symbols missing/malformed): " + entry);
                continue;
            }

            // Extract the drop interval (seconds for drop) and the drop items part
            String dropIntervalStr = remaining.substring(0, bracketStartIndex).trim();
            int dropInterval = Integer.parseInt(dropIntervalStr); // Parse drop interval
            String dropItemsPart = remaining.substring(bracketStartIndex + 1, bracketEndIndex).trim();

            // Step 3: Parse the drop items
            String[] itemStrings = dropItemsPart.split(",");
            for (String itemString : itemStrings) {
                String[] itemParts = itemString.trim().split("\\|");
                if (itemParts.length == 3) {
                    String itemType = itemParts[0].trim();
                    int minDropped = Integer.parseInt(itemParts[1].trim());
                    int maxDropped = Integer.parseInt(itemParts[2].trim());

                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemType));

                    RegistryValue<Species> foundSpecies = PixelmonSpecies.fromName(pokemonName.toUpperCase());
                    if (foundSpecies != null && foundSpecies.getValue().isPresent()) {
                        Species foundSpeciesResolved = foundSpecies.getValue().get();

                        if (!drops.containsKey(foundSpeciesResolved)) {
                            drops.put(foundSpecies.getValue().get(), new ArrayList<>());
                        }

                        PeriodicDropData addition = new PeriodicDropData(item, minDropped, maxDropped, dropInterval);
                        drops.get(foundSpeciesResolved).add(addition);
                    }
                    else {
                        VanillaCrossover.LOGGER.error("Cannot resolve a Periodic Drop data (malformed config file - cannot read Species name): " + entry);
                        break;
                    }
                }
            }

        }

        random = new Random();

        // Track all entities that have a species in this list
        tracker = new PixelmonEntityTracker<>(drops.keySet());
        tracker.SetAddEvent(PeriodicDropping::OnEntitySpawned);
        tracker.SetTickEvent(PeriodicDropping::Tick);
    }

    public static void OnEntitySpawned(PixelmonEntity entity) {
        Pokemon pokemon = entity.getPokemon();

        // Initialize entity data
        PeriodicDropData data = getPeriodicDropData(pokemon.getSpecies());
        tracker.SetEntityData(entity, data.dropTicks);
    }

    public static void Tick(PixelmonEntity entity) {
        int ticksRemaining = tracker.GetEntityData(entity);
        ticksRemaining--;

        if (ticksRemaining <= 0) {
            PeriodicDropData data = getPeriodicDropData(entity.getPokemon().getSpecies());
            ItemStack toDrop = data.getDrop();

            BlockPos pos = entity.blockPosition();
            InventoryHelper.dropItemStack(entity.level, pos.getX(), pos.getY(), pos.getZ(), toDrop);

            ticksRemaining = data.dropTicks;
        }

        tracker.SetEntityData(entity, ticksRemaining);
    }

    private static PeriodicDropData getPeriodicDropData(Species species) {
        int randIndex = random.nextInt(drops.get(species).size());
        return drops.get(species).get(randIndex);
    }
}
