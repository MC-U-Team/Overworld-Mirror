package info.u_team.overworld_mirror.impl;

import info.u_team.overworld_mirror.portal.PortalManager;
import info.u_team.overworld_mirror.util.TeleportUtil;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class FabricTeleportUtil extends TeleportUtil {
	
	@Override
	public Entity teleport(Entity entity, ServerLevel destination) {
		return FabricDimensions.teleport(entity, destination, PortalManager.findOrCreatePortal(destination, entity));
	}
	
}
