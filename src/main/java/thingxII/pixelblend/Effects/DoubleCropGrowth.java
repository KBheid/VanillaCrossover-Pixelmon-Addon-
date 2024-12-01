package thingxII.pixelblend.Effects;

import Core.PixelmonEntityTracker;
import com.google.common.collect.Iterators;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import thingxII.pixelblend.Config.DoubleGrowthConfig;
import thingxII.pixelblend.ConfigProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DoubleCropGrowth {
    private static List<PixelmonEntityTracker<Integer>> trackers = new ArrayList<>();
    private static final Random random = new Random();

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        for (DoubleGrowthConfig.DoubleGrowthPokemonConfiguration config : ConfigProxy.getDoubleGrowthConfig().getConfigs()) {
            PixelmonEntityTracker<Integer> newTracker = new PixelmonEntityTracker<>(config.getPredicate().asPredicate());
            newTracker.SetDefaultDataSetter(e -> config.getRange());
            trackers.add(newTracker);
        }
    }

    @SubscribeEvent
    static void OnPlantGrowth(BlockEvent.CropGrowEvent.Post event) {
        boolean foundNearbyMon = Iterators.any(trackers.iterator(), tracker -> {
            return Iterators.any(tracker.getTrackedEntities().iterator(), entity -> {
                int dist = manhattanDistance(entity.blockPosition(), event.getPos());
                return dist <= tracker.GetEntityData(entity);
            });
        });

        if (foundNearbyMon && event.getState().getBlock() instanceof CropsBlock) {
            CropsBlock cropBlock = (CropsBlock) event.getState().getBlock();

            Property<Integer> ageProperty = cropBlock.getAgeProperty();
            int currentAge = event.getState().getValue(ageProperty);
            int maxAge = cropBlock.getMaxAge();

            // If the crop is not fully grown, try to grow it
            if (currentAge < maxAge) {
                BlockState newCropState = event.getState().setValue(ageProperty, currentAge + 1);

                event.getWorld().setBlock(event.getPos(), newCropState, 2);

                // VFX
                spawnBonemealParticles((ServerWorld) event.getWorld(), event.getPos(), random.nextInt(5));
            }
        }
    }

    // Shameless
    private static void spawnBonemealParticles(World worldIn, BlockPos pos, int amount) {
        if (!worldIn.isClientSide) {
            ServerWorld world = (ServerWorld)worldIn;
            if (amount == 0) {
                amount = 15;
            }

            BlockState iblockstate = worldIn.getBlockState(pos);
            int i;
            double d0;
            double d1;
            double d2;
            if (iblockstate.getMaterial() != Material.AIR) {
                for(i = 0; i < amount; ++i) {
                    d0 = random.nextGaussian() * 0.02;
                    d1 = random.nextGaussian() * 0.02;
                    d2 = random.nextGaussian() * 0.02;
                    world.sendParticles(ParticleTypes.HAPPY_VILLAGER, (double)((float)pos.getX() + random.nextFloat()), (double)pos.getY() + (double)random.nextFloat() * iblockstate.getShape(worldIn, pos).max(Direction.Axis.Y), (double)((float)pos.getZ() + random.nextFloat()), 1, d0, d1, d2, 0.0);
                }
            } else {
                for(i = 0; i < amount; ++i) {
                    d0 = random.nextGaussian() * 0.02;
                    d1 = random.nextGaussian() * 0.02;
                    d2 = random.nextGaussian() * 0.02;
                    world.sendParticles(ParticleTypes.HAPPY_VILLAGER, (double)((float)pos.getX() + random.nextFloat()), (double)pos.getY() + (double)random.nextFloat() * 1.0, (double)((float)pos.getZ() + random.nextFloat()), 1, d0, d1, d2, 0.0);
                }
            }
        }

    }

    public static int manhattanDistance(BlockPos pos1, BlockPos pos2) {
        // Calculate the Manhattan distance as the sum of the absolute differences
        return Math.abs(pos1.getX() - pos2.getX()) +
                Math.abs(pos1.getY() - pos2.getY()) +
                Math.abs(pos1.getZ() - pos2.getZ());
    }
}
