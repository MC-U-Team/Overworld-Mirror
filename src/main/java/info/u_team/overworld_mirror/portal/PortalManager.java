package info.u_team.overworld_mirror.portal;

import java.util.*;

import info.u_team.overworld_mirror.config.ServerConfig;
import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import info.u_team.u_team_core.util.world.WorldUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

public class PortalManager {
	
	public static boolean trySpawnPortalFromFrame(ServerWorld world, BlockPos pos) {
		
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
		data.markDirty();
		
		return true;
	}
	
	private static boolean validatePortalFrameAndSpawnPortal(World world, BlockPos pos) {
		final List<BlockPos> flowers = new ArrayList<>();
		final List<BlockPos> frame = new ArrayList<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				flowers.add(pos.add(i, 0, j));
			}
		}
		
		for (int i = -1; i <= 1; i++) {
			frame.add(pos.add(2, 0, i));
			frame.add(pos.add(-2, 0, i));
			frame.add(pos.add(i, 0, 2));
			frame.add(pos.add(i, 0, -2));
		}
		
		final boolean flowersMatching = flowers.stream().allMatch(flowerPos -> world.getBlockState(flowerPos).getBlock() instanceof FlowerBlock);
		final boolean frameMatching = frame.stream().allMatch(framePos -> world.getBlockState(framePos).getBlock() == Blocks.STONE_BRICKS);
		
		if (flowersMatching && frameMatching) {
			flowers.forEach(portalPos -> world.setBlockState(portalPos, OverworldMirrorBlocks.PORTAL.getDefaultState(), 2));
			
			final PlayerList playerlist = world.getServer().getPlayerList();
			flowers.forEach(portalPos -> playerlist.sendToAllNearExcept(null, portalPos.getX(), portalPos.getY(), portalPos.getZ(), 64, world.getDimension().getType(), new SChangeBlockPacket(world, portalPos)));
			
			return true;
		}
		return false;
	}
	
	public static void trySummonEntityInPortal(ServerWorld world, Entity entity, float yaw) {
		final BlockPos entityPos = entity.getPosition();
		
		final PortalWorldSavedData data = getSavedData(world);
		
		BlockPos middlePos = null;
		
		final Iterator<BlockPos> iterator = data.getPortals().iterator();
		while (iterator.hasNext()) {
			final BlockPos pos = iterator.next();
			if (distanceSq(pos.getX(), pos.getZ(), entityPos.getX(), entityPos.getZ()) < ServerConfig.getInstance().portalDistance.get()) {
				if (validatePortal(world, pos)) {
					middlePos = pos;
					break;
				} else {
					iterator.remove();
					data.markDirty();
				}
			}
		}
		if (middlePos == null) {
			middlePos = spawnPortal(world, entityPos);
			data.getPortals().add(middlePos);
			data.markDirty();
		}
		entity.setPositionAndRotation(middlePos.getX() + 0.5D, middlePos.getY(), middlePos.getZ() + 0.5F, yaw, entity.rotationPitch);
	}
	
	private static boolean validatePortal(World world, BlockPos pos) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (world.getBlockState(pos.add(i, 0, j)).getBlock() != OverworldMirrorBlocks.PORTAL) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static BlockPos spawnPortal(World world, BlockPos entityPos) {
		world.getChunk(entityPos); // This loads the chunk / generates it so we can determine the height
		
		final BlockPos pos = world.getHeight(Heightmap.Type.WORLD_SURFACE, entityPos).down();
		
		final ArrayList<BlockPos> portal = new ArrayList<>();
		final ArrayList<BlockPos> frame = new ArrayList<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				portal.add(pos.add(i, 0, j));
			}
		}
		
		for (int i = -2; i <= 2; i++) {
			frame.add(pos.add(2, 0, i));
			frame.add(pos.add(-2, 0, i));
			frame.add(pos.add(i, 0, 2));
			frame.add(pos.add(i, 0, -2));
		}
		
		frame.forEach(framePos -> {
			world.setBlockState(framePos, Blocks.STONE_BRICKS.getDefaultState());
			world.removeBlock(framePos.up(), false);
			world.removeBlock(framePos.up(2), false);
		});
		portal.forEach(portalPos -> {
			world.removeBlock(portalPos.up(), false);
			world.removeBlock(portalPos.up(2), false);
			world.setBlockState(portalPos.down(), Blocks.STONE_BRICKS.getDefaultState());
		});
		
		portal.forEach(portalPos -> world.setBlockState(portalPos, OverworldMirrorBlocks.PORTAL.getDefaultState(), 2));
		
		return pos;
	}
	
	public static PortalWorldSavedData getSavedData(ServerWorld world) {
		final String name = "overworldmirror_portal";
		return WorldUtil.getSaveData(world, name, () -> new PortalWorldSavedData(name));
	}
	
	public static double distanceSq(double fromX, double fromZ, double toX, double toZ) {
		double x = fromX - toX;
		double z = fromZ - toZ;
		return x * x + z * z;
	}
}
