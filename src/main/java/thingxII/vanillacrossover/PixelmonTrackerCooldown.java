package thingxII.vanillacrossover;

import Core.PixelmonEntityTracker;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class PixelmonTrackerCooldown extends PixelmonEntityTracker<Integer> {

    private final int defaultCooldown;

    private Consumer<PixelmonEntity> onAdd = null;
    private Consumer<PixelmonEntity> onTick = null;
    private Consumer<PixelmonEntity> onCooldownElapsed = null;

    private final boolean resetCooldown;

    public PixelmonTrackerCooldown(Predicate<PixelmonEntity> entityAcceptance, boolean tickingValidator, int cooldown, boolean resetCooldownWhenComplete) {
        super(entityAcceptance, tickingValidator);
        defaultCooldown = cooldown;

        onEntityAdded = this::entityAdded;
        onEntityTick = this::entityTick;
        resetCooldown = resetCooldownWhenComplete;
    }

    public PixelmonTrackerCooldown(Predicate<PixelmonEntity> entityAcceptance, int cooldown, boolean resetCooldownWhenComplete) { this(entityAcceptance, false, cooldown, resetCooldownWhenComplete); }

    void entityAdded(PixelmonEntity entity) {
        trackedEntities.put(entity, defaultCooldown);

        if (onAdd != null) {
            onAdd.accept(entity);
        }
    }

    void entityTick(PixelmonEntity entity) {
        Integer cooldown = trackedEntities.get(entity);
        cooldown--;

        // No negatives
        cooldown = MathHelper.clamp(cooldown, 0, cooldown);

        if (onTick != null) {
            onTick.accept(entity);
        }

        if (cooldown <= 0) {
            if (onCooldownElapsed != null) {
                onCooldownElapsed.accept(entity);
            }

            if (resetCooldown) {
                cooldown = defaultCooldown;
            }
        }

        trackedEntities.put(entity, cooldown);
    }

    public int GetCooldown(PixelmonEntity entity) { return GetEntityData(entity); }
    public void SetCooldown(PixelmonEntity entity, int cooldown) { SetEntityData(entity, cooldown); }
    public void SetCooldown(PixelmonEntity entity) { SetEntityData(entity, defaultCooldown); }

    public void SetCooldownComplete(Consumer<PixelmonEntity> cooldownComplete) { onCooldownElapsed = cooldownComplete; }
    public void ClearCooldownComplete() { onCooldownElapsed = null; }

    /// Returns true if not on cooldown, false if it is.
    public boolean CheckCooldown(PixelmonEntity entity) { return GetCooldown(entity) <= 0; }

    @Override
    public void SetAddEvent(Consumer<PixelmonEntity> added) {
        onAdd = added;
    }

    @Override
    public void ClearAddEvent() {
        onAdd = null;
    }

    @Override
    public void SetTickEvent(Consumer<PixelmonEntity> tick) {
        onTick = tick;
    }

    @Override
    public void ClearTickEvent() {
        onTick = null;
    }
}
