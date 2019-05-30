package info.u_team.overworld_mirror.portal;

import java.util.*;

import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.storage.WorldSavedDataStorage;

public class PortalManager {
	
	public static boolean trySpawnPortalFromFrame(World world, BlockPos pos) {
		
		int west_count = 0;
		while (world.getBlockState(pos.west(west_count + 1)).getBlock() != Blocks.STONE_BRICKS && west_count < 3) {
			west_count++;
		}
		
		BlockPos west_pos = pos.west(west_count);
		
		int north_count = 0;
		while (world.getBlockState(west_pos.north(north_count + 1)).getBlock() != Blocks.STONE_BRICKS && north_count < 3) {
			north_count++;
		}
		
		BlockPos west_north_pos = west_pos.north(north_count);
		
		BlockPos middle_pos = west_north_pos.east().south();
		
		if (!validatePortalFrameAndSpawnPortal(world, middle_pos)) {
			return false;
		}
		
		WorldSaveDataPortal data = getSaveData(world);
		data.getPortals().add(middle_pos);
		data.markDirty();
		
		return true;
	}
	
	private static boolean validatePortalFrameAndSpawnPortal(World world, BlockPos pos) {
		ArrayList<BlockPos> flowers = new ArrayList<>();
		ArrayList<BlockPos> frame = new ArrayList<>();
		
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
		
		boolean flowers_ok = flowers.stream().allMatch(flower_pos -> world.getBlockState(flower_pos).getBlock() instanceof BlockFlower);
		boolean frame_ok = frame.stream().allMatch(frame_pos -> world.getBlockState(frame_pos).getBlock() == Blocks.STONE_BRICKS);
		
		if (flowers_ok && frame_ok) {
			flowers.forEach(portal_pos -> world.setBlockState(portal_pos, OverworldMirrorBlocks.portal.getDefaultState(), 2));
			
			PlayerList playerlist = world.getServer().getPlayerList();
			flowers.forEach(portal_pos -> playerlist.sendToAllNearExcept(null, portal_pos.getX(), portal_pos.getY(), portal_pos.getZ(), 64, world.getDimension().getType(), new SPacketBlockChange(world, portal_pos)));
			
			return true;
		}
		return false;
	}
	
	public static void trySummonEntityInPortal(World world, Entity entity, float yaw) {
		BlockPos entity_pos = entity.getPosition();
		
		WorldSaveDataPortal data = getSaveData(world);
		
		BlockPos middle_pos = null;
		
		Iterator<BlockPos> iterator = data.getPortals().iterator();
		while (iterator.hasNext()) {
			BlockPos pos = iterator.next();
			// TODO
			// if (distanceSq(pos.getX(), pos.getZ(), entity_pos.getX(), entity_pos.getZ()) < CommonConfig.settings.portal_distance)
			// {
			if (distanceSq(pos.getX(), pos.getZ(), entity_pos.getX(), entity_pos.getZ()) < 200) {
				if (validatePortal(world, pos)) {
					middle_pos = pos;
					break;
				} else {
					iterator.remove();
					data.markDirty();
				}
			}
		}
		if (middle_pos == null) {
			middle_pos = spawnPortal(world, entity_pos);
			data.getPortals().add(middle_pos);
			data.markDirty();
		}
		entity.setPositionAndRotation(middle_pos.getX() + 0.5D, middle_pos.getY(), middle_pos.getZ() + 0.5F, yaw, entity.rotationPitch);
		
	}
	
	private static boolean validatePortal(World world, BlockPos pos) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (world.getBlockState(pos.add(i, 0, j)).getBlock() != OverworldMirrorBlocks.portal) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static BlockPos spawnPortal(World world, BlockPos entity_pos) {
		final Chunk chunk = world.getChunk(entity_pos);
		chunk.getHeightmap(Type.WORLD_SURFACE).generate(); // Generate height map first, so we get accurate height
		
		final BlockPos pos = world.getHeight(Heightmap.Type.WORLD_SURFACE, entity_pos).down();
		
		ArrayList<BlockPos> portal = new ArrayList<>();
		ArrayList<BlockPos> frame = new ArrayList<>();
		
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
		
		frame.forEach(frame_pos -> {
			world.setBlockState(frame_pos, Blocks.STONE_BRICKS.getDefaultState());
			world.removeBlock(frame_pos.up());
			world.removeBlock(frame_pos.up(2));
		});
		portal.forEach(portal_pos -> {
			world.setBlockState(portal_pos.down(), Blocks.STONE_BRICKS.getDefaultState());
			world.setBlockState(portal_pos, OverworldMirrorBlocks.portal.getDefaultState(), 2);
		});
		
		return pos;
	}
	
	public static WorldSaveDataPortal getSaveData(World world) {
		DimensionType type = world.getDimension().getType();
		WorldSavedDataStorage storage = world.getSavedDataStorage();
		WorldSaveDataPortal instance = storage.get(type, WorldSaveDataPortal::new, "overworldmirror_portal");
		if (instance == null) {
			instance = new WorldSaveDataPortal("overworldmirror_portal");
			storage.set(type, "overworldmirror_portal", instance);
		}
		return instance;
	}
	
	public static double distanceSq(double from_x, double from_z, double to_x, double to_z) {
		double x = from_x - to_x;
		double z = from_z - to_z;
		return x * x + z * z;
	}
}
