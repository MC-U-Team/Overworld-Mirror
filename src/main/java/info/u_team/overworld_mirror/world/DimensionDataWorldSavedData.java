package info.u_team.overworld_mirror.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class DimensionDataWorldSavedData extends WorldSavedData {
	
	private long dayTime;
	
	public DimensionDataWorldSavedData(String name) {
		super(name);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		dayTime = compound.getLong("dayTime");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putLong("dayTime", dayTime);
		return compound;
	}
	
	public void updateDayTime(long dayTime) {
		this.dayTime = dayTime;
		markDirty();
	}
	
}
