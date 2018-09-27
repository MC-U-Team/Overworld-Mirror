package info.u_team.overworldmirror.proxy;

import info.u_team.overworldmirror.event.EventHandlerConfigChange;
import info.u_team.u_team_core.registry.CommonRegistry;
import net.minecraftforge.fml.common.event.*;

public class ClientProxy extends CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
		super.preinit(event);
	}
	
	public void init(FMLInitializationEvent event) {
		super.init(event);
		CommonRegistry.registerEventHandler(EventHandlerConfigChange.class);
	}
	
	public void postinit(FMLPostInitializationEvent event) {
		super.postinit(event);
	}
	
}
