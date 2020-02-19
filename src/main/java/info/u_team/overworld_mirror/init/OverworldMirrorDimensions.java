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
	
	public static final ModDimension dimension = new UModDimension("dimension", DimensionOverworldMirror::new);
	
	public static void construct() {
		DimensionRegistry.register(OverworldMirrorMod.MODID, OverworldMirrorDimensions.class);
		CommonRegistry.registerEventHandler(OverworldMirrorDimensions.class);
	}
	
	@SubscribeEvent
	public static void on(final RegisterDimensionsEvent event) {
		DimensionType type = DimensionType.byName(dimension.getRegistryName());
		if (type == null) { // Check if dimension is already registered
			type = DimensionManager.registerDimension(dimension.getRegistryName(), dimension, null);
		}
		type.setRegistryName(dimension.getRegistryName()); // Fix missing registry name
	}
}
