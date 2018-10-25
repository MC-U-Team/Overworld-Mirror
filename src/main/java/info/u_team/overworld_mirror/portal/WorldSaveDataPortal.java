package info.u_team.overworld_mirror.portal;

import java.util.*;

import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

public class WorldSaveDataPortal extends WorldSavedData {
	
	private List<BlockPos> portals = new ArrayList<>();
	
	public WorldSaveDataPortal(String name) {
		super(name);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("list", 10);
		if (list != null) {
			list.forEach(tag -> {
				NBTTagCompound compound = (NBTTagCompound) tag;
				portals.add(new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z")));
			});
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();
		portals.forEach(pos -> {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setInteger("x", pos.getX());
			compound.setInteger("y", pos.getY());
			compound.setInteger("z", pos.getZ());
			list.appendTag(compound);
		});
		nbt.setTag("list", list);
		return nbt;
	}
	
	public List<BlockPos> getPortals() {
		return portals;
	}
	
}
