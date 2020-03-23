package info.u_team.overworld_mirror.portal;

import info.u_team.u_team_core.util.world.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

public class DimensionTeleportManager {
	
	public static void teleportEntity(Entity entity, DimensionType type) {
		if (!(entity.getEntityWorld() instanceof ServerWorld)) {
			return;
		}
		
		final ServerWorld originWorld = (ServerWorld) entity.getEntityWorld();
		final ServerWorld destinationWorld = WorldUtil.getServerWorld(entity, type);
		
		PortalManager.trySummonEntityInPortal(destinationWorld, entity, entity.rotationYaw);
		
		final double movementFactor = originWorld.getDimension().getMovementFactor() / destinationWorld.getDimension().getMovementFactor();
		double newX = entity.posX * movementFactor;
		double newZ = entity.posZ * movementFactor;
		
		double worldBorderMinX = Math.min(-2.9999872E7D, destinationWorld.getWorldBorder().minX() + 16.0D);
		double worldBorderMinZ = Math.min(-2.9999872E7D, destinationWorld.getWorldBorder().minZ() + 16.0D);
		double worldBorderMaxX = Math.min(2.9999872E7D, destinationWorld.getWorldBorder().maxX() - 16.0D);
		double worldBorderMaxZ = Math.min(2.9999872E7D, destinationWorld.getWorldBorder().maxZ() - 16.0D);
		newX = MathHelper.clamp(newX, worldBorderMinX, worldBorderMaxX);
		newZ = MathHelper.clamp(newZ, worldBorderMinZ, worldBorderMaxZ);
		
		WorldUtil.teleportEntity(entity, type, new BlockPos(newX, entity.posY, newZ));
	}
	
}
