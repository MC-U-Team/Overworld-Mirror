package info.u_team.overworldmirror.block;

import java.util.Random;

import info.u_team.overworldmirror.init.OverworldMirrorDimensions;
import info.u_team.overworldmirror.portal.PortalTeleporter;
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
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
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
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos neighbor) {
		if (!neighbor.down().equals(pos) && !neighbor.up().equals(pos)) {
			worldIn.setBlockToAir(pos);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 0.75, 0, 1, 0.75, 1);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
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
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		// if (rand.nextInt(100) == 0) {
		// worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
		// (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT,
		// SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		// }
		//
		// for (int i = 0; i < 4; ++i) {
		// double d0 = (double) ((float) pos.getX() + rand.nextFloat());
		// double d1 = (double) ((float) pos.getY() + rand.nextFloat());
		// double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
		// double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
		// double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
		// double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
		// int j = rand.nextInt(2) * 2 - 1;
		//
		// if (worldIn.getBlockState(pos.west()).getBlock() != this &&
		// worldIn.getBlockState(pos.east()).getBlock() != this) {
		// d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
		// d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
		// } else {
		// d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
		// d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
		// }
		//
		// worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
		// }
	}
	
}