package thingxII.vanillacrossover.PokemonEffects.PeriodicDropping;

import net.minecraft.item.Item;

public class PeriodicDropData extends DropData {

    public final int dropTicks;

    // TODO: This is kind of stupid if we're not going to have any difference between tick intervals per drop data
    public PeriodicDropData(Item i, int min, int max, int tickInterval) {
        super(i, min, max);
        this.dropTicks = tickInterval*20;
    }
}
