package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.portal.*;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerPortalCreation {
	
	@SubscribeEvent
	public static void on(BonemealEvent event) {
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		
		if (world.isRemote) {
			return;
		}
		if (!(event.getBlock().getBlock() instanceof BlockBush)) {
			return;
		}
		
		if (new PortalValidator(world, pos).create()) {
			WorldSaveDataPortal data = WorldSaveDataPortal.get(world);
			data.getPortals().add(pos);
			data.markDirty();
			EntityLightningBolt lightning = new EntityLightningBolt(world, pos.getX(), pos.getY() + 1, pos.getZ(), true);
			world.addWeatherEffect(lightning);
		}
	}
	
}
