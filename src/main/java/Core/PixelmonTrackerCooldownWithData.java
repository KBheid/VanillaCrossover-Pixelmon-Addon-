package Core;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.util.math.MathHelper;

import java.util.function.*;

public class PixelmonTrackerCooldownWithData<T> extends PixelmonEntityTracker<PixelmonTrackerCooldownWithData<T>.CustomDataWithCooldown> {

    private final int defaultCooldown;

    private Consumer<PixelmonEntity> onAdd = null;
    private Consumer<PixelmonEntity> onTick = null;
    private BiFunction<PixelmonEntity, PixelmonTrackerCooldownWithData<T>, Void> onTickWithTracker = null;
    private BiFunction<PixelmonEntity, T, Void> onCooldownElapsed = null;
    private Function<PixelmonEntity, Integer> cooldownGetter = null;
    private BiFunction<PixelmonEntity, T, Integer> customCooldownGetter = null;

    private final boolean resetCooldown;

    public PixelmonTrackerCooldownWithData(Predicate<PixelmonEntity> entityAcceptance, boolean tickingValidator, int cooldown, boolean resetCooldownWhenComplete) {
        super(entityAcceptance, tickingValidator);
        defaultCooldown = cooldown;

        onEntityAdded = this::entityAdded;
        onEntityTick = this::entityTick;
        getDefaultData = e -> new CustomDataWithCooldown(null, cooldown);
        cooldownGetter = e -> defaultCooldown;
        resetCooldown = resetCooldownWhenComplete;
    }

    public PixelmonTrackerCooldownWithData(Predicate<PixelmonEntity> entityAcceptance, int cooldown, boolean resetCooldownWhenComplete) { this(entityAcceptance, false, cooldown, resetCooldownWhenComplete); }

    void entityAdded(PixelmonEntity entity) {
        if (onAdd != null) {
            onAdd.accept(entity);
        }
    }

    void entityTick(PixelmonEntity entity) {
        int cooldown = trackedEntities.get(entity).cooldown;
        cooldown--;

        // No negatives
        cooldown = MathHelper.clamp(cooldown, 0, cooldown);

        if (onTick != null) {
            onTick.accept(entity);
        }

        if (onTickWithTracker != null) {
            onTickWithTracker.apply(entity, this);
        }

        if (cooldown <= 0) {
            if (onCooldownElapsed != null) {
                onCooldownElapsed.apply(entity, trackedEntities.get(entity).data);
            }

            if (resetCooldown) {
                if (customCooldownGetter != null) {
                    cooldown = customCooldownGetter.apply(entity, trackedEntities.get(entity).data);
                }
                else {
                    cooldown = cooldownGetter.apply(entity);
                }
            }
        }

        trackedEntities.get(entity).cooldown = cooldown;
    }

    public int GetCooldown(PixelmonEntity entity) { return super.GetEntityData(entity).cooldown; }
    public void SetCooldown(PixelmonEntity entity, int cooldown) { super.GetEntityData(entity).cooldown = cooldown; }
    public void SetCooldown(PixelmonEntity entity) { super.GetEntityData(entity).cooldown = defaultCooldown; }

    public void SetCooldownComplete(BiFunction<PixelmonEntity, T, Void> cooldownComplete) { onCooldownElapsed = cooldownComplete; }
    public void ClearCooldownComplete() { onCooldownElapsed = null; }

    /// Returns true if not on cooldown, false if it is.
    public boolean CheckCooldown(PixelmonEntity entity) { return GetCooldown(entity) <= 0; }

    public void SetDefaultCustomData(Function<PixelmonEntity, T> function) {
        getDefaultData = e -> new CustomDataWithCooldown(function.apply(e), defaultCooldown);
    }
    public void SetCooldownFunction(Function<PixelmonEntity, Integer> function) { cooldownGetter = function; }
    public void SetCooldownWithDataFunction(BiFunction<PixelmonEntity, T, Integer> function) { customCooldownGetter = function; }

    @Override
    public void SetAddEvent(Consumer<PixelmonEntity> added) { onAdd = added; }
    @Override
    public void ClearAddEvent() { onAdd = null; }

    @Override
    public void SetTickEvent(Consumer<PixelmonEntity> tick) { onTick = tick; }
    @Override
    public void ClearTickEvent() { onTick = null; }

    public void SetTickWithTrackerEvent(BiFunction<PixelmonEntity, PixelmonTrackerCooldownWithData<T>, Void> tick) { onTickWithTracker = tick; }
    public void ClearTickWithTrackerEvent() { onTickWithTracker = null; }

    public T GetCustomData(PixelmonEntity entity) { return trackedEntities.get(entity).data; }
    public void SetCustomData(PixelmonEntity entity, T newData) { trackedEntities.get(entity).data = newData; }

    class CustomDataWithCooldown {
        T data;
        int cooldown;

        CustomDataWithCooldown(T data, int cooldown) {
            this.data = data;
            this.cooldown = cooldown;
        }
    }
}
