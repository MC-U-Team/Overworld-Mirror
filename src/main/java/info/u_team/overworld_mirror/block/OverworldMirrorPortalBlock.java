package info.u_team.overworld_mirror.block;

import info.u_team.overworld_mirror.init.*;
import info.u_team.overworld_mirror.portal.*;
import info.u_team.u_team_core.block.UBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ModDimension;

public class OverworldMirrorPortalBlock extends UBlock {
	
	protected static final VoxelShape SHAPE = makeCuboidShape(0, 11.9, 0, 16, 12, 16);
	
	public OverworldMirrorPortalBlock(String name) {
		super(name, Properties.create(Material.PORTAL).doesNotBlockMovement().hardnessAndResistance(-1.0F).sound(SoundType.GLASS).lightValue(11).noDrops());
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!entity.isPassenger() && !entity.isBeingRidden()) {
			final DimensionType type = entity.dimension;
			final ModDimension modDimension = type.getModType();
			
			if (entity.timeUntilPortal > 0) {
				entity.timeUntilPortal = 10;
			} else if (type == DimensionType.OVERWORLD) {
				entity.timeUntilPortal = 10;
				entity.changeDimension(DimensionType.byName(OverworldMirrorModDimensions.DIMENSION.getRegistryName()), new PortalTeleporter());
			} else if (modDimension != null && modDimension == OverworldMirrorModDimensions.DIMENSION) {
				entity.timeUntilPortal = 10;
				entity.changeDimension(DimensionType.OVERWORLD, new PortalTeleporter());
			}
		}
	}
	
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (world instanceof ServerWorld) {
			final PortalWorldSavedData data = PortalManager.getSavedData((ServerWorld) world);
			data.getPortals().removeIf(portal -> portal.equals(pos));
			data.markDirty();
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!fromPos.down().equals(pos) && !fromPos.up().equals(pos) && world.getBlockState(fromPos).getBlock() != OverworldMirrorBlocks.PORTAL) {
			world.removeBlock(pos, isMoving);
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}
	
}