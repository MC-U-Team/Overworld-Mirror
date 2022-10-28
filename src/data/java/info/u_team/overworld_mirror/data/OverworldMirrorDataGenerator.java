package info.u_team.overworld_mirror.data;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.data.provider.OverworldMirrorBlockStateProvider;
import info.u_team.overworld_mirror.data.provider.OverworldMirrorLanguagesProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = OverworldMirrorMod.MODID, bus = Bus.MOD)
public class OverworldMirrorDataGenerator {
	
	@SubscribeEvent
	public static void data(GatherDataEvent event) {
		final GenerationData data = new GenerationData(OverworldMirrorMod.MODID, event);
		
		data.addProvider(event.includeClient(), OverworldMirrorBlockStateProvider::new);
		data.addProvider(event.includeClient(), OverworldMirrorLanguagesProvider::new);
	}
	
}
