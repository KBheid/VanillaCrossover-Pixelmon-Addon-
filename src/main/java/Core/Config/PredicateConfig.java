package Core.Config;

import Core.PixelmonEntityPredicateBuilder;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.function.Predicate;

@ConfigSerializable
public class PredicateConfig {
    private List<String> acceptedMoves;
    private List<String> acceptedAbilities;
    private List<String> acceptedSpecies;

    public PredicateConfig() {}

    public PredicateConfig(List<String> moves, List<String> abilities, List<String> species) {
        acceptedMoves = moves;
        acceptedAbilities = abilities;
        acceptedSpecies = species;
    }

    public List<String> getAcceptedMoves() {
        return this.acceptedMoves;
    }
    public List<String> getAcceptedAbilities() { return this.acceptedAbilities; }
    public List<String> getAcceptedSpecies() { return this.acceptedSpecies; }

    public Predicate<PixelmonEntity> asPredicate() {
        return new PixelmonEntityPredicateBuilder()
                .moveRestriction(acceptedMoves)
                .abilityRestriction(acceptedAbilities)
                .speciesRestriction(acceptedSpecies)
                .buildPredicate();
    }
}
