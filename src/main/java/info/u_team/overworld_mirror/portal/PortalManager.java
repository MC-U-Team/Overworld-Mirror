package info.u_team.overworld_mirror.portal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import info.u_team.overworld_mirror.config.ServerConfig;
import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import info.u_team.u_team_core.util.world.WorldUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.server.management.PlayerList;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.Constants.BlockFlags;

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
			flowers.forEach(portalPos -> world.setBlockState(portalPos, OverworldMirrorBlocks.PORTAL.get().getDefaultState(), 2));
			
			final PlayerList playerlist = world.getServer().getPlayerList();
			flowers.forEach(portalPos -> playerlist.sendToAllNearExcept(null, portalPos.getX(), portalPos.getY(), portalPos.getZ(), 64, world.getDimensionKey(), new SChangeBlockPacket(world, portalPos)));
			
			return true;
		}
		return false;
	}
	
	public static PortalInfo findOrCreatePortal(ServerWorld destinationWorld, Entity entity) {
		final WorldBorder border = destinationWorld.getWorldBorder();
		
		final double minX = Math.max(-2.9999872e7, border.minX() + 16);
		final double minZ = Math.max(-2.9999872e7, border.minZ() + 16);
		final double maxX = Math.min(2.9999872e7, border.maxX() - 16);
		final double maxZ = Math.min(2.9999872e7, border.maxZ() - 16);
		
		final double coordinateScale = DimensionType.getCoordinateDifference(entity.getEntityWorld().getDimensionType(), destinationWorld.getDimensionType());
		final BlockPos estimatedPos = new BlockPos(MathHelper.clamp(entity.getPosX() * coordinateScale, minX, maxX), entity.getPosY(), MathHelper.clamp(entity.getPosZ() * coordinateScale, minZ, maxZ));
		
		final PortalWorldSavedData data = getSavedData(destinationWorld);
		final ServerConfig config = ServerConfig.getInstance();
		final double searchDistance = Math.pow(destinationWorld.getDimensionKey() == World.OVERWORLD ? config.portalSearchDistanceOverworld.get() : config.portalSearchDistanceOverworldMirror.get(), 2);
		
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
					data.markDirty();
				}
			}
		}
		if (portalMiddlePos == null) {
			portalMiddlePos = spawnPortal(destinationWorld, estimatedPos);
			data.getPortals().add(portalMiddlePos);
			data.markDirty();
		}
		
		return new PortalInfo(Vector3d.copyCenteredWithVerticalOffset(portalMiddlePos, 0.25), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
	}
	
	private static boolean validatePortal(World world, BlockPos pos) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (world.getBlockState(pos.add(i, 0, j)).getBlock() != OverworldMirrorBlocks.PORTAL.get()) {
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
		
		portal.forEach(portalPos -> world.setBlockState(portalPos, OverworldMirrorBlocks.PORTAL.get().getDefaultState(), BlockFlags.BLOCK_UPDATE));
		
		return pos;
	}
	
	public static PortalWorldSavedData getSavedData(ServerWorld world) {
		final String name = "overworldmirror_portal";
		return WorldUtil.getSaveData(world, name, () -> new PortalWorldSavedData(name));
	}
	
	public static float getPlaneDistanceSq(int x1, int z1, int x2, int z2) {
		final int xDiff = x2 - x1;
		final int zDiff = z2 - z1;
		return xDiff * xDiff + zDiff * zDiff;
	}
}
