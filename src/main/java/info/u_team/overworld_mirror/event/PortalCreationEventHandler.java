package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.portal.PortalManager;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = OverworldMirrorMod.MODID)
public class PortalCreationEventHandler {
	
	@SubscribeEvent
	public static void on(BonemealEvent event) {
		final BlockPos pos = event.getPos();
		
		if (!(event.getWorld() instanceof ServerWorld)) {
			return;
		}
		
		final ServerWorld world = (ServerWorld) event.getWorld();
		
		if (!(event.getBlock().getBlock() instanceof FlowerBlock)) {
			return;
		}
		
		if (!event.getPlayer().isSneaking()) {
			return;
		}
		
		if (PortalManager.trySpawnPortalFromFrame(world, pos)) {
			LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), true);
			world.addLightningBolt(lightning);
		}
	}
}
