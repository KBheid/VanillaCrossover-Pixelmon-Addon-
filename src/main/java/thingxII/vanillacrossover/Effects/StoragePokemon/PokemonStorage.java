package thingxII.vanillacrossover.Effects.StoragePokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thingxII.vanillacrossover.Config.ChestablePokemonConfig;
import Core.InventoryHelper;
import thingxII.vanillacrossover.InventoryStorageData;
import thingxII.vanillacrossover.StorageType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// This class is the storage for a particular storage pokemon.
public class PokemonStorage {
    public InventoryStorageData data = null;

    public UUID uuid;
    public Pokemon referencedMon;
    public Inventory inventory;

    public StorageType storageType;

    private List<ItemStack> itemsToSpill = null;

    private PokemonStorage() { }

    PokemonStorage(Pokemon owner) {
        referencedMon = owner;
        uuid = owner.getUUID();
        calculateSize();

        inventory = new Inventory(data.slotCount);
    }

    public void updateReference(Pokemon newOwner) {
        referencedMon = newOwner;
    }

    public void calculateSize() {
        boolean anyMaxxed = false;

        // base size is one
        int size = 1;

        // config error correction
        int evListLength = Math.min(Math.min(ChestablePokemonConfig.chestSizeEvs.size(), ChestablePokemonConfig.chestSizeEvsValues.size()), 6);
        int ivListLength = Math.min(Math.min(ChestablePokemonConfig.chestSizeIvs.size(), ChestablePokemonConfig.chestSizeIvsValues.size()), 6);

        // Check EVs
        // each EV can add +1
        for (int statIndex = 0; statIndex < evListLength; statIndex++) {
            boolean shouldCheck = ChestablePokemonConfig.chestSizeEvs.get(statIndex);
            if (!shouldCheck) {
                continue;
            }

            BattleStatsType EVStat = BattleStatsType.EV_IV_STATS[statIndex];
            int EVValue = referencedMon.getEVs().getStat(EVStat);

            if (EVValue >= ChestablePokemonConfig.chestSizeEvsValues.get(statIndex)) {
                size++;
            }
            if (EVValue >= 252 && ChestablePokemonConfig.maxEVsIncreases) {
                anyMaxxed = true;
            }
        }

        // Check IVs
        // each IV can add +1
        for (int statIndex = 0; statIndex < ivListLength; statIndex++) {
            boolean shouldCheck = ChestablePokemonConfig.chestSizeIvs.get(statIndex);
            if (!shouldCheck) {
                continue;
            }

            BattleStatsType IVStat = BattleStatsType.EV_IV_STATS[statIndex];
            int IVValue = referencedMon.getIVs().getStat(IVStat);

            if (IVValue >= ChestablePokemonConfig.chestSizeIvsValues.get(statIndex)) {
                size++;
            }
            if (IVValue >= 252 && ChestablePokemonConfig.maxIVsIncreases) {
                anyMaxxed = true;
            }
        }

        // Check dynamax
        // Can add +1
        if (ChestablePokemonConfig.dynamaxIncreases) {
            int dynamaxLevel = referencedMon.getDynamaxLevel();
            if (dynamaxLevel >= ChestablePokemonConfig.dynamaxValue) {
                size++;
            }
            if (dynamaxLevel >= 10 && ChestablePokemonConfig.maxDynamaxIncreases) {
                anyMaxxed = true;
            }
        }

        // Check Growth
        // Can add +1 or more, depending on settings
        if (ChestablePokemonConfig.growthIncreases) {
            int pokemonSize = referencedMon.getGrowth().ordinal();

            if (ChestablePokemonConfig.growthDiffIncreases && pokemonSize > ChestablePokemonConfig.growthReferenceValue) {
                size += pokemonSize - ChestablePokemonConfig.growthReferenceValue;
            }
            else {
                if (pokemonSize >= ChestablePokemonConfig.growthReferenceValue) {
                    size++;
                }
            }

            if (pokemonSize == 8 && ChestablePokemonConfig.maxGrowthIncreases) {
                anyMaxxed = true;
            }
        }

        // and +1 for any of those maxxed
        if (anyMaxxed) {
            size++;
        }

        // Cap to 5
        size = MathHelper.clamp(size, 1, 5);

        switch (size) {

            case 2:
                data = InventoryStorageData.Data.get(StorageType.SMALL);
                storageType = StorageType.SMALL;
                break;

            case 3:
                data = InventoryStorageData.Data.get(StorageType.MEDIUM);
                storageType = StorageType.MEDIUM;
                break;

            case 4:
                data = InventoryStorageData.Data.get(StorageType.LARGE);
                storageType = StorageType.LARGE;
                break;

            case 5:
                data = InventoryStorageData.Data.get(StorageType.VERY_LARGE);
                storageType = StorageType.VERY_LARGE;
                break;

            case 1:
            default:
                data = InventoryStorageData.Data.get(StorageType.VERY_SMALL);
                storageType = StorageType.VERY_SMALL;
        }

        if (inventory != null && inventory.getContainerSize() != data.slotCount) {
            Inventory newInventory = new Inventory(data.slotCount);
            itemsToSpill = InventoryHelper.copyInventory(inventory, newInventory);

            inventory = newInventory;
        }
    }

