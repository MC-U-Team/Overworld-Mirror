package info.u_team.overworldmirror.event;

import info.u_team.overworldmirror.portal.PortalValidator;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerPortalCreation {
	
	@SubscribeEvent
	public void onBoneMeal(BonemealEvent event) {
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		if (!(event.getBlock().getBlock() instanceof BlockFlower)) {
			return;
		}
		if (new PortalValidator(world, pos).create()) {
			EntityLightningBolt lightning = new EntityLightningBolt(world, pos.getX(), pos.getY() + 1, pos.getZ(), true);
			world.spawnEntity(lightning);
		}
	}
	
}
