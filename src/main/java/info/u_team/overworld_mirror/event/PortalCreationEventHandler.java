package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.portal.PortalManager;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
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
		
		if (!event.getPlayer().isShiftKeyDown()) {
			return;
		}
		
		if (PortalManager.trySpawnPortalFromFrame(world, pos)) {
			final LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(world);
			lightning.moveTo(Vec3.atBottomCenterOf(pos.above()));
			lightning.setVisualOnly(true);
			
			if (event.getPlayer() instanceof ServerPlayer) {
				lightning.setCause((ServerPlayer) event.getPlayer());
			}
			
			world.addFreshEntity(lightning);
		}
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(PortalCreationEventHandler::onBonemeal);
	}
}
