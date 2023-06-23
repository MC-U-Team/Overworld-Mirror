package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorReference;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;

@Construct(modid = OverworldMirrorReference.MODID)
public class OverworldMirrorCommonConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		OverworldMirrorBlocks.register();
	}
	
}
