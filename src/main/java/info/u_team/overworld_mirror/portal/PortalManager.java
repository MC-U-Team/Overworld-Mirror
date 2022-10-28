package info.u_team.overworld_mirror.portal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import info.u_team.overworld_mirror.config.ServerConfig;
import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import info.u_team.u_team_core.util.LevelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.Mth;
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
	
	public static boolean trySpawnPortalFromFrame(ServerLevel world, BlockPos pos) {
		
		int westCount = 0;
		while (world.getBlockState(pos.west(westCount + 1)).getBlock() != Blocks.STONE_BRICKS && westCount < 3) {
			westCount++;
		}
		
		final BlockPos westPost = pos.west(westCount);
		
		int northCount = 0;
		while (world.getBlockState(westPost.north(northCount + 1)).getBlock() != Blocks.STONE_BRICKS && northCount < 3) {
			northCount++;
		}
		
		final BlockPos westNorthPos = westPost.north(northCount);
		
		final BlockPos middlePos = westNorthPos.east().south();
		
		if (!validatePortalFrameAndSpawnPortal(world, middlePos)) {
			return false;
		}
		
		final PortalWorldSavedData data = getSavedData(world);
		data.getPortals().add(middlePos);
		data.setDirty();
		
		return true;
	}
	
	private static boolean validatePortalFrameAndSpawnPortal(Level world, BlockPos pos) {
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
		
		final boolean flowersMatching = flowers.stream().allMatch(flowerPos -> world.getBlockState(flowerPos).getBlock() instanceof FlowerBlock);
		final boolean frameMatching = frame.stream().allMatch(framePos -> world.getBlockState(framePos).getBlock() == Blocks.STONE_BRICKS);
		
		if (flowersMatching && frameMatching) {
			flowers.forEach(portalPos -> world.setBlock(portalPos, OverworldMirrorBlocks.PORTAL.get().defaultBlockState(), 2));
			
			final PlayerList playerlist = world.getServer().getPlayerList();
			flowers.forEach(portalPos -> playerlist.broadcast(null, portalPos.getX(), portalPos.getY(), portalPos.getZ(), 64, world.dimension(), new ClientboundBlockUpdatePacket(world, portalPos)));
			
			return true;
		}
		return false;
	}
	
	public static PortalInfo findOrCreatePortal(ServerLevel destinationWorld, Entity entity) {
		final WorldBorder border = destinationWorld.getWorldBorder();
		
		final double minX = Math.max(-2.9999872e7, border.getMinX() + 16);
		final double minZ = Math.max(-2.9999872e7, border.getMinZ() + 16);
		final double maxX = Math.min(2.9999872e7, border.getMaxX() - 16);
		final double maxZ = Math.min(2.9999872e7, border.getMaxZ() - 16);
		
		final double coordinateScale = DimensionType.getTeleportationScale(entity.getCommandSenderWorld().dimensionType(), destinationWorld.dimensionType());
		final BlockPos estimatedPos = new BlockPos(Mth.clamp(entity.getX() * coordinateScale, minX, maxX), entity.getY(), Mth.clamp(entity.getZ() * coordinateScale, minZ, maxZ));
		
		final PortalWorldSavedData data = getSavedData(destinationWorld);
		final ServerConfig config = ServerConfig.getInstance();
		final double searchDistance = Math.pow(destinationWorld.dimension() == Level.OVERWORLD ? config.portalSearchDistanceOverworld.get() : config.portalSearchDistanceOverworldMirror.get(), 2);
		
		BlockPos portalMiddlePos = null;
		
		final Iterator<BlockPos> iterator = data.getPortals().iterator();
		while (iterator.hasNext()) {
			final BlockPos pos = iterator.next();
			
			if (getPlaneDistanceSq(pos.getX(), pos.getZ(), estimatedPos.getX(), estimatedPos.getZ()) < searchDistance) {
				if (validatePortal(destinationWorld, pos)) {
					portalMiddlePos = pos;
					break;
				} else {
					iterator.remove();
					data.setDirty();
				}
			}
		}
		if (portalMiddlePos == null) {
			portalMiddlePos = spawnPortal(destinationWorld, estimatedPos);
			data.getPortals().add(portalMiddlePos);
			data.setDirty();
		}
		
		return new PortalInfo(Vec3.upFromBottomCenterOf(portalMiddlePos, 0.25), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
	}
	
	private static boolean validatePortal(Level world, BlockPos pos) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (world.getBlockState(pos.offset(i, 0, j)).getBlock() != OverworldMirrorBlocks.PORTAL.get()) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static BlockPos spawnPortal(Level world, BlockPos entityPos) {
		world.getChunk(entityPos); // This loads the chunk / generates it so we can determine the height
		
		final BlockPos pos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, entityPos).below();
		
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
			world.setBlockAndUpdate(framePos, Blocks.STONE_BRICKS.defaultBlockState());
			world.removeBlock(framePos.above(), false);
			world.removeBlock(framePos.above(2), false);
		});
		portal.forEach(portalPos -> {
			world.removeBlock(portalPos.above(), false);
			world.removeBlock(portalPos.above(2), false);
			world.setBlockAndUpdate(portalPos.below(), Blocks.STONE_BRICKS.defaultBlockState());
		});
		
		portal.forEach(portalPos -> world.setBlock(portalPos, OverworldMirrorBlocks.PORTAL.get().defaultBlockState(), Block.UPDATE_CLIENTS));
		
		return pos;
	}
	
	public static PortalWorldSavedData getSavedData(ServerLevel level) {
		final String name = "overworldmirror_portal";
		return LevelUtil.getSaveData(level, name, PortalWorldSavedData::load, PortalWorldSavedData::new);
	}
	
	public static float getPlaneDistanceSq(int x1, int z1, int x2, int z2) {
		final int xDiff = x2 - x1;
		final int zDiff = z2 - z1;
		return xDiff * xDiff + zDiff * zDiff;
	}
}
