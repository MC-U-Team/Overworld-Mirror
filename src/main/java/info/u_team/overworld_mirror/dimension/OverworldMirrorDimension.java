package info.u_team.overworld_mirror.dimension;

import info.u_team.overworld_mirror.config.ServerConfig;
import net.minecraft.world.World;
import net.minecraft.world.dimension.*;

public class OverworldMirrorDimension extends OverworldDimension {
	
	public OverworldMirrorDimension(World world, DimensionType type) {
		super(world, type);
		world.worldInfo = new OverworldMirrorWorldInfo(world.worldInfo);
	}
	
	@Override
	public double getMovementFactor() {
		return ServerConfig.getInstance().movementFactor.get();
	}
	
}
