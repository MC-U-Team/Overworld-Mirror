package info.u_team.overworld_mirror.data.provider;

import static info.u_team.overworld_mirror.init.OverworldMirrorBlocks.*;

import info.u_team.u_team_core.data.*;
import net.minecraft.util.Direction;

public class OverworldMirrorBlockStatesProvider extends CommonBlockStatesProvider {
	
	public OverworldMirrorBlockStatesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerStatesAndModels() {
		simpleBlock(PORTAL, //
				getBuilder(getPath(PORTAL)) //
						.texture("particle", modLoc("block/" + getPath(PORTAL))) //
						.texture("portal", modLoc("block/" + getPath(PORTAL))) //
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
