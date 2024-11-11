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
import thingxII.vanillacrossover.PixelmonTrackerCooldown;

import java.util.Arrays;
import java.util.List;

public class Harvest_HarvestCrops {
    private static final int MAX_RANGE = 5;
    private static final int TICKS_TO_ACTIVATE = 20;

    private static PixelmonTrackerCooldown tracker;

    // TODO: Configuration
    private static final List<Ability> abilities = Arrays.asList(AbilityRegistry.HARVEST.get());

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        tracker = new PixelmonTrackerCooldown(p -> abilities.contains(p.getPokemon().getAbility()), TICKS_TO_ACTIVATE, true);
        tracker.SetCooldownComplete(Harvest_HarvestCrops::harvestNearby);
    }

    private static void harvestNearby(PixelmonEntity entity) {
        int maxDistance = TICKS_TO_ACTIVATE;
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
