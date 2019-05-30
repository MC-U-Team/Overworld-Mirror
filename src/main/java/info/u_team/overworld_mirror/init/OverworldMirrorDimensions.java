package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.dimension.DimensionOverworldMirror;
import info.u_team.u_team_core.dimension.UModDimension;
import info.u_team.u_team_core.registry.DimensionRegistry;
import info.u_team.u_team_core.registry.util.CommonRegistry;
import net.minecraftforge.common.*;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OverworldMirrorDimensions {
	
	public static final ModDimension dimension = new UModDimension("dimension", DimensionOverworldMirror::new);
	
	public static void construct() {
		DimensionRegistry.register(OverworldMirrorMod.modid, OverworldMirrorDimensions.class);
		CommonRegistry.registerEventHandler(OverworldMirrorDimensions.class);
	}
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void on(final RegisterDimensionsEvent event) {
		if (!DimensionManager.getRegistry().containsKey(dimension.getRegistryName())) { // How do we know when the dimension needs to be registered??
			DimensionManager.registerDimension(dimension.getRegistryName(), dimension, null);
		}
	}
}
