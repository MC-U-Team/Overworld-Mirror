package info.u_team.overworld_mirror.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class DimensionDataWorldSavedData extends SavedData {
	
	private long dayTime;
	
	public DimensionDataWorldSavedData() {
		this(0);
	}
	
	public DimensionDataWorldSavedData(long dayTime) {
		this.dayTime = dayTime;
	}
	
	public static DimensionDataWorldSavedData load(CompoundTag compound) {
		return new DimensionDataWorldSavedData(compound.getLong("dayTime"));
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
