package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorReference;
import info.u_team.overworld_mirror.config.FabricCommonConfig;
import info.u_team.overworld_mirror.event.WorldInfoReplaceEventHandler;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

@Construct(modid = OverworldMirrorReference.MODID)
public class OverworldMirrorFabricCommonConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		FabricCommonConfig.getInstance();
		
		ServerWorldEvents.LOAD.register((server, level) -> {
			WorldInfoReplaceEventHandler.onWorldLoad(level);
		});
		ServerTickEvents.END_WORLD_TICK.register(level -> {
			WorldInfoReplaceEventHandler.onWorldTick(level);
		});
	}
	
}
