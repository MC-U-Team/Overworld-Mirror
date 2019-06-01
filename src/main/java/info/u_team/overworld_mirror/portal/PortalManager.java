package info.u_team.overworld_mirror.portal;

import java.util.*;

import info.u_team.overworld_mirror.config.CommonConfig;
import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PortalManager {
	
	public static boolean trySpawnPortalFromFrame(World world, BlockPos pos) {
		
		int west_count = 0;
		while (world.getBlockState(pos.west(west_count + 1)).getBlock() != Blocks.STONEBRICK && west_count < 3) {
			west_count++;
		}
		
		BlockPos west_pos = pos.west(west_count);
		
		int north_count = 0;
		while (world.getBlockState(west_pos.north(north_count + 1)).getBlock() != Blocks.STONEBRICK && north_count < 3) {
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
		boolean frame_ok = frame.stream().allMatch(frame_pos -> world.getBlockState(frame_pos).getBlock() == Blocks.STONEBRICK);
		
		if (flowers_ok && frame_ok) {
			flowers.forEach(portal_pos -> world.setBlockState(portal_pos, OverworldMirrorBlocks.portal.getDefaultState(), 2));
			
			PlayerList playerlist = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
			flowers.forEach(portal_pos -> playerlist.sendToAllNearExcept(null, portal_pos.getX(), portal_pos.getY(), portal_pos.getZ(), 64, world.provider.getDimension(), new SPacketBlockChange(world, portal_pos)));
			
			return true;
		}
		return false;
	}
	
	public static void trySummonEntityInPortal(World world, Entity entity, float yaw) {
		BlockPos entity_pos = entity.getPosition();
		
		WorldSaveDataPortal data = getSaveData(world);
		
		BlockPos middle_pos = null;
		
		ListIterator<BlockPos> iterator = data.getPortals().listIterator();
		while (iterator.hasNext()) {
			BlockPos pos = iterator.next();
			if (distanceSq(pos.getX(), pos.getZ(), entity_pos.getX(), entity_pos.getZ()) < CommonConfig.settings.portal_distance) {
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
		BlockPos pos = world.getPrecipitationHeight(entity_pos).down();
		
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
			world.setBlockState(frame_pos, Blocks.STONEBRICK.getDefaultState());
			world.setBlockToAir(frame_pos.up());
			world.setBlockToAir(frame_pos.up(2));
		});
		portal.forEach(portal_pos -> {
			world.setBlockState(portal_pos.down(), Blocks.STONEBRICK.getDefaultState());
			world.setBlockState(portal_pos, OverworldMirrorBlocks.portal.getDefaultState(), 2);
		});
		
		return pos;
	}
	
	public static WorldSaveDataPortal getSaveData(World world) {
		MapStorage storage = world.getPerWorldStorage();
		WorldSaveDataPortal instance = (WorldSaveDataPortal) storage.getOrLoadData(WorldSaveDataPortal.class, "overworldmirror_portal");
		
		if (instance == null) {
			instance = new WorldSaveDataPortal("overworldmirror_portal");
			storage.setData("overworldmirror_portal", instance);
		}
		return instance;
	}
	
	public static double distanceSq(double from_x, double from_z, double to_x, double to_z) {
		double x = from_x - to_x;
		double z = from_z - to_z;
		return x * x + z * z;
	}
}
