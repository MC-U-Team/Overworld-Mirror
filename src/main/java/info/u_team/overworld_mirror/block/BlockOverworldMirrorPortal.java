package info.u_team.overworld_mirror.block;

import java.util.Random;

import info.u_team.overworld_mirror.init.*;
import info.u_team.overworld_mirror.portal.*;
import info.u_team.u_team_core.block.UBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.world.dimension.DimensionType;

public class BlockOverworldMirrorPortal extends UBlock {
	
	public BlockOverworldMirrorPortal(String name) {
		super(name, Properties.create(Material.PORTAL).doesNotBlockMovement().hardnessAndResistance(-1.0F).sound(SoundType.GLASS).lightValue(11));
	}
	
	@Override
	public void onEntityCollision(IBlockState state, World world, BlockPos pos, Entity entity) {
		if (!entity.isPassenger() && !entity.isBeingRidden()) {
			if (entity.timeUntilPortal > 0) {
				entity.timeUntilPortal = 10;
			} else if (entity.dimension == DimensionType.OVERWORLD) {
				entity.timeUntilPortal = 10;
				entity.changeDimension(OverworldMirrorDimensions.dimension_type, new PortalTeleporter());
			} else if (entity.dimension == OverworldMirrorDimensions.dimension_type) {
				entity.timeUntilPortal = 10;
				entity.changeDimension(DimensionType.OVERWORLD, new PortalTeleporter());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving) {
		super.onReplaced(state, world, pos, newState, isMoving);
		WorldSaveDataPortal data = PortalManager.getSaveData(world);
		data.getPortals().removeIf(portal -> portal.equals(pos));
		data.markDirty();
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos neighbor) {
		if (!neighbor.down().equals(pos) && !neighbor.up().equals(pos) && world.getBlockState(neighbor).getBlock() != OverworldMirrorBlocks.portal) {
			world.removeBlock(pos);
		}
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return makeCuboidShape(0, 0.75, 0, 1, 0.75, 1);
	}
	
	@Override
	public int quantityDropped(IBlockState state, Random random) {
		return 0;
	}
	
	public ItemStack getItem(IBlockReader world, BlockPos pos, IBlockState state) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
}