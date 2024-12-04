package thingxII.pixelblend.ItemEffects.StatusEffectItems;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.status.Burn;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import thingxII.pixelblend.Config.StatusItemsConfig;

import java.util.Random;

public class BlazePowderBurn {
    private static final int MIN_PARTICLES = 10;
    private static final int MAX_PARTICLES = 15;

    private static final float MAX_PARTICLE_VELOCITY = 0.35f;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void playerInteract(PlayerInteractEvent.EntityInteract event) {
        if (!StatusItemsConfig.allowStatusItems) {
            return;
        }

        if (event.getCancellationResult() == ActionResultType.CONSUME)
        {
            return;
        }

        if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemStack itemStack = event.getItemStack();

        if (itemStack.getItem() != Items.BLAZE_POWDER) {
            return;
        }

        // Add burn to pokemon and play effects if the pokemon isn't already burned
        if (event.getTarget() instanceof PixelmonEntity && event.getTarget().isAlive()) {
            Pokemon pokemon = ((PixelmonEntity) event.getTarget()).getPokemon();
            StatusPersist currentStatus = pokemon.getStatus();

            if (isAllowedToUseOn(player, pokemon) && !(currentStatus instanceof Burn)) {
                if (!player.level.isClientSide) {
                    pokemon.setStatus(new Burn());

                    if (player.gameMode.getGameModeForPlayer() != GameType.CREATIVE) {
                        itemStack.shrink(1);
                    }

                    // Play our sound effect
                    playSound(player.level, event.getTarget().blockPosition());

                    // Spawn random flame particles
                    if (player.level instanceof ServerWorld) {
                        Vector3d startPos = player.getEyePosition(1.0F);

                        Vector3d direction = Vector3d.directionFromRotation(player.getRotationVector());
                        Random rand = new Random();

                        int particleCount = rand.nextInt(MAX_PARTICLES - MIN_PARTICLES) + MIN_PARTICLES;

                        ServerWorld world = (ServerWorld) player.level;
                        world.<BasicParticleType>sendParticles(ParticleTypes.FLAME,
                                startPos.x, startPos.y, startPos.z,
                                particleCount,
                                direction.x, direction.y, direction.z,
                                MAX_PARTICLE_VELOCITY);

                        // Consume the event
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private static void playSound(World world, BlockPos pos) {
        Random random = new Random();
        world.playSound((PlayerEntity) null, pos, SoundEvents.FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
    }

    private static boolean isAllowedToUseOn(PlayerEntity user, Pokemon target) {
        PlayerEntity ownerPlayer = target.getOwnerPlayer();
        NPCTrainer ownerTrainer = target.getOwnerTrainer();

        if (ownerPlayer == user && StatusItemsConfig.allowBlazeOnOwned) {
            return true;
        }

        if (ownerPlayer != user && ownerPlayer != null && StatusItemsConfig.allowBlazeOnOtherPlayer) {
            return true;
        }

        if (target.getOwnerTrainer() != null && StatusItemsConfig.allowBlazeOnTrainer) {
            return true;
        }

        return StatusItemsConfig.allowBlazeOnWild;
    }
}
