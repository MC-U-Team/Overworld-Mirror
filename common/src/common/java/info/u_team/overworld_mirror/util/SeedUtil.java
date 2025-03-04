package info.u_team.overworld_mirror.util;

import info.u_team.overworld_mirror.config.CommonConfig;
import net.minecraft.server.MinecraftServer;

public class SeedUtil {
	
	public static long getOverworldMirrorSeed(MinecraftServer server) {
		final long seedValue = CommonConfig.getInstance().seed().get();
		
		if (CommonConfig.getInstance().seedAddition().get()) {
			final long overworldSeed = server.getWorldData().worldGenOptions().seed();
			return overworldSeed + seedValue;
		} else {
			return seedValue;
		}
	}
	
}
