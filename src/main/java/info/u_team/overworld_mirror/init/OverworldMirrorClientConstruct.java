package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.u_team_core.util.registry.BusRegister;

@Construct(modid = OverworldMirrorMod.MODID, client = true)
public class OverworldMirrorClientConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		BusRegister.registerMod(OverworldMirrorRenderTypes::registerMod);
	}
	
}
