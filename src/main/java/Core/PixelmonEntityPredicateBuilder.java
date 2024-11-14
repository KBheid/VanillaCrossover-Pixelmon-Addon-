package Core;

import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.ImmutableAttack;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/// Builds a PixelmonEntity Predicate based on strings.
public class PixelmonEntityPredicateBuilder {
    public static PixelmonEntityPredicateBuilder getBuilder() { return new PixelmonEntityPredicateBuilder(); }

    private Collection<String> allowedMoves = null;
    private Collection<String> allowedAbilities = null;
    private Collection<String> allowedSpecies = null;

    public PixelmonEntityPredicateBuilder moveRestriction(Collection<String> allowedMoves) {
        this.allowedMoves = allowedMoves;
        return this;
    }

    public PixelmonEntityPredicateBuilder abilityRestriction(Collection<String> allowedAbilities) {
        this.allowedAbilities = allowedAbilities;
        return this;
    }

    public PixelmonEntityPredicateBuilder speciesRestriction(Collection<String> allowedSpecies) {
        this.allowedSpecies = allowedSpecies;
        return this;
    }

    public Predicate<PixelmonEntity> buildPredicate() {
        ImmutableAttack[] acceptableAttacks = Attack.getAttacks(this.allowedMoves.toArray(new String[0]));
        List<Ability> acceptableAbilities = new ArrayList<>();
        List<Species> acceptableSpecies = new ArrayList<>();

        // Build our lists:
        for (String abilityString : allowedAbilities) {
            Optional<Ability> possibleAbility = AbilityRegistry.getAbility(abilityString);
            possibleAbility.ifPresent(acceptableAbilities::add);
        }

        for (String speciesString : allowedSpecies) {
            RegistryValue<Species> possibleSpecies = PixelmonSpecies.fromName(speciesString.toUpperCase());
            possibleSpecies.ifInitialized(acceptableSpecies::add);
        }

        // Construct the actual predicate
        return (entity -> {
            Pokemon pokemon = entity.getPokemon();
            if (pokemon == null) {
                return false;
            }

            // Check for attack
            boolean hasNecssaryAttack = false;
            if (acceptableAttacks.length == 0) {
                hasNecssaryAttack = true;
            }
            else {
                for (Attack a : pokemon.getMoveset().attacks) {
                    if (a == null) {
                        continue;
                    }

                    ImmutableAttack attack = a.getActualMove();
                    if (ArrayUtils.contains(acceptableAttacks, attack)) {
                        hasNecssaryAttack = true;
                        break;
                    }
                }
            }

            // Check for ability
            boolean hasNecessaryAbility = false;
            if (acceptableAbilities.isEmpty()) {
                hasNecessaryAbility = true;
            }
            else {
                hasNecessaryAbility = acceptableAbilities.contains(pokemon.getAbility());
            }

            // Check for species
            boolean hasNecssarySpecies = false;
            if (acceptableSpecies.isEmpty()) {
                hasNecssarySpecies = true;
            }
            else {
                hasNecssarySpecies = acceptableSpecies.contains(pokemon.getSpecies());
            }

            return hasNecssaryAttack && hasNecessaryAbility && hasNecssarySpecies;
        });
    }
}
