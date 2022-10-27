package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.init.OverworldMirrorWorldKeys;
import info.u_team.overworld_mirror.world.CustomTimeWorldInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class WorldInfoReplaceEventHandler {
	
	private static void onWorldLoad(WorldEvent.Load event) {
		if (!(event.getWorld() instanceof ServerLevel)) {
			return;
		}
		final ServerLevel world = (ServerLevel) event.getWorld();
		if (world.dimension() != OverworldMirrorWorldKeys.MIRROR_OVERWORLD) {
			return;
		}
		final CustomTimeWorldInfo worldInfo = new CustomTimeWorldInfo(world.serverLevelData);
		world.serverLevelData = worldInfo;
		world.levelData = worldInfo;
	}
	
	private static void onWorldTick(WorldTickEvent event) {
		if (event.phase != Phase.END) {
			return;
		}
		if (!(event.world instanceof ServerLevel)) {
			return;
		}
		final ServerLevel world = (ServerLevel) event.world;
		if (!(world.serverLevelData instanceof CustomTimeWorldInfo)) {
			return;
		}
		((CustomTimeWorldInfo) world.serverLevelData).tick(world);
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(WorldInfoReplaceEventHandler::onWorldLoad);
		bus.addListener(WorldInfoReplaceEventHandler::onWorldTick);
	}
	
}
