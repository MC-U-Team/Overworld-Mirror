package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.dimension.DimensionOverworldMirror;
import info.u_team.u_team_core.dimension.UModDimension;
import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = OverworldMirrorMod.MODID, bus = Bus.MOD)
public class OverworldMirrorDimensions {
	
	public static final ModDimension DIMENSION = new UModDimension("dimension", DimensionOverworldMirror::new);
	
	@SubscribeEvent
	public static void register(Register<ModDimension> event) {
		BaseRegistryUtil.getAllRegistryEntriesAndApplyNames(OverworldMirrorMod.MODID, ModDimension.class).forEach(event.getRegistry()::register);
	}
	
	// @SubscribeEvent
	// public static void on(final RegisterDimensionsEvent event) {
	// DimensionType type = DimensionType.byName(dimension.getRegistryName());
	// if (type == null) { // Check if dimension is already registered
	// type = DimensionManager.registerDimension(dimension.getRegistryName(), dimension, null);
	// }
	// type.setRegistryName(dimension.getRegistryName()); // Fix missing registry name
	// }
}
