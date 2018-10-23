package info.u_team.overworld_mirror.block;

import java.util.Random;

import info.u_team.overworld_mirror.init.OverworldMirrorDimensions;
import info.u_team.overworld_mirror.portal.*;
import info.u_team.u_team_core.block.UBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.*;

public class BlockOverworldMirrorPortal extends UBlock {
	
	public BlockOverworldMirrorPortal(String name) {
		super(name, Material.PORTAL);
		setTickRandomly(true);
		setHardness(-1.0F);
		setSoundType(SoundType.GLASS);
		setLightLevel(0.75F);
	}
	
	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		// if (!entityIn.isRiding() && !entityIn.isBeingRidden() &&
		// entityIn.isNonBoss()) {
		// entityIn.setPortal(pos);
		// }
		
		if (!entity.isRiding() && !entity.isBeingRidden() && entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			
			if (player.timeUntilPortal > 0) {
				player.timeUntilPortal = 10;
			} else if (player.dimension == 0) {
				player.timeUntilPortal = 10;
				server.getPlayerList().transferPlayerToDimension(player, OverworldMirrorDimensions.dimension_id, new PortalTeleporter());
			} else if (player.dimension == OverworldMirrorDimensions.dimension_id) {
				player.timeUntilPortal = 10;
				server.getPlayerList().transferPlayerToDimension(player, 0, new PortalTeleporter());
			}
		}
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
		WorldSaveDataPortal data = WorldSaveDataPortal.get(world);
		data.getPortals().removeIf(portal -> portal.equals(pos));
		data.markDirty();
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos neighbor) {
		if (!neighbor.down().equals(pos) && !neighbor.up().equals(pos)) {
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