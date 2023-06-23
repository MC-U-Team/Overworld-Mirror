package info.u_team.overworld_mirror.data.provider;

import static info.u_team.overworld_mirror.init.OverworldMirrorBlocks.PORTAL;

import info.u_team.u_team_core.data.CommonLanguagesProvider;
import info.u_team.u_team_core.data.GenerationData;

public class OverworldMirrorLanguagesProvider extends CommonLanguagesProvider {
	
	public OverworldMirrorLanguagesProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register() {
		// English
		add(PORTAL.get(), "Overworld Mirror Portal");
		
		// German
		add("de_de", PORTAL.get(), "Oberwelt Spiegel Portal");
	}
	
}
