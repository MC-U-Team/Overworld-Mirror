package info.u_team.overworld_mirror.impl;

import info.u_team.overworld_mirror.portal.PortalTeleporter;
import info.u_team.overworld_mirror.util.TeleportUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class ForgeTeleportUtil extends TeleportUtil {
	
	private final PortalTeleporter teleporter = new PortalTeleporter();
	
	@Override
	public Entity teleport(Entity entity, ServerLevel destination) {
		return entity.changeDimension(destination, teleporter);
	}
	
}
