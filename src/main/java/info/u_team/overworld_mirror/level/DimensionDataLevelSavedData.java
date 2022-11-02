package info.u_team.overworld_mirror.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class DimensionDataLevelSavedData extends SavedData {
	
	private long dayTime;
	
	public DimensionDataLevelSavedData() {
		this(0);
	}
	
	public DimensionDataLevelSavedData(long dayTime) {
		this.dayTime = dayTime;
	}
	
	public static DimensionDataLevelSavedData load(CompoundTag compound) {
		return new DimensionDataLevelSavedData(compound.getLong("dayTime"));
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
