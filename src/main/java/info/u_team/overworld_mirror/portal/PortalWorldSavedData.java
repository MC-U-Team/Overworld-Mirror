package info.u_team.overworld_mirror.portal;

import java.util.*;

import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

public class PortalWorldSavedData extends WorldSavedData {
	
	private final List<BlockPos> portals;
	
	public PortalWorldSavedData(String name) {
		super(name);
		portals = new ArrayList<>();
	}
	
	@Override
	public void read(CompoundNBT compound) {
		compound.getList("list", 10).stream().filter(tag -> tag instanceof CompoundNBT).map(tag -> (CompoundNBT) tag).forEach(entryCompound -> {
			portals.add(new BlockPos(entryCompound.getInt("x"), entryCompound.getInt("y"), entryCompound.getInt("z")));
		});
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		final ListNBT list = new ListNBT();
		portals.forEach(pos -> {
			final CompoundNBT entryCompound = new CompoundNBT();
			entryCompound.putInt("x", pos.getX());
			entryCompound.putInt("y", pos.getY());
			entryCompound.putInt("z", pos.getZ());
			list.add(entryCompound);
		});
		compound.put("list", list);
		return compound;
	}
	
	/**
	 * Changes must be marked dirty
	 */
	public List<BlockPos> getPortals() {
		return portals;
	}
	
}
