package info.u_team.overworld_mirror.portal;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class PortalTeleporter implements ITeleporter {
	
	@Override
	public void placeEntity(World world, Entity entity, float yaw) {
		PortalManager.trySummonEntityInPortal(world, entity, yaw);
	}
	
}
