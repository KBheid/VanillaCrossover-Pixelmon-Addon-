package thingxII.vanillacrossover.Effects;

import Core.PixelmonTrackerCooldownWithData;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import thingxII.vanillacrossover.Config.CreateAndPlantSeedConfig;
import thingxII.vanillacrossover.ConfigProxy;

import java.util.ArrayList;
import java.util.List;

public class CreateAndPlantSeeds {
    private static final int TICKS_TO_ACTIVATE = 20;

    // Tracker data holds ticks until next activation
    private static List<PixelmonTrackerCooldownWithData<Integer>> trackers = new ArrayList<>();

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        for (CreateAndPlantSeedConfig.EntityConfig config : ConfigProxy.getCreateAndPlantSeedConfig().getConfigs()) {
            PixelmonTrackerCooldownWithData<Integer> newTracker = new PixelmonTrackerCooldownWithData<>(config.getPredicate().asPredicate(), TICKS_TO_ACTIVATE, true);
            newTracker.SetDefaultCustomData(e -> config.getRange());
            newTracker.SetCooldownComplete(CreateAndPlantSeeds::plantUnderFeet);

            trackers.add(newTracker);
        }
    }

    private static Void plantUnderFeet(PixelmonEntity entity, Integer range) {
        Iterable<BlockPos> blockPositions = BlockPos.withinManhattan(entity.blockPosition(), range, range, 2);
        for (BlockPos blockPos : blockPositions) {
            BlockState airAboveFarmlandState = entity.level.getBlockState(blockPos);
            BlockState farmlandState = entity.level.getBlockState(blockPos.below());
            if (farmlandState.getBlock().isFertile(farmlandState, entity.level, blockPos.below()) && airAboveFarmlandState.getBlock().is(Blocks.AIR)) {
                BlockState newCropState = Blocks.WHEAT.defaultBlockState();
                entity.level.setBlock(blockPos, newCropState, 2);
            }
        }

        return null;
    }
}