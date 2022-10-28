package info.u_team.overworld_mirror.data.provider;

import static info.u_team.overworld_mirror.init.OverworldMirrorBlocks.PORTAL;

import info.u_team.u_team_core.data.CommonBlockStateProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraft.core.Direction;

public class OverworldMirrorBlockStateProvider extends CommonBlockStateProvider {
	
	public OverworldMirrorBlockStateProvider(GenerationData generationData) {
		super(generationData);
	}

	@Override
	public void register() {
		simpleBlock(PORTAL.get(), models() //
				.getBuilder(getPath(PORTAL.get())) //
				.texture("particle", models().modLoc("block/" + getPath(PORTAL.get()))) //
				.texture("portal", models().modLoc("block/" + getPath(PORTAL.get()))) //
				.element() //
				.from(0, 12, 0) //
				.to(16, 12, 16) //
				.face(Direction.UP) //
				.uvs(0, 0, 16, 16) //
				.texture("#portal") //
				.end() //
				.end() //
		);
	}
	
}
