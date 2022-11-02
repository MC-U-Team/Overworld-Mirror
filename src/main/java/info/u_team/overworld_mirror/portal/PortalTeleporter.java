package info.u_team.overworld_mirror.portal;

import java.util.function.Function;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraftforge.common.util.ITeleporter;

public class PortalTeleporter implements ITeleporter {
	
	@Override
	public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destinationWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		return repositionEntity.apply(false);
	}
	
	@Override
	public PortalInfo getPortalInfo(Entity entity, ServerLevel destinationWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		return PortalManager.findOrCreatePortal(destinationWorld, entity);
	}
}
