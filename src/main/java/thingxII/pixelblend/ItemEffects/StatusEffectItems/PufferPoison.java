package thingxII.pixelblend.ItemEffects.StatusEffectItems;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.status.Poison;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import thingxII.pixelblend.Config.StatusItemsConfig;

import java.util.Random;

public class PufferPoison {

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

        if (itemStack.getItem() != Items.PUFFERFISH) {
            return;
        }

        // Add poison to pokemon and play effects if the pokemon isn't already poisoned
        if (event.getTarget() instanceof PixelmonEntity && event.getTarget().isAlive()) {
            Pokemon pokemon = ((PixelmonEntity) event.getTarget()).getPokemon();
            StatusPersist currentStatus = pokemon.getStatus();

            if (isAllowedToUseOn(player, pokemon) && !(currentStatus instanceof Poison)) {
                if (!player.level.isClientSide) {
                    pokemon.setStatus(new Poison());

                    if (player.gameMode.getGameModeForPlayer() != GameType.CREATIVE) {
                        itemStack.shrink(1);
                    }

                    // Play our sound effect
                    playSound(player.level, event.getTarget().blockPosition());

                    // Consume the event
                    event.setCanceled(true);
                }
            }
        }
    }

    private static void playSound(World world, BlockPos pos) {
        Random random = new Random();
        world.playSound((PlayerEntity) null, pos, SoundEvents.PLAYER_BURP, SoundCategory.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
    }

    private static boolean isAllowedToUseOn(PlayerEntity user, Pokemon target) {
        PlayerEntity ownerPlayer = target.getOwnerPlayer();
        NPCTrainer ownerTrainer = target.getOwnerTrainer();

        if (ownerPlayer == user && StatusItemsConfig.allowPufferOnOwned) {
            return true;
        }

        if (ownerPlayer != user && ownerPlayer != null && StatusItemsConfig.allowPufferOnOtherPlayer) {
            return true;
        }

        if (target.getOwnerTrainer() != null && StatusItemsConfig.allowPufferOnTrainer) {
            return true;
        }

        return StatusItemsConfig.allowPufferOnWild;
    }
}
