package info.u_team.overworld_mirror.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class CommonConfig {
	
	public static final ForgeConfigSpec config;
	private static final CommonConfig instance;
	
	static {
		Pair<CommonConfig, ForgeConfigSpec> pair = new Builder().configure(CommonConfig::new);
		config = pair.getRight();
		instance = pair.getLeft();
	}
	
	public static CommonConfig getInstance() {
		return instance;
	}
	
	public final EnumValue<SeedType> seedType;
	public final LongValue seedValue;
	
	public final ConfigValue<String> generatorType;
	public final ConfigValue<String> generatorSettings;
	
	private CommonConfig(Builder builder) {
		builder.comment("Mirrored Overworld settings").push("dimension");
		seedType = builder.comment("If you have set this to \"SEED\" then the seedValue value will be treated as new seed, else if its set to \"ADDITION\" the value will be added to the main world seed.").defineEnum("seedType", SeedType.ADDITION);
		seedValue = builder.comment("The seed value. See seedType for more information.").defineInRange("seedValue", 100_000, Long.MIN_VALUE, Long.MAX_VALUE);
		generatorType = builder.comment("Generator type").define("generatorType", "default");
		generatorSettings = builder.comment("Generator settings").define("generatorSettings", "");
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
