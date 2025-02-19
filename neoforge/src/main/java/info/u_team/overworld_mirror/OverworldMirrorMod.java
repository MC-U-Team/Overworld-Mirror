package info.u_team.overworld_mirror;

import info.u_team.u_team_core.util.annotation.AnnotationManager;
import net.neoforged.fml.common.Mod;

@Mod(OverworldMirrorMod.MODID)
public class OverworldMirrorMod {
	
	public static final String MODID = OverworldMirrorReference.MODID;
	
	public OverworldMirrorMod() {
		AnnotationManager.callAnnotations(MODID);
	}
	
}