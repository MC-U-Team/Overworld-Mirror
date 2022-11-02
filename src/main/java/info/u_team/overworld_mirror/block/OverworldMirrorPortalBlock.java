package info.u_team.overworld_mirror.block;

import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import info.u_team.overworld_mirror.init.OverworldMirrorLevelKeys;
import info.u_team.overworld_mirror.portal.PortalManager;
import info.u_team.overworld_mirror.portal.PortalTeleporter;
import info.u_team.overworld_mirror.portal.PortalLevelSavedData;
import info.u_team.u_team_core.block.UBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OverworldMirrorPortalBlock extends UBlock {
	
	protected static final VoxelShape SHAPE = box(0, 11.9, 0, 16, 12, 16);
	
	private final PortalTeleporter teleporter;
	
	public OverworldMirrorPortalBlock() {
		super(Properties.of(Material.PORTAL).noCollission().strength(-1.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noLootTable());
		teleporter = new PortalTeleporter();
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (world instanceof ServerLevel && world.getServer() != null && !entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions()) {
			final MinecraftServer server = world.getServer();
			if (entity.isOnPortalCooldown()) {
				entity.setPortalCooldown();
			} else if (world.dimension() == Level.OVERWORLD) {
				changeDimension(server, entity, OverworldMirrorLevelKeys.MIRROR_OVERWORLD);
			} else if (world.dimension() == OverworldMirrorLevelKeys.MIRROR_OVERWORLD) {
				changeDimension(server, entity, Level.OVERWORLD);
			}
		}
	}
	
	private void changeDimension(MinecraftServer server, Entity entity, ResourceKey<Level> key) {
		final ServerLevel newWorld = server.getLevel(key);
		if (newWorld == null) {
			return;
		}
		entity.setPortalCooldown();
		entity.changeDimension(newWorld, teleporter);
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (world instanceof ServerLevel) {
			final PortalLevelSavedData data = PortalManager.getSavedData((ServerLevel) world);
			data.getPortals().removeIf(portal -> portal.equals(pos));
			data.setDirty();
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!fromPos.below().equals(pos) && !fromPos.above().equals(pos) && world.getBlockState(fromPos).getBlock() != OverworldMirrorBlocks.PORTAL.get()) {
			world.removeBlock(pos, isMoving);
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}
	
}