package Core;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryHelper {
    public static List<ItemStack> copyInventory(IInventory source, IInventory destination) {
        // Ensure both inventories are not null
        if (source == null || destination == null) {
            return new ArrayList<ItemStack>();
        }

        ArrayList<ItemStack> overflow;
        if (source.getContainerSize() > destination.getContainerSize()) {
            overflow = new ArrayList<>(source.getContainerSize() - destination.getContainerSize());
        }
        else {
            overflow = new ArrayList<>();
        }

        // Loop through the source inventory
        for (int i = 0; i < source.getContainerSize(); i++) {
            ItemStack sourceStack = source.getItem(i);
            if (i > destination.getContainerSize() - 1) {
                overflow.add(sourceStack);
                continue;
            }

            // Create a copy of the item stack from the source
            ItemStack stackToCopy = sourceStack.copy();

            // Set the copied stack into the destination inventory
            if (!stackToCopy.isEmpty()) {
                destination.setItem(i, stackToCopy);
            } else {
                destination.setItem(i, ItemStack.EMPTY); // Clear the slot if the source is empty
            }
        }

        return overflow;
    }
}
