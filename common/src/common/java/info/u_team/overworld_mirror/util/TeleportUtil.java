package info.u_team.overworld_mirror.util;

import info.u_team.u_team_core.util.ServiceUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public abstract class TeleportUtil {
	
	private static final TeleportUtil INSTANCE = ServiceUtil.loadOne(TeleportUtil.class);
	
	public static Entity changeDimension(Entity entity, ServerLevel destination) {
		return INSTANCE.teleport(entity, destination);
	}
	
	public abstract Entity teleport(Entity entity, ServerLevel destination);
	
}
