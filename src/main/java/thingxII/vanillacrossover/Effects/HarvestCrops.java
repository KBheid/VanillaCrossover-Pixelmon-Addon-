package thingxII.vanillacrossover.Effects;

import Core.PixelmonEntityPredicateBuilder;
import Core.PixelmonTrackerCooldownWithData;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import thingxII.vanillacrossover.ConfigProxy;
import thingxII.vanillacrossover.Config.HarvestEffectConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class HarvestCrops {
    private static final int TICKS_TO_ACTIVATE = 20;
    // total area is (range*2+1)x(range*2+1) area - so range of 4 would be 9x9
    private static final int MAX_RANGE = 4;
    private static final int MIN_RANGE = 0;

    private static List<PixelmonTrackerCooldownWithData<Integer>> tracker = new ArrayList<>();

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        List<HarvestEffectConfig.HarvestPokemonConfiguration> configs = ConfigProxy.getHarvestEffectConfig().getConfigs();

        for (HarvestEffectConfig.HarvestPokemonConfiguration config : configs) {
            Predicate<PixelmonEntity> acceptedEntities = config.getPredicate().asPredicate();

            PixelmonTrackerCooldownWithData<Integer> newTracker = new PixelmonTrackerCooldownWithData<>(acceptedEntities, TICKS_TO_ACTIVATE, true);
            newTracker.SetCooldownFunction(e -> config.getCooldown());
            newTracker.SetDefaultCustomData(HarvestCrops::calcData);
            newTracker.SetCooldownComplete(HarvestCrops::harvestNearby);

            tracker.add(newTracker);
        }
    }

    private static Integer calcData(PixelmonEntity entity) {
        int speedEV = entity.getPokemon().getEVs().getStat(BattleStatsType.SPEED);
        int speedIV = entity.getPokemon().getIVs().getStat(BattleStatsType.SPEED);

        // IVs are 50% and EVs are 50%
        int distance = (int) ((speedEV / 252.0f + speedIV / 31.0f) * (MAX_RANGE - MIN_RANGE)) + MIN_RANGE;

        return distance;
    }

    private static Void harvestNearby(PixelmonEntity entity, Integer maxDistance) {
        Iterable<BlockPos> blockPositions = BlockPos.withinManhattan(entity.blockPosition(), maxDistance, maxDistance, 2);
        for (BlockPos blockPos : blockPositions) {
            BlockState state = entity.level.getBlockState(blockPos);
            if (state.getBlock() instanceof CropsBlock) {
                CropsBlock crop = (CropsBlock) state.getBlock();
                if (crop.isMaxAge(state)) {
                    entity.level.destroyBlock(blockPos, true, entity);
                }
            }
        }

        return null;
    }
}
