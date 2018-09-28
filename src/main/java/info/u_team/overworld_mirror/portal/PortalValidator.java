package info.u_team.overworld_mirror.portal;

import java.util.ArrayList;

import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PortalValidator {
	
	private World world;
	private BlockPos pos;
	
	private State state;
	
	private ArrayList<BlockPos> portal = new ArrayList<>();
	
	public PortalValidator(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
		state = checkState();
	}
	
	private State checkState() {
		Block north = world.getBlockState(pos.north()).getBlock();
		Block east = world.getBlockState(pos.east()).getBlock();
		Block south = world.getBlockState(pos.south()).getBlock();
		Block west = world.getBlockState(pos.west()).getBlock();
		
		if (north == Blocks.STONEBRICK && west == Blocks.STONEBRICK) {
		} else if (north == Blocks.STONEBRICK && east == Blocks.STONEBRICK) {
			pos = pos.west();
		} else if (south == Blocks.STONEBRICK && west == Blocks.STONEBRICK) {
			pos = pos.north();
		} else if (south == Blocks.STONEBRICK && east == Blocks.STONEBRICK) {
			pos = pos.north().west();
		} else {
			return State.NONE;
		}
		return validateFrame();
	}
	
	private State validateFrame() {
		ArrayList<BlockPos> stone = new ArrayList<>();
		
		stone.add(pos.north());
		stone.add(pos.north().east());
		stone.add(pos.east(2));
		stone.add(pos.south().east(2));
		stone.add(pos.south(2).east());
		stone.add(pos.south(2));
		stone.add(pos.south().west());
		stone.add(pos.west());
		
		portal.add(pos);
		portal.add(pos.east());
		portal.add(pos.south().east());
		portal.add(pos.south());
		
		boolean bool = stone.stream().allMatch(pos -> world.getBlockState(pos).getBlock() == Blocks.STONEBRICK);
		boolean bool2 = portal.stream().allMatch(pos -> world.getBlockState(pos).getBlock() instanceof BlockFlower);
		
		return bool && bool2 ? State.WORKING : State.NONE;
	}
	
	public boolean create() {
		if (state == State.NONE) {
			return false;
		}
		portal.forEach(pos -> world.setBlockState(pos, OverworldMirrorBlocks.portal.getDefaultState(), 2));
		return true;
	}
	
	private enum State {
		NONE,
		WORKING;
	}
	
}
