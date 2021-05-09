package info.u_team.overworld_mirror.portal;

import java.util.function.Function;

import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

public class PortalTeleporter implements ITeleporter {
	
	@Override
	public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destinationWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		return repositionEntity.apply(false);
	}
	
	@Override
	public PortalInfo getPortalInfo(Entity entity, ServerWorld destinationWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
		return PortalManager.findOrCreatePortal(destinationWorld, entity);
	}
}
