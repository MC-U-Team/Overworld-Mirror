package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.portal.PortalManager;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
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
			final LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
			lightning.moveForced(Vector3d.copyCenteredHorizontally(pos.up()));
			lightning.setEffectOnly(true);
			
			if (event.getPlayer() instanceof ServerPlayerEntity) {
				lightning.setCaster((ServerPlayerEntity) event.getPlayer());
			}
			
			world.addEntity(lightning);
		}
	}
}
