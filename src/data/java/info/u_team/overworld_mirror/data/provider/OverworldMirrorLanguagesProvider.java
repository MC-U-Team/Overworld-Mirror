package info.u_team.overworld_mirror.data.provider;

import info.u_team.u_team_core.data.*;

import static info.u_team.overworld_mirror.init.OverworldMirrorBlocks.*;

public class OverworldMirrorLanguagesProvider extends CommonLanguagesProvider {
	
	public OverworldMirrorLanguagesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	public void addTranslations() {
		// English
		add(PORTAL.get(), "Overworld Mirror Portal");
		
		// German
		add("de_de", PORTAL.get(), "Oberwelt Spiegel Portal");
	}
	
}
