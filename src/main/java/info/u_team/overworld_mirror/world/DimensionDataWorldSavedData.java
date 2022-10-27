package info.u_team.overworld_mirror.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class DimensionDataWorldSavedData extends SavedData {
	
	private long dayTime;
	
	public DimensionDataWorldSavedData(String name) {
		super(name);
	}
	
	@Override
	public void load(CompoundTag compound) {
		dayTime = compound.getLong("dayTime");
	}
	
	@Override
	public CompoundTag save(CompoundTag compound) {
		compound.putLong("dayTime", dayTime);
		return compound;
	}
	
	public void updateDayTime(long dayTime) {
		this.dayTime = dayTime;
		setDirty();
	}
	
}
