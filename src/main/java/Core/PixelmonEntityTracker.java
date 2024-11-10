package Core;

import com.pixelmonmod.pixelmon.api.events.PokemonRetrievedEvent;
import com.pixelmonmod.pixelmon.api.events.PokemonSendOutEvent;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PixelmonEntityTracker<T> {
    private final HashMap<PixelmonEntity, T> trackedEntities = new HashMap<>();

    private final Predicate<PixelmonEntity> validator;
    private Consumer<PixelmonEntity> onEntityAdded = null;
    private Consumer<PixelmonEntity> onEntityRemoved = null;
    private Consumer<PixelmonEntity> onEntityTick = null;

    private boolean tickingValidator = false;

    private static final List<PixelmonEntityTracker<?>> activeTrackers = new ArrayList<>();

    @SubscribeEvent
    public static void OnEntitySpawned(EntityJoinWorldEvent event) {
        if (!(event.getWorld() instanceof ServerWorld)) {
            return;
        }

        if (event.getEntity() instanceof PixelmonEntity) {
            PixelmonEntity entity = (PixelmonEntity) event.getEntity();

            for (PixelmonEntityTracker<?> tracker : activeTrackers) {
                if (tracker.validator.test(entity)) {
                    tracker.trackedEntities.put(entity, null);
                    if (tracker.onEntityRemoved != null) {
                        tracker.onEntityAdded.accept(entity);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void OnEntityDestructed(EntityLeaveWorldEvent event) {
        if (!(event.getWorld() instanceof ServerWorld)) {
            return;
        }

        if (event.getEntity() instanceof PixelmonEntity) {
            PixelmonEntity entity = (PixelmonEntity) event.getEntity();

            for (PixelmonEntityTracker<?> tracker : activeTrackers) {
                if (tracker.trackedEntities.containsKey(entity) && tracker.onEntityRemoved != null) {
                    tracker.onEntityRemoved.accept(entity);
                }

                tracker.trackedEntities.remove(entity);
            }
        }
    }

    /* UNNECESSARY (likely)
    @SubscribeEvent
    public static void OnPokemonSentOut(PokemonSendOutEvent.Post event) {
        if (!(event.getPlayer().level instanceof ServerWorld)) {
            return;
        }

        if (event.getEntity() instanceof PixelmonEntity) {
            PixelmonEntity entity = event.getEntity();

            for (PixelmonEntityTracker<?> tracker : activeTrackers) {
                if (tracker.validator.test(entity)) {
                    tracker.trackedEntities.put(entity, null);
                    if (tracker.onEntityRemoved != null) {
                        tracker.onEntityAdded.accept(entity);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void OnPokemonRetrieved(PokemonRetrievedEvent.Pre event) {
        event.getPokemon().ifEntityExists(entity -> {
            for (PixelmonEntityTracker<?> tracker : activeTrackers) {
                if (tracker.trackedEntities.containsKey(entity) && tracker.onEntityRemoved != null) {
                    tracker.trackedEntities.remove(entity);
                    tracker.onEntityRemoved.accept(entity);
                }
            }
        });
    }*/

    @SubscribeEvent
    public static void DoWorldTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        // For each entity in each tracker, tick it. If it has ticking validation and doesn't pass, then do not tick and remove it instead.
        for (PixelmonEntityTracker<?> tracker : activeTrackers) {
            ArrayList<PixelmonEntity> untrackList = new ArrayList<>();

            for (PixelmonEntity entity : tracker.trackedEntities.keySet()) {
                boolean shouldTick = true;

                if (tracker.tickingValidator && !tracker.validator.test(entity)) {
                    untrackList.add(entity);
                    shouldTick = false;

                    if (tracker.onEntityRemoved != null) {
                        tracker.onEntityRemoved.accept(entity);
                    }
                }

                if (shouldTick && tracker.onEntityTick != null) {
                    tracker.onEntityTick.accept(entity);
                }
            }

            untrackList.forEach(tracker.trackedEntities::remove);
        }
    }

    /// Create a PixelmonEntityTracker that tracks entities that fit a predicate. If tickingValidator is true, then this predicate will be checked each tick.
    public PixelmonEntityTracker(Predicate<PixelmonEntity> acceptance, boolean tickingValidator) {
        activeTrackers.add(this);
        validator = acceptance;
        this.tickingValidator = tickingValidator;
    }

    public PixelmonEntityTracker(Predicate<PixelmonEntity> acceptance) { this(acceptance, false); }

    /// Simple constuctor for creating species-specific trackers
    public PixelmonEntityTracker(Collection<Species> acceptedSpecies) { this((e) -> acceptedSpecies.contains(e.getSpecies()), false); }

    public void SetTickEvent(Consumer<PixelmonEntity> tick) { onEntityTick = tick; }
    public void ClearTickEvent() { onEntityTick = null;  }

    public void SetAddEvent(Consumer<PixelmonEntity> added) { onEntityAdded = added; }
    public void ClearAddEvent() { onEntityAdded = null; }

    public void SetRemoveEvent(Consumer<PixelmonEntity> removed) { onEntityRemoved = removed; }
    public void ClearRemoveEvent() { onEntityRemoved = null; }

    public T GetEntityData(PixelmonEntity entity) { return (T) trackedEntities.get(entity); }
    public void SetEntityData(PixelmonEntity entity, T data) { trackedEntities.put(entity, data); }

    /// If this tracker's lifetime isn't static, calling this is necessary to cease tracking (no need to clear events).
    public void Close() { activeTrackers.remove(this); }

    public Iterable<PixelmonEntity> getTrackedEntities() { return trackedEntities.keySet(); }

    public void forEachTrackedEntity(Consumer<PixelmonEntity> forEachEntity) {
        for (PixelmonEntity entity : trackedEntities.keySet()) {
            forEachEntity.accept(entity);
        }
    }
}
