package info.u_team.overworld_mirror.dimension;

import info.u_team.overworld_mirror.config.ServerConfig;
import net.minecraft.world.storage.*;

public class SeedFixWorldInfo extends DerivedWorldInfo {

	public SeedFixWorldInfo(WorldInfo worldInfo) {
		super(worldInfo);
	}

	@Override
	public long getSeed() {
		final ServerConfig config = ServerConfig.getInstance();
		return config.seedType.get().calculateSeed(config.seedValue.get(), super.getSeed());
	}
}
