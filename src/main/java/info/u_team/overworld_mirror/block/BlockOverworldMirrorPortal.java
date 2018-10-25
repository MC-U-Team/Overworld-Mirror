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
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockOverworldMirrorPortal extends UBlock {
	
	public BlockOverworldMirrorPortal(String name) {
		super(name, Material.PORTAL);
		// setTickRandomly(true);
		setHardness(-1.0F);
		setSoundType(SoundType.GLASS);
		setLightLevel(0.75F);
	}
	
	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (!entity.isRiding() && !entity.isBeingRidden()) {
			if (entity.timeUntilPortal > 0) {
				entity.timeUntilPortal = 10;
			} else if (entity.dimension == 0) {
				entity.timeUntilPortal = 10;
				entity.changeDimension(OverworldMirrorDimensions.dimension_id, new PortalTeleporter());
			} else if (entity.dimension == OverworldMirrorDimensions.dimension_id) {
				entity.timeUntilPortal = 10;
				entity.changeDimension(0, new PortalTeleporter());
			}
		}
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
		WorldSaveDataPortal data = PortalManager.getSaveData(world);
		data.getPortals().removeIf(portal -> portal.equals(pos));
		data.markDirty();
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos neighbor) {
		if (!neighbor.down().equals(pos) && !neighbor.up().equals(pos) && world.getBlockState(neighbor).getBlock() != OverworldMirrorBlocks.portal) {
			world.setBlockToAir(pos);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 0.75, 0, 1, 0.75, 1);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}
	
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
}