package info.u_team.overworld_mirror.portal;

import java.util.*;

import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.*;

public class WorldSaveDataPortal extends WorldSavedData {
	
	private static final String data_name = "overworldmirror_portal";
	
	private List<BlockPos> portals = new ArrayList<>();
	
	public WorldSaveDataPortal() {
		super(data_name);
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
	
	public static WorldSaveDataPortal get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		WorldSaveDataPortal instance = (WorldSaveDataPortal) storage.getOrLoadData(WorldSaveDataPortal.class, data_name);
		
		if (instance == null) {
			instance = new WorldSaveDataPortal();
			storage.setData(data_name, instance);
		}
		return instance;
	}
	
}
