package info.u_team.overworld_mirror.config;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.u_team_core.util.ConfigValueHolder;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;

public class NeoForgeCommonConfig {
	
	public static final ModConfigSpec CONFIG;
	private static final NeoForgeCommonConfig INSTANCE;
	
	static {
		final Pair<NeoForgeCommonConfig, ModConfigSpec> pair = new Builder().configure(NeoForgeCommonConfig::new);
		CONFIG = pair.getRight();
		INSTANCE = pair.getLeft();
	}
	
	public static NeoForgeCommonConfig getInstance() {
		return INSTANCE;
	}
	
	public final ConfigValueHolder<Double> portalSearchDistanceOverworld;
	public final ConfigValueHolder<Double> portalSearchDistanceOverworldMirror;
	
	private NeoForgeCommonConfig(Builder builder) {
		builder.comment("To configure the dimension and dimension type please create a data pack and add a dimension type in this resource location: data/overworldmirror/dimension_type/overworld_mirror.json and a dimension in this resource location: data/overworldmirror/dimension/overworld.json").push("information");
		builder.define("information", "");
		builder.pop();
		
		builder.comment("Portal settings").push("portal");
		final DoubleValue portalSearchDistanceOverworldValue = builder.comment("How many blocks the portal can be from the normal spawn location to not create a new one.").defineInRange("portalSearchDistanceOverworld", 30, 1, 1e10);
		portalSearchDistanceOverworld = new ConfigValueHolder<>(portalSearchDistanceOverworldValue, portalSearchDistanceOverworldValue::set);
		final DoubleValue portalSearchDistanceOverworldMirrorValue = builder.comment("How many blocks the portal can be from the normal spawn location to not create a new one.").defineInRange("portalSearchDistanceOverworldMirror", 30, 1, 1e10);
		portalSearchDistanceOverworldMirror = new ConfigValueHolder<>(portalSearchDistanceOverworldMirrorValue, portalSearchDistanceOverworldMirrorValue::set);
		builder.pop();
	}
	
	public static class Impl extends CommonConfig {
		
		@Override
		public ConfigValueHolder<Double> portalSearchDistanceOverworld() {
			return INSTANCE.portalSearchDistanceOverworld;
		}
		
		@Override
		public ConfigValueHolder<Double> portalSearchDistanceOverworldMirror() {
			return INSTANCE.portalSearchDistanceOverworldMirror;
		}
	}
}
