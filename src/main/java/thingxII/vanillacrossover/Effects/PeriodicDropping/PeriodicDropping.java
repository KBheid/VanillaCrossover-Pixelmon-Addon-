package thingxII.vanillacrossover.Effects.PeriodicDropping;

import Core.PixelmonTrackerCooldownWithData;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import thingxII.vanillacrossover.Config.PeriodicDroppingConfig;
import thingxII.vanillacrossover.ConfigProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PeriodicDropping {
    private static Random random;
    private static List<PixelmonTrackerCooldownWithData<List<PeriodicDropData>>> trackers = new ArrayList<>();

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        random = new Random();

        for (PeriodicDroppingConfig.SpecConfig spec : ConfigProxy.getPeriodicDroppingConfig().getConfig()) {
            PixelmonTrackerCooldownWithData<List<PeriodicDropData>> newTracker = new PixelmonTrackerCooldownWithData<>(spec.getPredicate().asPredicate(), spec.getCooldown() * 20, true);
            newTracker.SetDefaultCustomData(e -> spec.getDropData());
            newTracker.SetCooldownComplete(PeriodicDropping::doDrop);
            trackers.add(newTracker);
        }
    }

    private static Void doDrop(PixelmonEntity entity, List<PeriodicDropData> data) {
        ItemStack toDrop = getPeriodicDropData(data).getDrop();

        BlockPos pos = entity.blockPosition();
        InventoryHelper.dropItemStack(entity.level, pos.getX(), pos.getY(), pos.getZ(), toDrop);

        return null;
    }

    private static PeriodicDropData getPeriodicDropData(List<PeriodicDropData> drops) {
        int randIndex = random.nextInt(drops.size());
        return drops.get(randIndex);
    }
}
