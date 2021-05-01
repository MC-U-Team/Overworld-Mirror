package info.u_team.overworld_mirror.block;

import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import info.u_team.overworld_mirror.init.OverworldMirrorWorldKeys;
import info.u_team.overworld_mirror.portal.PortalManager;
import info.u_team.overworld_mirror.portal.PortalTeleporter;
import info.u_team.overworld_mirror.portal.PortalWorldSavedData;
import info.u_team.u_team_core.block.UBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class OverworldMirrorPortalBlock extends UBlock {
	
	protected static final VoxelShape SHAPE = makeCuboidShape(0, 11.9, 0, 16, 12, 16);
	
	public OverworldMirrorPortalBlock() {
		super(Properties.create(Material.PORTAL).doesNotBlockMovement().hardnessAndResistance(-1.0F).sound(SoundType.GLASS).setLightLevel(state -> 11).noDrops());
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!entity.isPassenger() && !entity.isBeingRidden()) {
			if (entity.hasPortalCooldown()) {
				entity.setPortalCooldown();
			} else if (world.getDimensionKey() == World.OVERWORLD) {
				entity.setPortalCooldown();
				entity.changeDimension(world.getServer().getWorld(OverworldMirrorWorldKeys.MIRROR_OVERWORLD), new PortalTeleporter());
			} else if (world.getDimensionKey() == OverworldMirrorWorldKeys.MIRROR_OVERWORLD) {
				entity.setPortalCooldown();
				entity.changeDimension(world.getServer().getWorld(World.OVERWORLD), new PortalTeleporter());
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
		if (!fromPos.down().equals(pos) && !fromPos.up().equals(pos) && world.getBlockState(fromPos).getBlock() != OverworldMirrorBlocks.PORTAL.get()) {
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