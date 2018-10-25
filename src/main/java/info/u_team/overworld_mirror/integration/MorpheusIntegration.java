package info.u_team.overworld_mirror.integration;

import info.u_team.overworld_mirror.init.OverworldMirrorDimensions;
import net.minecraftforge.fml.common.Optional;
import net.quetzi.morpheus.Morpheus;

public class MorpheusIntegration {
	
	@Optional.Method(modid = "morpheus")
	public static void init() {
		Morpheus.register.registerHandler(new MorpheusDayHandler(), OverworldMirrorDimensions.dimension_id);
	}
	
}
