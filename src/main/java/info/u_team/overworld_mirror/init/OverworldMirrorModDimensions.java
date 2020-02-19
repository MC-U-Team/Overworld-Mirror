package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.dimension.OverworldMirrorDimension;
import info.u_team.u_team_core.dimension.UModDimension;
import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = OverworldMirrorMod.MODID, bus = Bus.MOD)
public class OverworldMirrorModDimensions {
	
	public static final ModDimension DIMENSION = new UModDimension("dimension", OverworldMirrorDimension::new);
	
	@SubscribeEvent
	public static void register(Register<ModDimension> event) {
		BaseRegistryUtil.getAllRegistryEntriesAndApplyNames(OverworldMirrorMod.MODID, ModDimension.class).forEach(event.getRegistry()::register);
	}
}
