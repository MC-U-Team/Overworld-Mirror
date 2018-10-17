package info.u_team.overworld_mirror.dimension;

import info.u_team.overworld_mirror.config.CommonConfig;
import info.u_team.overworld_mirror.init.OverworldMirrorDimensions;
import net.minecraft.world.*;

public class WorldProviderMirroredSurface extends WorldProviderSurface {
	
	private long time;
	
	@Override
	public DimensionType getDimensionType() {
		return OverworldMirrorDimensions.dimension_type;
	}
	
	@Override
	public void setWorld(World world) {
		this.world = world;
		
		time = world.getWorldInfo().getWorldTime();
		
		terrainType = WorldType.byName(CommonConfig.settings.world_type);
		if (terrainType == null) {
			terrainType = world.getWorldInfo().getTerrainType();
		}
		
		generatorSettings = CommonConfig.settings.generator_settings;
		
		init();
		generateLightBrightnessTable();
	}
	
	@Override
	public long getSeed() {
		return super.getSeed() + CommonConfig.settings.seed_shift;
	}
	
	@Override
	public double getMovementFactor() {
		return CommonConfig.settings.movement_factor;
	}
	
	@Override
	public long getWorldTime() {
		return time;
	}
	
	@Override
	public void setWorldTime(long time) {
		super.setWorldTime(time);
		this.time = time;
	}
	
}
