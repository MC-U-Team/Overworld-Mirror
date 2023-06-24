package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorReference;
import info.u_team.overworld_mirror.config.FabricCommonConfig;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;

@Construct(modid = OverworldMirrorReference.MODID)
public class OverworldMirrorFabricCommonConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		FabricCommonConfig.getInstance();
		
		
	}
	
}
