package thingxII.vanillacrossover.Effects.StoragePokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import Core.InventoryHelper;
import thingxII.vanillacrossover.Config.StorageConfig;
import thingxII.vanillacrossover.ConfigProxy;
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
    StorageConfig.EntityConfig referencedConfig;
    public Pokemon referencedMon;
    public Inventory inventory;

    public StorageType storageType;

    private List<ItemStack> itemsToSpill = null;

    private PokemonStorage() { }

    PokemonStorage(Pokemon owner, StorageConfig.EntityConfig foundEntityConfig) {
        referencedMon = owner;
        uuid = owner.getUUID();
        calculateSize(foundEntityConfig);

        inventory = new Inventory(data.slotCount);
    }

    public void updateReference(Pokemon newOwner, StorageConfig.EntityConfig config) {
        referencedMon = newOwner;
        referencedConfig = config;
    }

    public void calculateSize(StorageConfig.EntityConfig foundEntityConfig) {
        int size = foundEntityConfig.getSizeCalculation().getForPokemon(referencedMon);

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

    // TODO: This is ugly - why does it live in storage
    public void inventoryEvent() {
        if (referencedConfig.getTogglePaletteWhenEmpty()) {
            String prefix = ConfigProxy.getStorageConfig().getPalettePrefix();
            boolean emptyInventory = inventory.isEmpty();

            if (emptyInventory) {
                // Try to set to non-storage palette
                String oldPalette = referencedMon.getPalette().getName();
                if (oldPalette.startsWith(prefix)) {
                    String newPalette = oldPalette.substring(prefix.length());
                    if (newPalette.startsWith("_")) {
                        newPalette = newPalette.substring(1);
                    }

                    if (newPalette.isEmpty()) {
                        newPalette = "none";
                    }

                    if (referencedMon.hasPalette(newPalette)) {
                        referencedMon.setPalette(newPalette);
                    }
                }
            }
            else {
                // Try to set to storage palette
                String paletteToTry;
                if (!referencedMon.getPalette().getName().equals("none")) {
                    paletteToTry = prefix + "_" + referencedMon.getPalette().getName();
                }
                else {
                    paletteToTry = prefix;
                }

                if (referencedMon.hasPalette(paletteToTry)) {
                    referencedMon.setPalette(paletteToTry);
                }
            }
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
