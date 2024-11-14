package thingxII.vanillacrossover.Effects.PeriodicDropping;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class DropData {
    public final Item item;
    public final int minDrop;
    public final int maxDrop;

    public DropData(Item i, int min, int max) {
        item = i;
        minDrop = min;
        maxDrop = max;
    }

    public ItemStack getDrop() {
        Random r = new Random();
        int randCount = r.nextInt(maxDrop) + minDrop;

        return new ItemStack(item, randCount);
    }
}