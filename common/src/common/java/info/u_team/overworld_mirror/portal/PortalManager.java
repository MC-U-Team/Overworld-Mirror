package info.u_team.overworld_mirror.portal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import info.u_team.overworld_mirror.config.CommonConfig;
import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import info.u_team.u_team_core.util.LevelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

public class PortalManager {
	
	public static boolean trySpawnPortalFromFrame(ServerLevel level, BlockPos pos) {
		
		int westCount = 0;
		while (level.getBlockState(pos.west(westCount + 1)).getBlock() != Blocks.STONE_BRICKS && westCount < 3) {
			westCount++;
		}
		
		final BlockPos westPost = pos.west(westCount);
		
		int northCount = 0;
		while (level.getBlockState(westPost.north(northCount + 1)).getBlock() != Blocks.STONE_BRICKS && northCount < 3) {
			northCount++;
		}
		
		final BlockPos westNorthPos = westPost.north(northCount);
		
		final BlockPos middlePos = westNorthPos.east().south();
		
		if (!validatePortalFrameAndSpawnPortal(level, middlePos)) {
			return false;
		}
		
		final PortalLevelSavedData data = getSavedData(level);
		data.getPortals().add(middlePos);
		data.setDirty();
		
		return true;
	}
	
	private static boolean validatePortalFrameAndSpawnPortal(Level level, BlockPos pos) {
		final List<BlockPos> flowers = new ArrayList<>();
		final List<BlockPos> frame = new ArrayList<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				flowers.add(pos.offset(i, 0, j));
			}
		}
		
		for (int i = -1; i <= 1; i++) {
			frame.add(pos.offset(2, 0, i));
			frame.add(pos.offset(-2, 0, i));
			frame.add(pos.offset(i, 0, 2));
			frame.add(pos.offset(i, 0, -2));
		}
		
		final boolean flowersMatching = flowers.stream().allMatch(flowerPos -> level.getBlockState(flowerPos).getBlock() instanceof FlowerBlock);
		final boolean frameMatching = frame.stream().allMatch(framePos -> level.getBlockState(framePos).getBlock() == Blocks.STONE_BRICKS);
		
		if (flowersMatching && frameMatching) {
			flowers.forEach(portalPos -> level.setBlock(portalPos, OverworldMirrorBlocks.PORTAL.get().defaultBlockState(), 2));
			
			final PlayerList playerlist = level.getServer().getPlayerList();
			flowers.forEach(portalPos -> playerlist.broadcast(null, portalPos.getX(), portalPos.getY(), portalPos.getZ(), 64, level.dimension(), new ClientboundBlockUpdatePacket(level, portalPos)));
			
			return true;
		}
		return false;
	}
	
	public static PortalInfo findOrCreatePortal(ServerLevel destinationLevel, Entity entity) {
		final WorldBorder border = destinationLevel.getWorldBorder();
		
		final double coordinateScale = DimensionType.getTeleportationScale(entity.getCommandSenderWorld().dimensionType(), destinationLevel.dimensionType());
		final BlockPos estimatedPos = border.clampToBounds(entity.getX() * coordinateScale, entity.getY(), entity.getZ() * coordinateScale);
		
		final PortalLevelSavedData data = getSavedData(destinationLevel);
		final CommonConfig config = CommonConfig.getInstance();
		final double searchDistance = Math.pow(destinationLevel.dimension() == Level.OVERWORLD ? config.portalSearchDistanceOverworld().get() : config.portalSearchDistanceOverworldMirror().get(), 2);
		
		BlockPos portalMiddlePos = null;
		
		final Iterator<BlockPos> iterator = data.getPortals().iterator();
		while (iterator.hasNext()) {
			final BlockPos pos = iterator.next();
			
			if (getPlaneDistanceSq(pos.getX(), pos.getZ(), estimatedPos.getX(), estimatedPos.getZ()) < searchDistance) {
				if (validatePortal(destinationLevel, pos)) {
					portalMiddlePos = pos;
					break;
				} else {
					iterator.remove();
					data.setDirty();
				}
			}
		}
		if (portalMiddlePos == null) {
			portalMiddlePos = spawnPortal(destinationLevel, estimatedPos);
			data.getPortals().add(portalMiddlePos);
			data.setDirty();
		}
		
		return new PortalInfo(Vec3.upFromBottomCenterOf(portalMiddlePos, 0.25), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
	}
	
	private static boolean validatePortal(Level level, BlockPos pos) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (level.getBlockState(pos.offset(i, 0, j)).getBlock() != OverworldMirrorBlocks.PORTAL.get()) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static BlockPos spawnPortal(Level level, BlockPos entityPos) {
		level.getChunk(entityPos); // This loads the chunk / generates it so we can determine the height
		
		final BlockPos pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, entityPos).below();
		
		final ArrayList<BlockPos> portal = new ArrayList<>();
		final ArrayList<BlockPos> frame = new ArrayList<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				portal.add(pos.offset(i, 0, j));
			}
		}
		
		for (int i = -2; i <= 2; i++) {
			frame.add(pos.offset(2, 0, i));
			frame.add(pos.offset(-2, 0, i));
			frame.add(pos.offset(i, 0, 2));
			frame.add(pos.offset(i, 0, -2));
		}
		
		frame.forEach(framePos -> {
			level.setBlockAndUpdate(framePos, Blocks.STONE_BRICKS.defaultBlockState());
			level.removeBlock(framePos.above(), false);
			level.removeBlock(framePos.above(2), false);
		});
		portal.forEach(portalPos -> {
			level.removeBlock(portalPos.above(), false);
			level.removeBlock(portalPos.above(2), false);
			level.setBlockAndUpdate(portalPos.below(), Blocks.STONE_BRICKS.defaultBlockState());
		});
		
		portal.forEach(portalPos -> level.setBlock(portalPos, OverworldMirrorBlocks.PORTAL.get().defaultBlockState(), Block.UPDATE_CLIENTS));
		
		return pos;
	}
	
	public static PortalLevelSavedData getSavedData(ServerLevel level) {
		final String name = "overworldmirror_portal";
		return LevelUtil.getSaveData(level, name, PortalLevelSavedData::load, PortalLevelSavedData::new);
	}
	
	public static float getPlaneDistanceSq(int x1, int z1, int x2, int z2) {
		final int xDiff = x2 - x1;
		final int zDiff = z2 - z1;
		return xDiff * xDiff + zDiff * zDiff;
	}
}
