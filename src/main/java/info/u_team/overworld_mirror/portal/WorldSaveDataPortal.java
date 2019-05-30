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
	public void read(NBTTagCompound nbt) {
		NBTTagList list = nbt.getList("list", 10);
		if (list != null) {
			list.forEach(tag -> {
				NBTTagCompound compound = (NBTTagCompound) tag;
				portals.add(new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z")));
			});
		}
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();
		portals.forEach(pos -> {
			NBTTagCompound compound = new NBTTagCompound();
			compound.putInt("x", pos.getX());
			compound.putInt("y", pos.getY());
			compound.putInt("z", pos.getZ());
			list.add(compound);
		});
		nbt.put("list", list);
		return nbt;
	}
	
	public List<BlockPos> getPortals() {
		return portals;
	}
	
}
