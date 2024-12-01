package thingxII.pixelblend.Effects;

import Core.PixelmonTrackerCooldownWithData;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import thingxII.pixelblend.Config.PeriodicEffectConfig;
import thingxII.pixelblend.ConfigProxy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ApplyPeriodicEffect {
    // Tracker data holds ticks until next activation
    private static List<PixelmonTrackerCooldownWithData<PeriodicEffectConfig.EntityConfig>> trackers = new ArrayList<>();

    @SubscribeEvent
    public static void OnServerStarted(FMLServerStartedEvent event) {
        for (PeriodicEffectConfig.EntityConfig config : ConfigProxy.getPeriodicEffectConfig().getConfigs()) {
            PixelmonTrackerCooldownWithData<PeriodicEffectConfig.EntityConfig> newTracker = new PixelmonTrackerCooldownWithData<>(config.getPredicate().asPredicate(), config.getCooldownSeconds() * 20, true);
            newTracker.SetCooldownComplete(ApplyPeriodicEffect::applyEffect);
            newTracker.SetDefaultCustomData(e -> config);

            trackers.add(newTracker);
        }
    }

    private static Void applyEffect(PixelmonEntity entity, PeriodicEffectConfig.EntityConfig config) {

        Effect toApply = config.getEffect();
        if (toApply == null) {
            return null;
        }

        if (config.getApplyDirectlyToOwner()) {
            entity.getPokemon().getOwnerPlayer().addEffect(new EffectInstance(toApply, config.getDurationSeconds() * 20, config.getLevel()));
            return null;
        }

        float radius = config.getApplyRange();
        BlockPos position = entity.blockPosition();

        ServerWorld world = (ServerWorld) entity.level;
        List<LivingEntity> nearbyEntities = world.getNearbyEntities(LivingEntity.class, new EntityPredicate() {
                    @Override
                    public boolean test(@Nullable LivingEntity p_221015_1_, LivingEntity p_221015_2_) {
                        return p_221015_2_.isInWater();
                    }
                }, entity,
                new AxisAlignedBB(
                        position.subtract(new Vector3i(radius, radius, radius)),
                        position.offset(new Vector3i(radius, radius, radius))
                )
        );

        for (LivingEntity nearbyEntity : nearbyEntities) {
            nearbyEntity.addEffect(new EffectInstance(toApply, config.getDurationSeconds() * 20, config.getLevel()));
        }

        return null;
    }
}
