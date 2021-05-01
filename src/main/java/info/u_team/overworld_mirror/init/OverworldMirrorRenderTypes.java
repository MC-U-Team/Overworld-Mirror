package info.u_team.overworld_mirror.init;

import static info.u_team.overworld_mirror.init.OverworldMirrorBlocks.PORTAL;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class OverworldMirrorRenderTypes {
	
	private static void setup(FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(PORTAL.get(), RenderType.getTranslucent());
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(OverworldMirrorRenderTypes::setup);
	}
	
}
