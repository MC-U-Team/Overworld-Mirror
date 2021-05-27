package info.u_team.overworld_mirror.event;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class WorldInfoReplaceEventHandler {
	
	private static void onWorldLoad(WorldEvent.Load event) {
		
	}
	
	private static void onWorldUnload(WorldEvent.Unload event) {
		
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(WorldInfoReplaceEventHandler::onWorldLoad);
		bus.addListener(WorldInfoReplaceEventHandler::onWorldUnload);
	}
	
}
