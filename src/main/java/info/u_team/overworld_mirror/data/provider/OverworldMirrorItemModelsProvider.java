package info.u_team.overworld_mirror.data.provider;

import static info.u_team.overworld_mirror.init.OverworldMirrorBlocks.*;

import info.u_team.u_team_core.data.*;

public class OverworldMirrorItemModelsProvider extends CommonItemModelsProvider {
	
	public OverworldMirrorItemModelsProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerModels() {
		withExistingParent(getPath(PORTAL), "block/cube_all") //
				.texture("all", modLoc("block/" + getPath(PORTAL)));
	}
	
}
