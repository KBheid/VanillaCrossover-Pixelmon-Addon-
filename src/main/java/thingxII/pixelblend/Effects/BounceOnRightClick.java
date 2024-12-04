package thingxII.pixelblend.Effects;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
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
import thingxII.pixelblend.Config.BounceConfig;
import thingxII.pixelblend.ConfigProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class BounceOnRightClick {
    private static final List<ServerPlayerEntity> playersWithNegatedFall = new ArrayList<>();
    private static HashMap<Predicate<PixelmonEntity>, BounceConfig.EntityConfig> configs = new HashMap<>();

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event) {
        // Evaluate our predicates ASAP so we're not constructing them every time
        List<BounceConfig.EntityConfig> foundConfigs = ConfigProxy.getBounceConfig().getConfigs();
        for (BounceConfig.EntityConfig config : foundConfigs) {
            configs.put(config.getPredicate().asPredicate(), config);
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

        for (Predicate<PixelmonEntity> predicate : configs.keySet()) {
            if (!predicate.test(pixelmonEntity)) {
                continue;
            }

            BounceConfig.EntityConfig config = configs.get(predicate);

            // Only allow the bounce effect if the player is not already moving upward (so they can't spam it very, very quickly)
            Vector3d prevMovement = player.getDeltaMovement();
            if (prevMovement.y > 0.5f) {
                return;
            }

            float statScalar = config.getScalarCalculation().getForEntity(pixelmonEntity);
            float forwardForce = (statScalar * (config.getMaxForwardForce() - config.getMinForwardForce())) + config.getMinForwardForce();
            float upwardForce = (statScalar * (config.getMaxUpwardForce() - config.getMinUpwardForce())) + config.getMinUpwardForce();

            Vector3d playerForward = Vector3d.directionFromRotation(player.getRotationVector());
            double xForce = playerForward.x * forwardForce;
            double zForce = playerForward.z * forwardForce;

            player.setDeltaMovement(prevMovement.add(xForce, upwardForce, zForce));
            player.hurtMarked = true;
            playersWithNegatedFall.add(player);

            break;
        }
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
