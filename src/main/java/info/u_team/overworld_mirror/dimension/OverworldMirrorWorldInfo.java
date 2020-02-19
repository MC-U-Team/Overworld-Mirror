package info.u_team.overworld_mirror.dimension;

import info.u_team.overworld_mirror.config.ServerConfig;
import net.minecraft.world.*;
import net.minecraft.world.storage.*;

public class OverworldMirrorWorldInfo extends DerivedWorldInfo {
	
	private static final ServerConfig CONFIG = ServerConfig.getInstance();
	
	private final World world;
	
	public OverworldMirrorWorldInfo(World world, WorldInfo worldInfo) {
		super(worldInfo);
		this.world = world;
	}
	
	@Override
	public long getSeed() {
		return CONFIG.seedType.get().calculateSeed(CONFIG.seedValue.get(), super.getSeed());
	}
	
	@Override
	public long getGameTime() {
		return super.getGameTime();
	}
	
	@Override
	public void setGameTime(long time) {
		super.setGameTime(time);
	}
}
