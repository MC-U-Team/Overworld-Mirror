package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.init.OverworldMirrorWorldKeys;
import info.u_team.overworld_mirror.world.CustomTimeWorldInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class WorldInfoReplaceEventHandler {
	
	private static void onWorldLoad(WorldEvent.Load event) {
		if (!(event.getWorld() instanceof ServerWorld)) {
			return;
		}
		final ServerWorld world = (ServerWorld) event.getWorld();
		if (world.getDimensionKey() != OverworldMirrorWorldKeys.MIRROR_OVERWORLD) {
			return;
		}
		final CustomTimeWorldInfo worldInfo = new CustomTimeWorldInfo(world.serverWorldInfo);
		world.serverWorldInfo = worldInfo;
		world.worldInfo = worldInfo;
	}
	
	private static void onWorldTick(WorldTickEvent event) {
		if (event.phase != Phase.END) {
			return;
		}
		if (!(event.world instanceof ServerWorld)) {
			return;
		}
		final ServerWorld world = (ServerWorld) event.world;
		if (!(world.serverWorldInfo instanceof CustomTimeWorldInfo)) {
			return;
		}
		((CustomTimeWorldInfo) world.serverWorldInfo).tick(world);
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(WorldInfoReplaceEventHandler::onWorldLoad);
		bus.addListener(WorldInfoReplaceEventHandler::onWorldTick);
	}
	
}
