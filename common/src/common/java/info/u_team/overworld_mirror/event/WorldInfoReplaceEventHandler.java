package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.init.OverworldMirrorLevelKeys;
import info.u_team.overworld_mirror.level.CustomTimeLevelInfo;
import net.minecraft.server.level.ServerLevel;

public class WorldInfoReplaceEventHandler {
	
	public static void onWorldLoad(ServerLevel level) {
		if (level.dimension() != OverworldMirrorLevelKeys.MIRROR_OVERWORLD) {
			return;
		}
		final CustomTimeLevelInfo worldInfo = new CustomTimeLevelInfo(level.serverLevelData);
		level.serverLevelData = worldInfo;
		level.levelData = worldInfo;
	}
	
	public static void onWorldTick(ServerLevel level) {
		if (!(level.serverLevelData instanceof final CustomTimeLevelInfo customTimeLevelData)) {
			return;
		}
		customTimeLevelData.tick(level);
	}
}
