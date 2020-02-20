package info.u_team.overworld_mirror.data;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.data.provider.*;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = OverworldMirrorMod.MODID, bus = Bus.MOD)
public class OverworldMirrorDataGenerator {
	
	@SubscribeEvent
	public static void data(GatherDataEvent event) {
		final GenerationData data = new GenerationData(OverworldMirrorMod.MODID, event);
		if (event.includeClient()) {
			data.addProvider(OverworldMirrorBlockStatesProvider::new);
			data.addProvider(OverworldMirrorItemModelsProvider::new);
			
			data.addProvider(OverworldMirrorLanguagesProvider::new);
		}
	}
	
}
