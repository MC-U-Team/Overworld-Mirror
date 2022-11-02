package info.u_team.overworld_mirror.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

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
	
	public final DoubleValue portalSearchDistanceOverworld;
	public final DoubleValue portalSearchDistanceOverworldMirror;
	
	private ServerConfig(Builder builder) {
		builder.comment("To configure the dimension and dimension type please create a data pack and add a dimension type in this resource location: data/overworldmirror/dimension_type/overworld_mirror.json and a dimension in this resource location: data/overworldmirror/dimension/overworld.json").push("information");
		builder.define("information", "");
		builder.pop();
		
		builder.comment("Portal settings").push("portal");
		portalSearchDistanceOverworld = builder.comment("How many blocks the portal can be from the normal spawn location to not create a new one.").defineInRange("portalSearchDistanceOverworld", 30, 1, 1e10);
		portalSearchDistanceOverworldMirror = builder.comment("How many blocks the portal can be from the normal spawn location to not create a new one.").defineInRange("portalSearchDistanceOverworldMirror", 30, 1, 1e10);
		builder.pop();
	}
	
}
