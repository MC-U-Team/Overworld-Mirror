package info.u_team.overworld_mirror.block;

import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import info.u_team.overworld_mirror.init.OverworldMirrorLevelKeys;
import info.u_team.overworld_mirror.portal.PortalLevelSavedData;
import info.u_team.overworld_mirror.portal.PortalManager;
import info.u_team.u_team_core.block.UBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OverworldMirrorPortalBlock extends UBlock implements Portal {
	
	protected static final VoxelShape SHAPE = box(0, 11.9, 0, 16, 12, 16);
	
	public OverworldMirrorPortalBlock() {
		super(Properties.of().noCollission().strength(-1.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noLootTable().pushReaction(PushReaction.BLOCK));
	}
	
	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity.canUsePortal(false)) {
			entity.setAsInsidePortal(this, pos);
		}
	}
	
	@Override
	public int getPortalTransitionTime(ServerLevel level, Entity entity) {
		return entity instanceof Player ? 1 : 0;
	}
	
	@Override
	public DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
		final ServerLevel newLevel;
		if (level.dimension() == Level.OVERWORLD) {
			newLevel = level.getServer().getLevel(OverworldMirrorLevelKeys.MIRROR_OVERWORLD);
		} else if (level.dimension() == OverworldMirrorLevelKeys.MIRROR_OVERWORLD) {
			newLevel = level.getServer().getLevel(Level.OVERWORLD);
		} else {
			newLevel = null;
		}
		
		if (newLevel == null) {
			return null;
		}
		
		return PortalManager.findOrCreatePortal(newLevel, entity);
	}
	
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (level instanceof final ServerLevel serverLevel) {
			final PortalLevelSavedData data = PortalManager.getSavedData(serverLevel);
			data.getPortals().removeIf(portal -> portal.equals(pos));
			data.setDirty();
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!fromPos.below().equals(pos) && !fromPos.above().equals(pos) && level.getBlockState(fromPos).getBlock() != OverworldMirrorBlocks.PORTAL.get()) {
			level.removeBlock(pos, isMoving);
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}
	
}