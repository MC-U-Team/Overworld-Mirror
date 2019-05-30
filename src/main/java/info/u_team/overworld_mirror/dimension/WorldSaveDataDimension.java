package info.u_team.overworld_mirror.dimension;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class WorldSaveDataDimension extends WorldSavedData {
	
	private long time;
	
	public WorldSaveDataDimension(String name) {
		super(name);
	}
	
	@Override
	public void read(NBTTagCompound compound) {
		time = compound.getLong("time");
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
		compound.putLong("time", time);
		return compound;
	}
	
	public void setTime(long time) {
		this.time = time;
		markDirty();
	}
	
	public long getTime() {
		return time;
	}
	
}
