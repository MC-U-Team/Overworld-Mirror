package info.u_team.overworldmirror.dimension;

import info.u_team.overworldmirror.config.CommonConfig;
import info.u_team.overworldmirror.init.OverworldMirrorDimensions;
import net.minecraft.world.*;

public class WorldProviderMirroredSurface extends WorldProviderSurface {
	
	@Override
	public DimensionType getDimensionType() {
		return OverworldMirrorDimensions.dimension_type;
	}
	
	@Override
	public void setWorld(World world) {
		this.world = world;
		
		terrainType = WorldType.byName(CommonConfig.settings.world_type);
		if(terrainType == null) {
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
	
}
