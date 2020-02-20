package info.u_team.overworld_mirror.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class ServerConfig {
	
	public static final ForgeConfigSpec CONFIG;
	private static final ServerConfig INSTANCE;
	
	static {
		final Pair<ServerConfig, ForgeConfigSpec> pair = new Builder().configure(ServerConfig::new);
		CONFIG = pair.getRight();
		INSTANCE = pair.getLeft();
	}
	
	public static ServerConfig getInstance() {
		return INSTANCE;
	}
	
	public final EnumValue<SeedType> seedType;
	public final LongValue seedValue;
	
	public final ConfigValue<String> generatorType;
	public final ConfigValue<String> generatorSettings;
	
	public final DoubleValue movementFactor;
	public final DoubleValue portalDistance;
	
	private ServerConfig(Builder builder) {
		builder.comment("Seed settings").push("seed");
		seedType = builder.comment("If you have set this to \"SEED\" then the seedValue value will be treated as new seed, else if its set to \"ADDITION\" the value will be added to the main world seed.").defineEnum("seedType", SeedType.ADDITION);
		seedValue = builder.comment("The seed value. See seedType for more information.").defineInRange("seedValue", 100_000, Long.MIN_VALUE, Long.MAX_VALUE);
		builder.pop();
		builder.comment("Generation settings").push("generator");
		generatorType = builder.comment("Generator type e.g. default, flat, buffet, ... (See here for a list: https://minecraft.gamepedia.com/World_type)").define("generatorType", "default");
		generatorSettings = builder.comment("Generator settings for the generator type").define("generatorSettings", "");
		builder.pop();
		builder.comment("Portal settings").push("portal");
		movementFactor = builder.comment("Movement factor. Like in the nether you move 8 times the block length as in the overworld").defineInRange("movementFactor", 1, 0, 1e10);
		portalDistance = builder.comment("How many blocks the portal can be from the normal spawn location to not create a new one. The value is not in sqrt cause of lag reduction. 400 = 20 Blocks, 900 = 30 Blocks, etc").defineInRange("portalDistance", 400, 1, 1e20);
		builder.pop();
	}
	
	public static enum SeedType {
		
		SEED,
		ADDITION;
		
		public long calculateSeed(long seedValue, long worldSeed) {
			if (this == SEED) {
				return seedValue;
			}
			return worldSeed + seedValue;
		}
	}
	
}
