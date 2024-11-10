package thingxII.vanillacrossover.StoragePokemon;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import thingxII.vanillacrossover.ContainerRegistration;

public class PokemonStorageContainer extends Container {
    public final PokemonStorage storage;

    // Creates the container on client-side
    public PokemonStorageContainer(int windowId, PlayerInventory inventoryPlayer, PacketBuffer buffer) {
        this(windowId, inventoryPlayer, PokemonStorage.fromBuffer(buffer));
    }

    // Creates the container for both client-side and server-side
    public PokemonStorageContainer(int windowId, PlayerInventory playerInv, PokemonStorage storage) {
        super(ContainerRegistration.STORAGE_POKEMON_CONTAINER_TYPE, windowId);

        this.storage = storage;
        storage.inventory.startOpen(playerInv.player);

        // Add the players hotbar to the gui - the [xpos, ypos] location of each item
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            int slotNumber = x;
            addSlot(new Slot(playerInv, slotNumber, storage.data.playerHotbarX + SLOT_X_SPACING * x, storage.data.playerHotbarY));
        }

        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = storage.data.playerInventoryX + x * SLOT_X_SPACING;
                int ypos = storage.data.playerInventoryY + y * SLOT_Y_SPACING;

                addSlot(new Slot(playerInv, slotNumber, xpos, ypos));
            }
        }

        for (int y = 0; y < storage.data.numberOfRows; y++) {
            for (int x = 0; x < storage.data.slotsPerRow; x++) {
                int slotNumber = y * storage.data.slotsPerRow + x;
                int xpos = storage.data.storageInventoryX + x * SLOT_X_SPACING;
                int ypos = storage.data.storageInventoryY + y * SLOT_Y_SPACING;

                addSlot(new Slot(storage.inventory, slotNumber, xpos, ypos));
            }
        }

    }

    @Override
    public void removed(PlayerEntity player) {
        super.removed(player);
        storage.inventory.stopOpen(player);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        super.setItem(slot, itemStack);
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity p_82846_1_, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotIndex < storage.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, storage.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, storage.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)

    private static final int SLOT_X_SPACING = 18;   // Width of a slot
    private static final int SLOT_Y_SPACING = 18;   // Height of a slot

    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;

    private static final int HOTBAR_SLOT_COUNT = 9;

    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

}
