package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.block.BlockOverworldMirrorPortal;
import info.u_team.u_team_core.registry.BlockRegistry;

public class OverworldMirrorBlocks {
	
	public static final BlockOverworldMirrorPortal portal = new BlockOverworldMirrorPortal("portal");
	
	public static void construct() {
		BlockRegistry.register(OverworldMirrorMod.modid, OverworldMirrorBlocks.class);
	}
	
}
