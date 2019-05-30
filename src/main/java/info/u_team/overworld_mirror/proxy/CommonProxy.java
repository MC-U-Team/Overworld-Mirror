package info.u_team.overworld_mirror.proxy;

import info.u_team.overworld_mirror.event.EventHandlerPortalCreation;
import info.u_team.overworld_mirror.init.*;
import info.u_team.u_team_core.api.IModProxy;
import info.u_team.u_team_core.registry.util.CommonRegistry;

public class CommonProxy implements IModProxy {
	
	@Override
	public void construct() {
		OverworldMirrorBlocks.construct();
		OverworldMirrorDimensions.construct();
	}
	
	@Override
	public void setup() {
		CommonRegistry.registerEventHandler(EventHandlerPortalCreation.class);
	}
	
	@Override
	public void complete() {
	}
	
}
