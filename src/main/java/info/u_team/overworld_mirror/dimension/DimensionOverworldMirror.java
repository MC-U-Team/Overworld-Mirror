package info.u_team.overworld_mirror.dimension;

import net.minecraft.world.dimension.*;

public class DimensionOverworldMirror extends OverworldDimension {
	
	private WorldSaveDataDimension saveData;
	
	public DimensionOverworldMirror(DimensionType type) {
		super(type);
	}
	
	@Override
	protected void init() {
		super.init();
		saveData = getSaveData();
	}
	
	@Override
	public boolean canDropChunk(int x, int z) {
		return false;
	}
	
	@Override
	public void setWorldTime(long time) {
		saveData.setTime(time);
	}
	
	@Override
	public long getWorldTime() {
		return saveData.getTime();
	}
	
	private WorldSaveDataDimension getSaveData() {
		final String name = "overworldmirror";
		final DimensionType type = getWorld().getDimension().getType();
		WorldSaveDataDimension instance = getWorld().getSavedData(type, WorldSaveDataDimension::new, name);
		if (instance == null) {
			instance = new WorldSaveDataDimension(name);
			getWorld().setSavedData(type, name, instance);
		}
		return instance;
	}
}
