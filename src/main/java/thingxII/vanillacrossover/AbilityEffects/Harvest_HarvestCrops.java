package thingxII.vanillacrossover.AbilityEffects;

import Core.PixelmonEntityTracker;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.util.Arrays;
import java.util.List;

public class Harvest_HarvestCrops {
    private static final int MAX_RANGE = 5;
    private static final int TICKS_TO_ACTIVATE = 20;

    // Tracker data holds ticks until next activation
    private static PixelmonEntityTracker<Integer> tracker;
    private static List<Ability> abilities = Arrays.asList(AbilityRegistry.HARVEST.get());

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        tracker = new PixelmonEntityTracker<>(p -> abilities.contains(p.getPokemon().getAbility()));
        tracker.SetAddEvent(Harvest_HarvestCrops::onPokemonAdded);
        tracker.SetTickEvent(Harvest_HarvestCrops::tick);
    }

    private static void onPokemonAdded(PixelmonEntity entity) {
        tracker.SetEntityData(entity, TICKS_TO_ACTIVATE);
    }

    private static void tick(PixelmonEntity entity) {
        Integer ticksRemaining = tracker.GetEntityData(entity);
        ticksRemaining--;

        if (ticksRemaining <= 0) {
            ticksRemaining = TICKS_TO_ACTIVATE;
            harvestNearby(entity, MAX_RANGE);
        }

        tracker.SetEntityData(entity, ticksRemaining);
    }

    private static void harvestNearby(PixelmonEntity entity, int maxDistance) {
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
    }
}
