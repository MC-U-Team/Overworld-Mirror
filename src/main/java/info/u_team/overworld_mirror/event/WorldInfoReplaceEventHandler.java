package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.init.OverworldMirrorLevelKeys;
import info.u_team.overworld_mirror.level.CustomTimeLevelInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class WorldInfoReplaceEventHandler {
	
	private static void onWorldLoad(LevelEvent.Load event) {
		if (!(event.getLevel() instanceof ServerLevel level)) {
			return;
		}
		if (level.dimension() != OverworldMirrorLevelKeys.MIRROR_OVERWORLD) {
			return;
		}
		final CustomTimeLevelInfo worldInfo = new CustomTimeLevelInfo(level.serverLevelData);
		level.serverLevelData = worldInfo;
		level.levelData = worldInfo;
	}
	
	private static void onWorldTick(LevelTickEvent event) {
		if (event.phase != Phase.END) {
			return;
		}
		if (!(event.level instanceof ServerLevel level)) {
			return;
		}
		if (!(level.serverLevelData instanceof CustomTimeLevelInfo customTimeLevelData)) {
			return;
		}
		customTimeLevelData.tick(level);
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(WorldInfoReplaceEventHandler::onWorldLoad);
		bus.addListener(WorldInfoReplaceEventHandler::onWorldTick);
	}
	
}
