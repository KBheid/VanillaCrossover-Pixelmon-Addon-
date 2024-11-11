package thingxII.vanillacrossover.AbilityEffects;

import Core.PixelmonEntityTracker;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.util.Arrays;
import java.util.List;

public class SeedSower_PlantSeeds {
    private static final int MAX_RANGE = 1;
    private static final int TICKS_TO_ACTIVATE = 20;

    // Tracker data holds ticks until next activation
    private static PixelmonEntityTracker<Integer> tracker;

    // TODO: Configuration
    private static final List<Ability> abilities = Arrays.asList(AbilityRegistry.SEED_SOWER.get());

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        tracker = new PixelmonEntityTracker<>(p -> abilities.contains(p.getPokemon().getAbility()));
        tracker.SetAddEvent(SeedSower_PlantSeeds::onPokemonAdded);
        tracker.SetTickEvent(SeedSower_PlantSeeds::tick);
    }

    private static void onPokemonAdded(PixelmonEntity entity) {
        tracker.SetEntityData(entity, TICKS_TO_ACTIVATE);
    }

    private static void tick(PixelmonEntity entity) {
        Integer ticksRemaining = tracker.GetEntityData(entity);
        ticksRemaining--;

        if (ticksRemaining <= 0) {
            ticksRemaining = TICKS_TO_ACTIVATE;
            plantUnderFeet(entity);
        }

        tracker.SetEntityData(entity, ticksRemaining);
    }

    private static void plantUnderFeet(PixelmonEntity entity) {
        Iterable<BlockPos> blockPositions = BlockPos.withinManhattan(entity.blockPosition(), MAX_RANGE, MAX_RANGE, 2);
        for (BlockPos blockPos : blockPositions) {
            BlockState airAboveFarmlandState = entity.level.getBlockState(blockPos);
            BlockState farmlandState = entity.level.getBlockState(blockPos.below());
            if (farmlandState.getBlock().isFertile(farmlandState, entity.level, blockPos.below()) && airAboveFarmlandState.getBlock().is(Blocks.AIR)) {
                BlockState newCropState = Blocks.WHEAT.defaultBlockState();
                entity.level.setBlock(blockPos, newCropState, 2);
            }
        }
    }
}