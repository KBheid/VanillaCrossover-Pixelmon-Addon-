package thingxII.vanillacrossover.Config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = "vanillacrossover", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChestablePokemonConfig {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	private static final ForgeConfigSpec.BooleanValue ALLOW_CHESTABLE_POKEMON;
	private static final ForgeConfigSpec.ConfigValue<String> PALETTE_PREFIX;

	private static final ForgeConfigSpec.ConfigValue<List<? extends String>> CHESTABLE_SPECIES;

	private static final ForgeConfigSpec.ConfigValue<List<? extends Boolean>> CHEST_SIZE_EVS;
	private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> CHEST_SIZE_EVS_VALUES;
	private static final ForgeConfigSpec.BooleanValue MAX_EVS_IS_INCREASE;

	private static final ForgeConfigSpec.ConfigValue<List<? extends Boolean>> CHEST_SIZE_IVS;
	private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> CHEST_SIZE_IVS_VALUES;
	private static final ForgeConfigSpec.BooleanValue MAX_IVS_IS_INCREASE;

	private static final ForgeConfigSpec.BooleanValue DYNAMAX_INCREASES;
	private static final ForgeConfigSpec.IntValue DYNAMAX_VALUE;
	private static final ForgeConfigSpec.BooleanValue MAX_DYNAMAX_INCREASES;

	private static final ForgeConfigSpec.BooleanValue GROWTH_INCREASES;
	private static final ForgeConfigSpec.IntValue GROWTH_VALUE;
	private static final ForgeConfigSpec.BooleanValue GROWTH_DIFF_INCREASES;
	private static final ForgeConfigSpec.BooleanValue MAX_GROWTH_IS_INCREASE;

	static {
		ALLOW_CHESTABLE_POKEMON = BUILDER
				.comment("If false, disregard any of the below config because chestable pokemon will be entirely disabled.")
				.define("Allow Chestable Pokemon", true);

		PALETTE_PREFIX = BUILDER
				.comment("Advanced - probably only of interest if you plan on making custom models. This is the prefix added to extant palettes when trying to set a pokemon's palette.")
				.define("Palette Prefix", "basket");

		CHESTABLE_SPECIES = BUILDER
				.comment("Which Pokemon can have chests/barrels applied to them?")
				.defineList("chestableSpecies", Arrays.asList("MUDBRAY", "MUDSDALE"), o -> o instanceof String);

		BUILDER.comment("Inventories come in 5 sizes, ranging from 9 items to 104 items. This next section determines what level of storage a Pokemon should have.");
		BUILDER.comment("For each true below, the potential size increases by 1 up to 5 maximum with a base of 1.");
		BUILDER.comment("e.g. if HP and Atk EVs are 'true' and the Max Increases is 'true' and everything else is false, a pokemon could have a maximum size 4 inventory. (1 base + 3)");
		BUILDER.comment("NOTE: All 'Max Increases' fields are shared - meaning that they can only ever provide a total of +1 to size. If three 'Max Increases' evaluate true, the inventory won't increase by 3, but by one!");
		BUILDER.push("EVs:");
			BUILDER.comment("Which EVs should be taken into consideration and at what values do they add their +1?");
		BUILDER.comment("HP, Atk, Def, SpA, SpD, Spe");
			CHEST_SIZE_EVS = BUILDER
					.defineList("EVs", Arrays.asList(true, false, false, false, false, false), o -> o instanceof Boolean);
			CHEST_SIZE_EVS_VALUES = BUILDER
					.defineList("Threshold Values", Arrays.asList(126, 126, 126, 126, 126, 126), o -> o instanceof Integer);
			BUILDER.comment("If true, if any of the above EVs marked 'true' are at 252, the size will get another +1 to size.");
			MAX_EVS_IS_INCREASE = BUILDER
					.define("Max EV Increases", true);
		BUILDER.pop();

		BUILDER.push("IVs:");
			BUILDER.comment("Which IVs should be taken into consideration and at what values do they add their +1?");
			BUILDER.comment("HP, Atk, Def, SpA, SpD, Spe");
			CHEST_SIZE_IVS = BUILDER
					.defineList("IVs", Arrays.asList(true, false, false, false, false, false), o -> o instanceof Boolean);
			CHEST_SIZE_IVS_VALUES = BUILDER
					.defineList("Threshold Values", Arrays.asList(15, 15, 15, 15, 15, 15), o -> o instanceof Integer);
			BUILDER.comment("If true, if any of the above IVs marked 'true' are at 31, the size will get another +1 to size.");
			MAX_IVS_IS_INCREASE = BUILDER
					.define("Max IV Increases", true);
		BUILDER.pop();

		BUILDER.push("Dynamax:");
			DYNAMAX_INCREASES = BUILDER
					.comment("Should the Dynamax level increase the storage size?")
					.define("Dynamax Increases", true);
			DYNAMAX_VALUE = BUILDER
					.comment("At what level should it increase? Values between 0-10 are valid.")
					.defineInRange("Threshold Value", 5, 0, 10);
			MAX_DYNAMAX_INCREASES = BUILDER
					.comment("Should the size get another +1 if the Dynamax level is at 10/10?")
					.define("Max Dynamax Increases", true);
		BUILDER.pop();

		BUILDER.push("Growth:");
			GROWTH_INCREASES = BUILDER
					.comment("Should the Growth of the Pokemon increase storage size?")
					.define("Growth Increases", false);
			BUILDER.comment("Growth sizes are (in order from 0 to 8): Microscopic, Pygmy, Runt, Small, Ordinary, Huge, Giant, Enormous, Ginormous");
			GROWTH_VALUE = BUILDER
					.comment("At what level should growth contribute to inventory size? 0 for Microscopic, 8 for Ginormous")
					.defineInRange("Reference Value", 5, 0, 8);
			BUILDER.comment("If true, inventory size will increase by +1 per difference in level from the Reference Value.");
			BUILDER.comment("Ex. with a reference value of 4, a Huge pokemon would receive +1, a Giant pokemon would receive +2, etc.");
			GROWTH_DIFF_INCREASES = BUILDER
					.define("Growth Difference Increases", true);
			MAX_GROWTH_IS_INCREASE = BUILDER
					.comment("If true, the pokemon will receive a +1 to inventory size if Ginormous.")
					.define("Max Growth Increases", false);
		BUILDER.pop();
	}

	public static final ForgeConfigSpec SPEC = BUILDER.build();

	public static boolean allowChestablePokemon;

	public static String palettePrefix;

	public static List<? extends String> chestableSpecies;

	public static List<? extends Boolean> chestSizeEvs;
	public static List<? extends Integer> chestSizeEvsValues;
	public static boolean maxEVsIncreases;

	public static List<? extends Boolean> chestSizeIvs;
	public static List<? extends Integer> chestSizeIvsValues;
	public static boolean maxIVsIncreases;

	public static boolean dynamaxIncreases;
	public static int dynamaxValue;
	public static boolean maxDynamaxIncreases;

	public static boolean growthIncreases;
	public static int growthReferenceValue;
	public static boolean growthDiffIncreases;
	public static boolean maxGrowthIncreases;

	@SubscribeEvent
	static void onLoad(final ModConfig.ModConfigEvent event) {
		allowChestablePokemon = ALLOW_CHESTABLE_POKEMON.get();

		palettePrefix = PALETTE_PREFIX.get();

		chestableSpecies = CHESTABLE_SPECIES.get();

		chestSizeEvs = CHEST_SIZE_EVS.get();
		chestSizeEvsValues = CHEST_SIZE_EVS_VALUES.get();
		maxEVsIncreases = MAX_EVS_IS_INCREASE.get();

		chestSizeIvs = CHEST_SIZE_IVS.get();
		chestSizeIvsValues = CHEST_SIZE_IVS_VALUES.get();
		maxIVsIncreases = MAX_IVS_IS_INCREASE.get();

		dynamaxIncreases = DYNAMAX_INCREASES.get();
		dynamaxValue = DYNAMAX_VALUE.get();
		maxDynamaxIncreases = MAX_DYNAMAX_INCREASES.get();

		growthIncreases = GROWTH_INCREASES.get();
		growthReferenceValue = GROWTH_VALUE.get();
		growthDiffIncreases = GROWTH_DIFF_INCREASES.get();
		maxGrowthIncreases = MAX_GROWTH_IS_INCREASE.get();
	}
}
