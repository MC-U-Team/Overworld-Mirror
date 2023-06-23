package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.portal.PortalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class PortalCreationEventHandler {
	
	private static void onBonemeal(BonemealEvent event) {
		final BlockPos pos = event.getPos();
		
		if (!(event.getLevel() instanceof final ServerLevel level) || !(event.getBlock().getBlock() instanceof FlowerBlock) || !event.getEntity().isShiftKeyDown()) {
			return;
		}
		
		if (PortalManager.trySpawnPortalFromFrame(level, pos)) {
			final LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
			lightning.moveTo(Vec3.atBottomCenterOf(pos.above()));
			lightning.setVisualOnly(true);
			
			if (event.getEntity() instanceof final ServerPlayer player) {
				lightning.setCause(player);
			}
			
			level.addFreshEntity(lightning);
		}
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(PortalCreationEventHandler::onBonemeal);
	}
}
