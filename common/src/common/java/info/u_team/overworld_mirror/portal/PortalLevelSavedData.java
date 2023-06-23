package info.u_team.overworld_mirror.portal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;

public class PortalLevelSavedData extends SavedData {
	
	private final List<BlockPos> portals;
	
	public PortalLevelSavedData() {
		this(Collections.emptyList());
	}
	
	public PortalLevelSavedData(List<BlockPos> entries) {
		portals = new ArrayList<>(entries);
	}
	
	public static PortalLevelSavedData load(CompoundTag compound) {
		return new PortalLevelSavedData(compound.getList("list", 10).stream().filter(tag -> tag instanceof CompoundTag).map(tag -> (CompoundTag) tag).map(entryCompound -> {
			return new BlockPos(entryCompound.getInt("x"), entryCompound.getInt("y"), entryCompound.getInt("z"));
		}).collect(Collectors.toList()));
	}
	
	@Override
	public CompoundTag save(CompoundTag compound) {
		final ListTag list = new ListTag();
		portals.forEach(pos -> {
			final CompoundTag entryCompound = new CompoundTag();
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
