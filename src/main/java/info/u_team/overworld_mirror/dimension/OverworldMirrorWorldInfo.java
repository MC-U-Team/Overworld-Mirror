package info.u_team.overworld_mirror.dimension;

import info.u_team.overworld_mirror.config.ServerConfig;
import info.u_team.u_team_core.util.world.WorldUtil;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.*;

public class OverworldMirrorWorldInfo extends DerivedWorldInfo {
	
	private static final ServerConfig CONFIG = ServerConfig.getInstance();
	
	private final TimeWorldSavedData timeData;
	
	public OverworldMirrorWorldInfo(World world, WorldInfo worldInfo) {
		super(worldInfo);
		timeData = world instanceof ServerWorld ? getSavedData((ServerWorld) world) : new TimeWorldSavedData("dummy");
	}
	
	@Override
	public long getSeed() {
		return CONFIG.seedType.get().calculateSeed(CONFIG.seedValue.get(), super.getSeed());
	}
	
	@Override
	public long getGameTime() {
		return timeData.getTime();
	}
	
	@Override
	public void setGameTime(long time) {
		timeData.setTime(time);
	}
	
	public static TimeWorldSavedData getSavedData(ServerWorld world) {
		final String name = "overworldmirror_time";
		return WorldUtil.getSaveData(world, name, () -> new TimeWorldSavedData(name));
	}
}