    // Find the pokemon within the input storage and link us up.
    public boolean findMonInPokes(Pokemon[] allPokes) {
        Optional<Pokemon> foundPokemon = Arrays.stream(allPokes).filter(pokemon -> pokemon != null && pokemon.getUUID().equals(uuid)).findFirst();;

        if (foundPokemon.isPresent()) {
            referencedMon = foundPokemon.get();
            return true;
        }
        else {
            return false;
        }
    }

    public void ExecutePendingSpillAt(World world, double x, double y, double z) {
        if (itemsToSpill == null) {
            return;
        }

        for (ItemStack itemStack : itemsToSpill) {
            net.minecraft.inventory.InventoryHelper.dropItemStack(world, x, y, z, itemStack);
        }
        itemsToSpill = null;
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        nbt.putUUID("uuid", referencedMon.getUUID());
        nbt.putInt("slotCount", inventory.getContainerSize());
        ListNBT items = new ListNBT();

        for(int slotIndex = 0; slotIndex < this.inventory.getContainerSize(); ++slotIndex) {
            ItemStack currentItemStack = this.inventory.getItem(slotIndex);
            if (!currentItemStack.isEmpty()) {
                CompoundNBT itemCompoundTag = new CompoundNBT();
                itemCompoundTag.putByte("slot", (byte)slotIndex);
                currentItemStack.save(itemCompoundTag);
                items.add(itemCompoundTag);
            }
        }
        nbt.put("items", items);

        return nbt;
    }

    public static PokemonStorage fromNBT(CompoundNBT nbt) {
        PokemonStorage createdStorage = new PokemonStorage();
        createdStorage.uuid = nbt.getUUID("uuid");
        createdStorage.inventory = new Inventory(nbt.getInt("slotCount"));

        ListNBT items = nbt.getList("items", 10);

        for(int listIndex = 0; listIndex < items.size(); ++listIndex) {
            CompoundNBT itemNBT = items.getCompound(listIndex);
            int slotIndex = itemNBT.getByte("slot") & 255;
            if (slotIndex < createdStorage.inventory.getContainerSize()) {
                createdStorage.inventory.setItem(slotIndex, ItemStack.of(itemNBT));
            }
        }

        return createdStorage;
    }

    public static PokemonStorage fromBuffer(PacketBuffer buffer) {
        CompoundNBT compound = buffer.readNbt();
        PokemonStorage storage = fromNBT(compound);
        storage.storageType = buffer.readEnum(StorageType.class);
        storage.data = InventoryStorageData.Data.get(storage.storageType);
        return storage;
    }

    public void writeToBuffer(PacketBuffer buffer) {
        CompoundNBT compound = new CompoundNBT();
        writeToNBT(compound);
        buffer.writeNbt(compound);
        buffer.writeEnum(storageType);
    }
}
