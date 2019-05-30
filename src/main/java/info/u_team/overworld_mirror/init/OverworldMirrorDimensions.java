package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.dimension.DimensionOverworldMirror;
import info.u_team.u_team_core.dimension.UModDimension;
import info.u_team.u_team_core.registry.DimensionRegistry;
import info.u_team.u_team_core.registry.util.CommonRegistry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.*;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OverworldMirrorDimensions {
	
	public static final ModDimension dimension = new UModDimension("mirrored_overworld", DimensionOverworldMirror::new);
	
	/**
	 * Might be null if dimension is not registered in the world
	 */
	public static DimensionType dimension_type;
	
	public static void construct() {
		DimensionRegistry.register(OverworldMirrorMod.modid, OverworldMirrorDimensions.class);
		CommonRegistry.registerEventHandler(OverworldMirrorDimensions.class);
	}
	
	@SubscribeEvent
	public static void on(final RegisterDimensionsEvent event) {
		dimension_type = DimensionManager.registerDimension(dimension.getRegistryName(), dimension, null);
	}
	
}
