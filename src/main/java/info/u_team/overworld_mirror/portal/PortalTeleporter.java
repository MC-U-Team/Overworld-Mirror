package info.u_team.overworld_mirror.portal;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

public class PortalTeleporter implements ITeleporter {
	
	@Override
	public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destinationWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		PortalManager.trySummonEntityInPortal(currentWorld, destinationWorld, entity, yaw);
		return repositionEntity.apply(false);
	}
}
