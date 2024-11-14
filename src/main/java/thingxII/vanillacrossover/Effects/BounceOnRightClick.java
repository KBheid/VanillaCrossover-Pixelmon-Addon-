package thingxII.vanillacrossover.Effects;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.util.ArrayList;
import java.util.List;

public class BounceOnRightClick {
    private static final List<ServerPlayerEntity> playersWithNegatedFall = new ArrayList<>();
    private static Species ALLOWED_SPECIES = null;
    private static final float UPWARD_BOOST = 2;

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event) {
        if (PixelmonSpecies.SPOINK.getValue().isPresent()) {
            ALLOWED_SPECIES = PixelmonSpecies.SPOINK.getValue().get();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void playerInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
            return;
        }

        if (event.getCancellationResult() == ActionResultType.CONSUME)
        {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemStack itemStack = event.getItemStack();
        Entity entity = event.getTarget();
        Item item = itemStack.getItem();

        if (!(entity instanceof PixelmonEntity)) {
            return;
        }

        PixelmonEntity pixelmonEntity = (PixelmonEntity) entity;
        Pokemon pokemon = pixelmonEntity.getPokemon();

        if (pokemon == null || !(item.equals(Items.AIR))) {
            return;
        }

        if (pokemon.getSpecies() != ALLOWED_SPECIES) {
            return;
        }

        // Only allow the bounce effect if the player is not already moving upward (so they can't spam it very, very quickly)
        if (player.getDeltaMovement().y > 0.5f) {
            return;
        }

        // TODO:
        // 0.75 - 2.0 for boost, determined by sp. attack or something
        Vector3d prevMovement = player.getDeltaMovement();
        player.setDeltaMovement(prevMovement.add(0, UPWARD_BOOST, 0));
        player.hurtMarked = true;
        playersWithNegatedFall.add(player);
    }

    @SubscribeEvent
    public static void negatePlayerFallDamage(LivingFallEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity)) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();

        // Remove. If present, negate fall damage
        if (playersWithNegatedFall.remove(player)) {
            event.setCanceled(true);
        }
    }
}
