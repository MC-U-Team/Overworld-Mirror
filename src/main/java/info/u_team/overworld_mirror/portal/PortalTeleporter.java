package info.u_team.overworld_mirror.portal;

import java.util.ListIterator;

import info.u_team.overworld_mirror.config.CommonConfig;
import info.u_team.overworld_mirror.init.OverworldMirrorBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class PortalTeleporter implements ITeleporter {
	
	private WorldSaveDataPortal data;
	
	@Override
	public void placeEntity(World world, Entity entity, float yaw) {
		
		data = WorldSaveDataPortal.get(world);
		
		BlockPos portal = null;
		
		PortalState state = PortalState.BROKEN;
		
		ListIterator<BlockPos> iterator = data.getPortals().listIterator();
		while (iterator.hasNext()) {
			BlockPos pos = iterator.next();
			if (distanceSq(pos.getX(), pos.getZ(), entity.posX, entity.posZ) < CommonConfig.settings.portal_distance) {
				state = validatePortal(world, pos);
				if (state == PortalState.BROKEN) {
					iterator.remove();
				} else {
					portal = pos;
					break;
				}
			}
		}
		
		if (state == PortalState.BROKEN) {
			BlockPos pos = world.getPrecipitationHeight(entity.getPosition()).down();
			createPortal(world, pos);
			data.getPortals().add(pos);
			portal = pos;
		}
		
		data.markDirty();
		
		if (portal == null) {
			System.out.println("ERROR. BIG WTF");
			return;
		}
		
		entity.setPositionAndRotation(portal.getX(), portal.getY() + 0, portal.getZ(), yaw, entity.rotationPitch);
	}
	
	private void createPortal(World world, BlockPos pos) {
		new PortalCreator(world, pos).create();
	}
	
	private PortalState validatePortal(World world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() == OverworldMirrorBlocks.portal && world.getBlockState(pos.east()).getBlock() == OverworldMirrorBlocks.portal && world.getBlockState(pos.east().down()).getBlock() == OverworldMirrorBlocks.portal && world.getBlockState(pos.down()).getBlock() == OverworldMirrorBlocks.portal) {
			return PortalState.OK;
		}
		return PortalState.BROKEN;
	}
	
	public enum PortalState {
		BROKEN,
		OK;
	}
	
	// private void teleport(World world, Entity entity, float yaw) {
	// BlockPos pos = world.getPrecipitationHeight(entity.getPosition());
	// for (int i = -10; i <= 10; i++) {
	// for (int j = -10; j <= 10; j++) {
	// for (int k = -3; k <= 3; k++) {
	// BlockPos temppos = pos.add(i, k, j);
	// if (world.getBlockState(temppos).getBlock() == OverworldMirrorBlocks.portal)
	// {
	// setLocation(world, entity, temppos, yaw);
	// return;
	// }
	// }
	// }
	// }
	// new PortalCreator(world, pos).create();
	// setLocation(world, entity, pos, yaw);
	// }
	//
	// private void setLocation(World world, Entity entity, BlockPos pos, float yaw)
	// {
	// int i = 1;
	// while (world.getBlockState(pos.north(i)).getBlock() ==
	// OverworldMirrorBlocks.portal) {
	// i++;
	// }
	// pos = pos.north(i);
	// entity.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() +
	// 0.5, yaw + 180, entity.rotationPitch);
	// }
	
	public double distanceSq(double fromX, double fromZ, double toX, double toZ) {
		double x = fromX - toX;
		double z = fromZ - toZ;
		return x * x + z * z;
	}
	
}
