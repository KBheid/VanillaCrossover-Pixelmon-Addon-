package thingxII.pixelblend.Effects;

import Core.PixelmonTrackerCooldown;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import thingxII.pixelblend.Config.SwapPositionOwnerInDangerConfig;
import thingxII.pixelblend.ConfigProxy;

import java.util.ArrayList;
import java.util.List;

public class SwapPositionOwnerInDanger {

    // Boolean data tracks whether we are currently on cooldown or not
    private static List<PixelmonTrackerCooldown> trackers = new ArrayList<>();

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        for (SwapPositionOwnerInDangerConfig.EntityConfig config : ConfigProxy.getSwapPositionOwnerInDangerConfig().getConfigs()) {
            PixelmonTrackerCooldown newTracker = new PixelmonTrackerCooldown(config.getPredicate().asPredicate(), config.getCooldown(), false);

            newTracker.SetTickWithTrackerEvent(SwapPositionOwnerInDanger::tick);
            trackers.add(newTracker);
        }
    }

    private static Void tick(PixelmonEntity entity, PixelmonTrackerCooldown tracker) {
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
        return null;
    }
}
