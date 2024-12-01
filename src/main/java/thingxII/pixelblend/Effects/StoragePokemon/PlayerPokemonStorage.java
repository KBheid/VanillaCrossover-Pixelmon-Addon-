package thingxII.pixelblend.Effects.StoragePokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.ArrayUtils;
import thingxII.pixelblend.Config.StorageConfig;
import thingxII.pixelblend.PixelBlend;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

// This class is the storage for a player - it includes ALL of their storage pokemon
public class PlayerPokemonStorage {
    private static HashMap<UUID, PlayerPokemonStorage> cachedStorages = new HashMap<>();

    private UUID playerUUID;
    private File saveFile;
    private final HashMap<UUID, PokemonStorage> pokemonStorages = new HashMap<>();

    PlayerPokemonStorage() {
    }

    @SubscribeEvent
    static void onServerStopping(FMLServerStoppingEvent event) {
        for (PlayerPokemonStorage storage : cachedStorages.values()) {
            try {
                CompressedStreamTools.write(storage.writeToNBT(new CompoundNBT()), storage.saveFile);
            } catch (IOException e) {
                PixelBlend.LOGGER.error("Unable to open file: " + storage.saveFile.getAbsolutePath() + " to save pokemon storage data.");
                e.printStackTrace();
            }
        }
        cachedStorages.clear();
    }

    public boolean hasStorageForMon(Pokemon owner) {
        return pokemonStorages.containsKey(owner.getUUID());
    }

    public PokemonStorage getOrCreateStorageForMon(Pokemon owner, StorageConfig.EntityConfig foundEntityConfig) {
        if (pokemonStorages.containsKey(owner.getUUID())) {
            return pokemonStorages.get(owner.getUUID());
        }

        PokemonStorage newStorage = new PokemonStorage(owner, foundEntityConfig);
        pokemonStorages.put(owner.getUUID(), newStorage);

        return newStorage;
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        nbt.putUUID("playerUUID", playerUUID);

        ListNBT pokemonStorages = new ListNBT();
        for (PokemonStorage storage : this.pokemonStorages.values()) {
            CompoundNBT pokemonData = new CompoundNBT();

            storage.writeToNBT(pokemonData);

            pokemonStorages.add(pokemonData);
        }

        nbt.put("pokemon", pokemonStorages);

        return nbt;
    }

    public static PlayerPokemonStorage fromNBT(CompoundNBT nbt) {
        PlayerPokemonStorage createdStorage = new PlayerPokemonStorage();

        createdStorage.playerUUID = nbt.getUUID("playerUUID");
        ListNBT items = nbt.getList("pokemon", 10);

        com.pixelmonmod.pixelmon.api.storage.PokemonStorage partyStorage = StorageProxy.getParty(createdStorage.playerUUID);
        com.pixelmonmod.pixelmon.api.storage.PokemonStorage pcStorage = StorageProxy.getPCForPlayer(createdStorage.playerUUID);

        Pokemon[] allPokes = ArrayUtils.addAll(partyStorage.getAll(), pcStorage.getAll());

        for(int listIndex = 0; listIndex < items.size(); ++listIndex) {
            CompoundNBT itemNBT = items.getCompound(listIndex);

            PokemonStorage pds = PokemonStorage.fromNBT(itemNBT);
            boolean pokemonExists = pds.findMonInPokes(allPokes);
            if (pokemonExists) {
                createdStorage.pokemonStorages.put(pds.uuid, pds);
            }
        }

        return createdStorage;

    }

    public static PlayerPokemonStorage getStorageFor(ServerPlayerEntity player) {
        // If cached, get cached storage
        if (cachedStorages.containsKey(player.getUUID())) {
            return cachedStorages.get(player.getUUID());
        }
        else {
            // Otehrwise create or load it
            File nbtFile = getStorageFileForUUID(player.getUUID());

            // Load the cached storage from NBT if it exists
            if (nbtFile.exists()) {
                try {
                    CompoundNBT LoadedNBT = CompressedStreamTools.read(nbtFile);

                    PlayerPokemonStorage dps = fromNBT(LoadedNBT);
                    cachedStorages.put(player.getUUID(), dps);

                    dps.playerUUID = player.getUUID();
                    dps.saveFile = nbtFile;
                    return dps;

                } catch (IOException e) {
                    PixelBlend.LOGGER.error("Unable to open file: " + nbtFile.getAbsolutePath() + " to load pokemon storage data.");
                    e.printStackTrace();
                }
            }

            // Otherwise, create it and cache it
            else {
                try {
                    File parent = nbtFile.getParentFile();
                    parent.mkdirs();

                    nbtFile.createNewFile();
                    CompoundNBT addedNBT = new CompoundNBT();
                    addedNBT.put("", new CompoundNBT());

                    // Write an empty NBT tag - we'll populate this with their storages upon saving
                    CompressedStreamTools.write(addedNBT, nbtFile);

                    PlayerPokemonStorage newStorage = new PlayerPokemonStorage();
                    cachedStorages.put(player.getUUID(), newStorage);

                    newStorage.playerUUID = player.getUUID();
                    newStorage.saveFile = nbtFile;

                    return newStorage;

                } catch (IOException e) {
                    PixelBlend.LOGGER.error("Unable to create file: " + nbtFile.getAbsolutePath() + " to save pokemon storage data.");
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static File getStorageFileForUUID(UUID playerUUID) {
        File nbtFile;
        if (ServerLifecycleHooks.getCurrentServer().isDedicatedServer()) {
            nbtFile = new File(ServerLifecycleHooks.getCurrentServer().getWorldData().getLevelName() + "/data/donkeymon/", playerUUID.toString() + ".donkeydat");
        }
        else {
            String levelName = ServerLifecycleHooks.getCurrentServer().getWorldData().getLevelName();
            nbtFile  = new File("saves/" + levelName + "/data/donkeymon/", playerUUID.toString() + ".donkeydat");
        }

        return nbtFile;
    }
}
