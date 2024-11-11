package thingxII.vanillacrossover.MoveEffects;

import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.ImmutableAttack;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import thingxII.vanillacrossover.PixelmonTrackerCooldown;

import java.util.Arrays;
import java.util.List;

public class SafetyTeleportEffect {
    // 3 minutes
    private static final int TICKS_TO_ACTIVATE = 3;// * 60 * 20;
    private static ImmutableAttack attack = null;

    private static PixelmonTrackerCooldown tracker;

    // TODO: Configuration
    private static final List<Attack> moves = Arrays.asList();

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        attack = AttackRegistry.ALLY_SWITCH.get();

        tracker = new PixelmonTrackerCooldown(
            p -> {
                for (Attack a : p.getPokemon().getMoveset().attacks) {
                    if (a != null && a.getActualMove().equals(attack)) {
                        return true;
                    }
                }
                return false;
            },
            TICKS_TO_ACTIVATE,
            false
            );

        tracker.SetTickEvent(SafetyTeleportEffect::tick);
    }

    private static void tick(PixelmonEntity entity) {
        if (tracker.CheckCooldown(entity)) {

            ServerPlayerEntity player = entity.getPokemon().getOwnerPlayer();
            if (player != null) {
                if (player.isInLava()) {
                    Vector3d entityPos = entity.position();
                    entity.setPos(player.getX(), player.getY(), player.getZ());
                    player.teleportTo(entityPos.x, entityPos.y, entityPos.z);

                    tracker.SetCooldown(entity);
                }
            }
        }
    }
}
