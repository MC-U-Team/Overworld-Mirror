package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.dimension.OverworldMirrorDimension;
import info.u_team.u_team_core.util.registry.CommonDeferredRegister;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class OverworldMirrorModDimensions {
	
	public static final CommonDeferredRegister<ModDimension> MOD_DIMENSIONS = CommonDeferredRegister.create(ForgeRegistries.MOD_DIMENSIONS, OverworldMirrorMod.MODID);
	
	public static final RegistryObject<ModDimension> DIMENSION = MOD_DIMENSIONS.register("dimension", () -> ModDimension.withFactory(OverworldMirrorDimension::new));
	
	public static void register(IEventBus bus) {
		MOD_DIMENSIONS.register(bus);
	}
	
}
