package info.u_team.overworld_mirror.dimension;

import info.u_team.overworld_mirror.config.ServerConfig;
import net.minecraft.world.World;
import net.minecraft.world.dimension.*;

public class DimensionOverworldMirror extends OverworldDimension {
	
	public DimensionOverworldMirror(World world, DimensionType type) {
		super(world, type);
		world.worldInfo = new OverworldMirrorWorldInfo(world, world.worldInfo);
	}
	
	@Override
	public double getMovementFactor() {
		return ServerConfig.getInstance().movementFactor.get();
	}
	
	// // Time
	//
	// @Override
	// public void setWorldTime(long time) {
	// saveData.setTime(time);
	// }
	//
	// @Override
	// public long getWorldTime() {
	// return saveData.getTime();
	// }
	//
	// // Seed
	//
	// @Override
	// public long getSeed() {
	// return config.seedType.get().calculateSeed(config.seedValue.get(), super.getSeed());
	// }
	//
	// // Movement factor
	// @Override
	// public double getMovementFactor() {
	// return config.movementFactor.get();
	// }
	//
}
