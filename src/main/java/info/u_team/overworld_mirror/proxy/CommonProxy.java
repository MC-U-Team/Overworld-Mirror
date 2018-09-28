package info.u_team.overworld_mirror.proxy;

import info.u_team.overworld_mirror.event.*;
import info.u_team.overworld_mirror.init.*;
import info.u_team.u_team_core.registry.CommonRegistry;
import net.minecraftforge.fml.common.event.*;

public class CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
		OverworldMirrorBlocks.preinit();
		OverworldMirrorDimensions.preinit();
	}
	
	public void init(FMLInitializationEvent event) {
		CommonRegistry.registerEventHandler(new EventHandlerPortalCreation());
	}
	
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
