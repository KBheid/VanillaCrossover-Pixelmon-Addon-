package Core.Config;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

@ConfigSerializable
public class StatIntegerConfig {
    private int baseValue = 0;

    // TODO: This is still really clunky... Think if we can generalize this MORE to allow custom evaluation, perhaps uri to JSON file is required instead of all this in yml?
    private PokemonStatConfig evs;
    private PokemonStatConfig ivs;

    private PixelmonStatConfig otherStats;

    StatIntegerConfig() { }
    public StatIntegerConfig(int baseValue, PokemonStatConfig evs, PokemonStatConfig ivs, PixelmonStatConfig otherStats) {
        this.baseValue = baseValue;
        this.evs = evs;
        this.ivs = ivs;
        this.otherStats = otherStats;
    }

    public int getForPokemon(Pokemon pokemon) {
        int curVal = baseValue;
        boolean maxIncrease = false;

        // EVs
        {
            int hpEvs = pokemon.getEVs().getStat(BattleStatsType.HP);
            int attackEvs = pokemon.getEVs().getStat(BattleStatsType.ATTACK);
            int defenseEvs = pokemon.getEVs().getStat(BattleStatsType.DEFENSE);
            int spAttackEvs = pokemon.getEVs().getStat(BattleStatsType.SPECIAL_ATTACK);
            int spDefenseEvs = pokemon.getEVs().getStat(BattleStatsType.SPECIAL_DEFENSE);
            int speedEvs = pokemon.getEVs().getStat(BattleStatsType.SPEED);

            if (evs.hpThreshold >= 0 && hpEvs >= evs.hpThreshold) {
                curVal++;

                if (evs.maxHpIncreases && hpEvs >= 252) {
                    maxIncrease = true;
                }
            }
            if (evs.attackThreshold >= 0 && attackEvs >= evs.attackThreshold) {
                curVal++;

                if (evs.maxAttackIncreases && attackEvs >= 252) {
                    maxIncrease = true;
                }
            }
            if (evs.defenseThreshold >= 0 && defenseEvs >= evs.defenseThreshold) {
                curVal++;

                if (evs.maxDefenseIncreases && defenseEvs >= 252) {
                    maxIncrease = true;
                }
            }
            if (evs.spAttackThreshold >= 0 && spAttackEvs >= evs.spAttackThreshold) {
                curVal++;

                if (evs.maxSpAttackIncreases && spAttackEvs >= 252) {
                    maxIncrease = true;
                }
            }
            if (evs.spDefenseThreshold >= 0 && spDefenseEvs >= evs.spDefenseThreshold) {
                curVal++;

                if (evs.maxSpDefenseIncreases && spDefenseEvs >= 252) {
                    maxIncrease = true;
                }
            }
            if (evs.speedThreshold >= 0 && speedEvs >= evs.speedThreshold) {
                curVal++;

                if (evs.maxSpeedIncreases && speedEvs >= 252) {
                    maxIncrease = true;
                }
            }
        }

        // IVs
        {
            int hpIvs = pokemon.getIVs().getStat(BattleStatsType.HP);
            int attackIvs = pokemon.getIVs().getStat(BattleStatsType.ATTACK);
            int defenseIvs = pokemon.getIVs().getStat(BattleStatsType.DEFENSE);
            int spAttackIvs = pokemon.getIVs().getStat(BattleStatsType.SPECIAL_ATTACK);
            int spDefenseIvs = pokemon.getIVs().getStat(BattleStatsType.SPECIAL_DEFENSE);
            int speedIvs = pokemon.getIVs().getStat(BattleStatsType.SPEED);

            if (ivs.hpThreshold >= 0 && hpIvs > ivs.hpThreshold) {
                curVal++;

                if (ivs.maxHpIncreases && hpIvs >= 31) {
                    maxIncrease = true;
                }
            }
            if (ivs.attackThreshold >= 0 && attackIvs > ivs.attackThreshold) {
                curVal++;

                if (ivs.maxAttackIncreases && attackIvs >= 31) {
                    maxIncrease = true;
                }
            }
            if (ivs.defenseThreshold >= 0 && defenseIvs > ivs.defenseThreshold) {
                curVal++;

                if (ivs.maxDefenseIncreases && defenseIvs >= 31) {
                    maxIncrease = true;
                }
            }
            if (ivs.spAttackThreshold >= 0 && spAttackIvs > ivs.spAttackThreshold) {
                curVal++;

                if (ivs.maxSpAttackIncreases && spAttackIvs >= 31) {
                    maxIncrease = true;
                }
            }
            if (ivs.spDefenseThreshold >= 0 && spDefenseIvs > ivs.spDefenseThreshold) {
                curVal++;

                if (ivs.maxSpDefenseIncreases && spDefenseIvs >= 31) {
                    maxIncrease = true;
                }
            }
            if (ivs.speedThreshold >= 0 && speedIvs > ivs.speedThreshold) {
                curVal++;

                if (ivs.maxSpeedIncreases && speedIvs >= 31) {
                    maxIncrease = true;
                }
            }
        }

        // Other stats
        {
            int growth = pokemon.getGrowth().scaleOrdinal;
            int dynamax = pokemon.getDynamaxLevel();

            if (otherStats.growthThreshold >= 0) {
                if (otherStats.useGrowthAsReference) {
                    curVal += growth - otherStats.growthThreshold;
                }
                else if (growth > otherStats.growthThreshold) {
                    curVal++;
                }

                if (otherStats.maxGrowthIncreases) {
                    maxIncrease = true;
                }
            }

            if (otherStats.dynamaxThreshold >= 0 && dynamax >= otherStats.dynamaxThreshold) {
                curVal++;

                if (otherStats.maxDynamaxIncreases && dynamax >= 10) {
                    maxIncrease = true;
                }
            }
        }

        if (maxIncrease) {
            curVal++;
        }

        return curVal;
    }

