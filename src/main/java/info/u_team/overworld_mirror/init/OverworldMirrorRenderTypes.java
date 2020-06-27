package info.u_team.overworld_mirror.init;

import static info.u_team.overworld_mirror.init.OverworldMirrorBlocks.PORTAL;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraft.client.renderer.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = OverworldMirrorMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class OverworldMirrorRenderTypes {
	
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		// Translucent
		final RenderType transluctent = RenderType.getTranslucent();
		
		RenderTypeLookup.setRenderLayer(PORTAL.get(), transluctent);
	}
	
}
