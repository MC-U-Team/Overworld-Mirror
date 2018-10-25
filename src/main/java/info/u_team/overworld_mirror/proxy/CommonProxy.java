package info.u_team.overworld_mirror.proxy;

import info.u_team.overworld_mirror.event.EventHandlerPortalCreation;
import info.u_team.overworld_mirror.init.*;
import info.u_team.overworld_mirror.integration.MorpheusIntegration;
import info.u_team.u_team_core.registry.CommonRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.*;

public class CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
		OverworldMirrorBlocks.preinit();
		OverworldMirrorDimensions.preinit();
	}
	
	public void init(FMLInitializationEvent event) {
		CommonRegistry.registerEventHandler(EventHandlerPortalCreation.class);
		if (Loader.isModLoaded("morpheus")) {
			MorpheusIntegration.init();
		}
	}
	
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