    @ConfigSerializable
    public static class PixelmonStatConfig {
        int dynamaxThreshold = -1;
        boolean maxDynamaxIncreases = false;
        int growthThreshold = -1;
        boolean useGrowthAsReference = false;
        boolean maxGrowthIncreases = false;

        public PixelmonStatConfig() { }

        public PixelmonStatConfig(int dynamaxThreshold, boolean maxDynamaxIncreases, int growthThreshold, boolean useGrowthAsReference, boolean maxGrowthIncreases) {
            this.dynamaxThreshold = dynamaxThreshold;
            this.maxDynamaxIncreases = maxDynamaxIncreases;
            this.growthThreshold = growthThreshold;
            this.useGrowthAsReference = useGrowthAsReference;
            this.maxGrowthIncreases = maxGrowthIncreases;
        }
    }

    @ConfigSerializable
    public static class PokemonStatConfig {
        int hpThreshold = -1;
        boolean maxHpIncreases = false;
        int attackThreshold = -1;
        boolean maxAttackIncreases = false;
        int defenseThreshold = -1;
        boolean maxDefenseIncreases = false;
        int spAttackThreshold = -1;
        boolean maxSpAttackIncreases = false;
        int spDefenseThreshold = -1;
        boolean maxSpDefenseIncreases = false;
        int speedThreshold = -1;
        boolean maxSpeedIncreases = false;

        public PokemonStatConfig() { }

        public PokemonStatConfig(int hpThreshold, boolean maxHpIncreases, int attackThreshold, boolean maxAttackIncreases, int defenseThreshold, boolean maxDefenseIncreases, int spAttackThreshold, boolean maxSpAttackIncreases, int spDefenseThreshold, boolean maxSpDefenseIncreases, int speedThreshold, boolean maxSpeedIncreases) {
            this.hpThreshold = hpThreshold;
            this.maxHpIncreases = maxHpIncreases;
            this.attackThreshold = attackThreshold;
            this.maxAttackIncreases = maxAttackIncreases;
            this.defenseThreshold = defenseThreshold;
            this.maxDefenseIncreases = maxDefenseIncreases;
            this.spAttackThreshold = spAttackThreshold;
            this.maxSpAttackIncreases = maxSpAttackIncreases;
            this.spDefenseThreshold = spDefenseThreshold;
            this.maxSpDefenseIncreases = maxSpDefenseIncreases;
            this.speedThreshold = speedThreshold;
            this.maxSpeedIncreases = maxSpeedIncreases;
        }
    }
}
