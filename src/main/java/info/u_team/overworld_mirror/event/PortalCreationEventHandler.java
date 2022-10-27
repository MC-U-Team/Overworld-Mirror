package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.portal.PortalManager;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class PortalCreationEventHandler {
	
	private static void onBonemeal(BonemealEvent event) {
		final BlockPos pos = event.getPos();
		
		if (!(event.getWorld() instanceof ServerLevel)) {
			return;
		}
		
		final ServerLevel world = (ServerLevel) event.getWorld();
		
		if (!(event.getBlock().getBlock() instanceof FlowerBlock)) {
			return;
		}
		
		if (!event.getPlayer().isSneaking()) {
			return;
		}
		
		if (PortalManager.trySpawnPortalFromFrame(world, pos)) {
			final LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
			lightning.moveForced(Vector3d.copyCenteredHorizontally(pos.up()));
			lightning.setEffectOnly(true);
			
			if (event.getPlayer() instanceof ServerPlayerEntity) {
				lightning.setCaster((ServerPlayerEntity) event.getPlayer());
			}
			
			world.addEntity(lightning);
		}
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(PortalCreationEventHandler::onBonemeal);
	}
}
