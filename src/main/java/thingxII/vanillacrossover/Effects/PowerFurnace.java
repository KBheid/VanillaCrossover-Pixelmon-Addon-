package thingxII.vanillacrossover.Effects;

import Core.PixelmonTrackerCooldownWithData;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import thingxII.vanillacrossover.Config.PowerFurnaceConfig;
import thingxII.vanillacrossover.ConfigProxy;
import thingxII.vanillacrossover.mixin.Patch_FurnaceDataAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PowerFurnace {
    // total area is (range*2+1)x(range*2+1) area - so range of 4 would be 9x9
    private static final int MAX_RANGE = 5;
    private static final int MIN_RANGE = 3;

    private static List<PixelmonTrackerCooldownWithData<PowerFurnaceConfig.EntityConfig>> trackers = new ArrayList<>();

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        List<PowerFurnaceConfig.EntityConfig> configs = ConfigProxy.getPowerFurnaceConfig().getConfigs();

        for (PowerFurnaceConfig.EntityConfig config : configs) {
            Predicate<PixelmonEntity> acceptedEntities = config.getPredicate().asPredicate();

            PixelmonTrackerCooldownWithData<PowerFurnaceConfig.EntityConfig> newTracker = new PixelmonTrackerCooldownWithData<>(acceptedEntities, 0, true);
            newTracker.SetDefaultCustomData(e -> config);
            newTracker.SetCooldownWithDataFunction(PowerFurnace::getCooldownTicks);
            newTracker.SetCooldownComplete(PowerFurnace::cooldownElapsed);

            trackers.add(newTracker);
        }
    }

    private static int getCooldownTicks(PixelmonEntity entity, PowerFurnaceConfig.EntityConfig config) {
        float min = config.getMinCooldownDuration();
        float max = config.getMaxCooldownDuration();

        float scalar = config.getCooldownScalarCalculation().getForEntity(entity);
        float inverseScalar = config.getCooldownScalarCalculation().getUpperBound() - scalar;

        float finalValue = inverseScalar * (max - min) + min;

        return (int) (finalValue * 20.0f);
    }

    private static int getRange(PixelmonEntity entity, PowerFurnaceConfig.EntityConfig config) {
        float scalar = config.getPowerScalarCalculation().getForEntity(entity);
        return (int) ((MAX_RANGE - MIN_RANGE) * scalar + MIN_RANGE);
    }

    private static float getFurnacePower(PixelmonEntity entity, PowerFurnaceConfig.EntityConfig config) {
        float min = config.getMinFurnacePower();
        float max = config.getMaxFurnacePower();

        float scalar = config.getPowerScalarCalculation().getForEntity(entity);

        return (max - min) * scalar + min;
    }

    private static Void cooldownElapsed(PixelmonEntity entity, PowerFurnaceConfig.EntityConfig config) {
        int range = getRange(entity, config);
        float power = getFurnacePower(entity, config);

        Iterable<BlockPos> blockPositions = BlockPos.withinManhattan(entity.blockPosition(), range, range, range);
        for (BlockPos blockPos : blockPositions) {
            TileEntity tileEntity = entity.level.getBlockEntity(blockPos);
            if (tileEntity instanceof AbstractFurnaceTileEntity) {
                AbstractFurnaceTileEntity furnaceTileEntity = (AbstractFurnaceTileEntity) tileEntity;
                Patch_FurnaceDataAccessor accessor = (Patch_FurnaceDataAccessor) furnaceTileEntity;

                int currentTimeLeft = accessor.getDataAccess().get(0);
                accessor.getDataAccess().set(0, (int) (currentTimeLeft + power * 20.0f));
                entity.level.setBlock(blockPos, entity.level.getBlockState(blockPos).setValue(AbstractFurnaceBlock.LIT, true), 3);
            }
        }

        return null;
    }
}
