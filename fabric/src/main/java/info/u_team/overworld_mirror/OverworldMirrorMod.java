package info.u_team.overworld_mirror;

import info.u_team.u_team_core.util.annotation.AnnotationManager;
import net.fabricmc.api.ModInitializer;

public class OverworldMirrorMod implements ModInitializer {
	
	public static final String MODID = OverworldMirrorReference.MODID;
	
	@Override
	public void onInitialize() {
		AnnotationManager.callAnnotations(MODID);
	}
}
