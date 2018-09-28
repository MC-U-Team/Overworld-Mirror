package info.u_team.overworld_mirror.portal;

import java.util.ArrayList;

import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PortalCreator {
	
	private World world;
	private BlockPos pos;
	
	public PortalCreator(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos.down().south();
	}
	
	public void create() {
		ArrayList<BlockPos> stone = new ArrayList<>();
		
		stone.add(pos.north());
		stone.add(pos.north().east());
		stone.add(pos.east(2));
		stone.add(pos.south().east(2));
		stone.add(pos.south(2).east());
		stone.add(pos.south(2));
		stone.add(pos.south().west());
		stone.add(pos.west());
		stone.add(pos.north().west());
		stone.add(pos.north().east(2));
		stone.add(pos.south(2).west());
		stone.add(pos.south(2).east(2));
		
		ArrayList<BlockPos> portal = new ArrayList<>();
		
		portal.add(pos);
		portal.add(pos.east());
		portal.add(pos.south().east());
		portal.add(pos.south());
		
		stone.forEach(pos -> world.setBlockState(pos, Blocks.STONEBRICK.getDefaultState()));
		portal.forEach(pos -> world.setBlockState(pos, OverworldMirrorBlocks.portal.getDefaultState(), 2));
		
		stone.forEach(pos -> world.setBlockToAir(pos.up()));
		stone.forEach(pos -> world.setBlockToAir(pos.up(2)));
		
		portal.forEach(pos -> world.setBlockToAir(pos.up()));
		portal.forEach(pos -> world.setBlockToAir(pos.up(2)));
		
	}
	
}
