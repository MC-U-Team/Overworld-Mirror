package info.u_team.overworldmirror.event;

import info.u_team.overworldmirror.OverworldMirrorConstants;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerConfigChange {
	
	@SubscribeEvent
	public static void on(OnConfigChangedEvent event) {
		if (event.getModID().equals(OverworldMirrorConstants.MODID)) {
			ConfigManager.sync(OverworldMirrorConstants.MODID, Type.INSTANCE);
		}
	}
}
