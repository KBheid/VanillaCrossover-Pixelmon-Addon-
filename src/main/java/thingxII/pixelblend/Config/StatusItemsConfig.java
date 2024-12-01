package thingxII.pixelblend.Config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = "pixelblend", bus = Mod.EventBusSubscriber.Bus.MOD)
public class StatusItemsConfig {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	private static final ForgeConfigSpec.BooleanValue ALLOW_STATUS_ITEMS;

	private static final ForgeConfigSpec.BooleanValue ALLOW_BLAZEPOWDER_ON_OWNED;
	private static final ForgeConfigSpec.BooleanValue ALLOW_BLAZEPOWDER_ON_WILD;
	private static final ForgeConfigSpec.BooleanValue ALLOW_BLAZEPOWDER_ON_TRAINER;
	private static final ForgeConfigSpec.BooleanValue ALLOW_BLAZEPOWDER_ON_PLAYER;

	private static final ForgeConfigSpec.BooleanValue ALLOW_PUFFER_ON_OWNED;
	private static final ForgeConfigSpec.BooleanValue ALLOW_PUFFER_ON_WILD;
	private static final ForgeConfigSpec.BooleanValue ALLOW_PUFFER_ON_TRAINER;
	private static final ForgeConfigSpec.BooleanValue ALLOW_PUFFER_ON_PLAYER;

	static {
		ALLOW_STATUS_ITEMS = BUILDER
				.comment("If false, disregard any of the below config because status effect items will be entirely disabled.")
				.define("Allow Status Items", true);

		BUILDER.push("Blaze Powder Settings");
			ALLOW_BLAZEPOWDER_ON_OWNED = BUILDER
				.comment("Can it be used on owned Pokemon?")
				.define("Use On Owned Pokemon", true);

			ALLOW_BLAZEPOWDER_ON_WILD = BUILDER
					.comment("Can it be used on wild Pokemon?")
					.define("Use On Wild Pokemon", false);

			ALLOW_BLAZEPOWDER_ON_TRAINER = BUILDER
					.comment("Can it be used on trainer Pokemon?")
					.define("Use On Trainer Pokemon", false);

			ALLOW_BLAZEPOWDER_ON_PLAYER = BUILDER
					.comment("Can it be used on other players' Pokemon?")
					.define("Use On Player Pokemon", false);
		BUILDER.pop();

		BUILDER.push("Pufferfish Settings");
			ALLOW_PUFFER_ON_OWNED = BUILDER
					.comment("Can it be used on owned Pokemon?")
					.define("Use On Owned Pokemon", true);

			ALLOW_PUFFER_ON_WILD = BUILDER
					.comment("Can it be used on wild Pokemon?")
					.define("Use On Wild Pokemon", false);

			ALLOW_PUFFER_ON_TRAINER = BUILDER
					.comment("Can it be used on trainer Pokemon?")
					.define("Use On Trainer Pokemon", false);

			ALLOW_PUFFER_ON_PLAYER = BUILDER
					.comment("Can it be used on other players' Pokemon?")
					.define("Use On Player Pokemon", false);
		BUILDER.pop();
	}

	public static final ForgeConfigSpec SPEC = BUILDER.build();

	public static boolean allowStatusItems;

	public static boolean allowBlazeOnOwned;
	public static boolean allowBlazeOnWild;
	public static boolean allowBlazeOnTrainer;
	public static boolean allowBlazeOnOtherPlayer;

	public static boolean allowPufferOnOwned;
	public static boolean allowPufferOnWild;
	public static boolean allowPufferOnTrainer;
	public static boolean allowPufferOnOtherPlayer;

	@SubscribeEvent
	static void onLoad(final ModConfig.ModConfigEvent event) {
		allowStatusItems = ALLOW_STATUS_ITEMS.get();

		allowBlazeOnOwned = ALLOW_BLAZEPOWDER_ON_OWNED.get();
		allowBlazeOnWild = ALLOW_BLAZEPOWDER_ON_WILD.get();
		allowBlazeOnTrainer = ALLOW_BLAZEPOWDER_ON_TRAINER.get();
		allowBlazeOnOtherPlayer = ALLOW_BLAZEPOWDER_ON_PLAYER.get();

		allowPufferOnOwned = ALLOW_PUFFER_ON_OWNED.get();
		allowPufferOnWild = ALLOW_PUFFER_ON_WILD.get();
		allowPufferOnTrainer = ALLOW_PUFFER_ON_TRAINER.get();
		allowPufferOnOtherPlayer = ALLOW_PUFFER_ON_PLAYER.get();
	}
}
