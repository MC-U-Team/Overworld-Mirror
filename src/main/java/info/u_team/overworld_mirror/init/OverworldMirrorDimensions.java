package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = OverworldMirrorMod.MODID, bus = Bus.FORGE)
public class OverworldMirrorDimensions {
	
	@SubscribeEvent
	public static void on(RegisterDimensionsEvent event) {
		DimensionManager.registerOrGetDimension(OverworldMirrorModDimensions.DIMENSION.getRegistryName(), OverworldMirrorModDimensions.DIMENSION, null, true);
	}
}
