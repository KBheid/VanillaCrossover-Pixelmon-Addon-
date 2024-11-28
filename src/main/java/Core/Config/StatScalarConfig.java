package Core.Config;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

@ConfigSerializable
public class StatScalarConfig {
    private float lowerBound = 0.0f;
    private float upperBound = 1.0f;

    private PokemonStatConfig evs;
    private PokemonStatConfig ivs;

    private PixelmonStatConfig otherStats;

    public StatScalarConfig() { }
    public StatScalarConfig(float lowerBound, float upperBound, PokemonStatConfig evs, PokemonStatConfig ivs, PixelmonStatConfig otherStats) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.evs = evs;
        this.ivs = ivs;
        this.otherStats = otherStats;
    }

    public float getLowerBound() { return lowerBound; }
    public float getUpperBound() { return upperBound; }

    public float getForEntity(PixelmonEntity entity) {
        Pokemon pokemon = entity.getPokemon();

        int curVal = 0;
        int maxVal = 0;

        // EVs
        {
            int hpEvs = pokemon.getEVs().getStat(BattleStatsType.HP);
            int attackEvs = pokemon.getEVs().getStat(BattleStatsType.ATTACK);
            int defenseEvs = pokemon.getEVs().getStat(BattleStatsType.DEFENSE);
            int spAttackEvs = pokemon.getEVs().getStat(BattleStatsType.SPECIAL_ATTACK);
            int spDefenseEvs = pokemon.getEVs().getStat(BattleStatsType.SPECIAL_DEFENSE);
            int speedEvs = pokemon.getEVs().getStat(BattleStatsType.SPEED);

            if (evs.minHp >= 0) {
                int maxHp = 252 - evs.minHp;
                maxVal += maxHp;

                if (hpEvs > evs.minHp) {
                    curVal += hpEvs - evs.minHp;
                }
            }
            if (evs.minAttack >= 0) {
                int maxAttack = 252 - evs.minAttack;
                maxVal += maxAttack;

                if (attackEvs > evs.minAttack) {
                    curVal += attackEvs - evs.minAttack;
                }
            }
            if (evs.minDefense >= 0) {
                int maxDefense = 252 - evs.minDefense;
                maxVal += maxDefense;

                if (defenseEvs > evs.minDefense) {
                    curVal += defenseEvs - evs.minDefense;
                }
            }
            if (evs.minSpAttack >= 0) {
                int maxSpAttack = 252 - evs.minSpAttack;
                maxVal += maxSpAttack;

                if (spAttackEvs > evs.minSpAttack) {
                    curVal += spAttackEvs - evs.minSpAttack;
                }
            }
            if (evs.minSpDefense >= 0) {
                int maxSpDefense = 252 - evs.minSpDefense;
                maxVal += maxSpDefense;

                if (spDefenseEvs > evs.minSpDefense) {
                    curVal += spDefenseEvs - evs.minSpDefense;
                }
            }
            if (evs.minSpeed >= 0) {
                int maxSpeed = 252 - evs.minSpeed;
                maxVal += maxSpeed;

                if (speedEvs > evs.minSpeed) {
                    curVal += speedEvs - evs.minSpeed;
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

            if (ivs.minHp >= 0) {
                int maxHp = 31 - ivs.minHp;
                maxVal += maxHp;

                if (hpIvs > ivs.minHp) {
                    curVal += hpIvs - ivs.minHp;
                }
            }
            if (ivs.minAttack >= 0) {
                int maxAttack = 31 - ivs.minAttack;
                maxVal += maxAttack;

                if (attackIvs > ivs.minAttack) {
                    curVal += attackIvs - ivs.minAttack;
                }
            }
            if (ivs.minDefense >= 0) {
                int maxDefense = 31 - ivs.minDefense;
                maxVal += maxDefense;

                if (defenseIvs > ivs.minDefense) {
                    curVal += defenseIvs - ivs.minDefense;
                }
            }
            if (ivs.minSpAttack >= 0) {
                int maxSpAttack = 31 - ivs.minSpAttack;
                maxVal += maxSpAttack;

                if (spAttackIvs > ivs.minSpAttack) {
                    curVal += spAttackIvs - ivs.minSpAttack;
                }
            }
            if (ivs.minSpDefense >= 0) {
                int maxSpDefense = 31 - ivs.minSpDefense;
                maxVal += maxSpDefense;

                if (spDefenseIvs > ivs.minSpDefense) {
                    curVal += spDefenseIvs - ivs.minSpDefense;
                }
            }
            if (ivs.minSpeed >= 0) {
                int maxSpeed = 31 - ivs.minSpeed;
                maxVal += maxSpeed;

                if (speedIvs > ivs.minSpeed) {
                    curVal += speedIvs - ivs.minSpeed;
                }
            }
        }

        // Other stats
        {
            int growth = pokemon.getGrowth().scaleOrdinal;
            int dynamax = pokemon.getDynamaxLevel();

            if (otherStats.minGrowth >= 0) {
                int maxGrowth = 8;
                maxVal += maxGrowth;

                if (growth > otherStats.minGrowth) {
                    curVal += growth - otherStats.minGrowth;
                }
            }

            if (otherStats.minDynamax >= 0) {
                int maxDynamax = 10;
                maxVal += maxDynamax;

                if (dynamax > otherStats.minDynamax) {
                    curVal += dynamax - otherStats.minDynamax;
                }
            }
        }

        return divOrZero(curVal, maxVal) * (upperBound - lowerBound) + lowerBound;
    }

    private static float divOrZero(float numerator, float divisor) {
        return divisor == 0.0f ? 0.0f : numerator / divisor;
    }

    @ConfigSerializable
    public static class PixelmonStatConfig {
        int minDynamax = -1;
        int minGrowth = -1;

        public PixelmonStatConfig() { }

        public PixelmonStatConfig(int minDynamax, int minGrowth) {
            this.minDynamax = minDynamax;
            this.minGrowth = minGrowth;
        }
    }

    @ConfigSerializable
    public static class PokemonStatConfig {
        int minHp = -1;
        int minAttack = -1;
        int minDefense = -1;
        int minSpAttack = -1;
        int minSpDefense = -1;
        int minSpeed = -1;

        public PokemonStatConfig() { }

        public PokemonStatConfig(int minHp, int minAttack, int minDefense, int minSpAttack, int minSpDefense, int minSpeed) {
            this.minHp = minHp;
            this.minAttack = minAttack;
            this.minDefense = minDefense;
            this.minSpAttack = minSpAttack;
            this.minSpDefense = minSpDefense;
            this.minSpeed = minSpeed;
        }
    }
}
