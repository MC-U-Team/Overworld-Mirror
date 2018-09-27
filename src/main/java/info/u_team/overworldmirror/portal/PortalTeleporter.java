package info.u_team.overworldmirror.portal;

import info.u_team.overworldmirror.init.OverworldMirrorBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class PortalTeleporter implements ITeleporter {
	
	@Override
	public void placeEntity(World world, Entity entity, float yaw) {
		if (entity instanceof EntityPlayerMP) {
			teleport(world, entity, yaw);
		}
	}
	
	private void teleport(World world, Entity entity, float yaw) {
		BlockPos pos = world.getPrecipitationHeight(entity.getPosition());
		for (int i = -10; i <= 10; i++) {
			for (int j = -10; j <= 10; j++) {
				for (int k = -3; k <= 3; k++) {
					BlockPos temppos = pos.add(i, k, j);
					if (world.getBlockState(temppos).getBlock() == OverworldMirrorBlocks.portal) {
						setLocation(world, entity, temppos, yaw);
						return;
					}
				}
			}
		}
		new PortalCreator(world, pos).create();
		setLocation(world, entity, pos, yaw);
	}
	
	private void setLocation(World world, Entity entity, BlockPos pos, float yaw) {
		int i = 1;
		while (world.getBlockState(pos.north(i)).getBlock() == OverworldMirrorBlocks.portal) {
			i++;
		}
		pos = pos.north(i);
		entity.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, yaw + 180, entity.rotationPitch);
	}
	
}
