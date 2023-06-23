package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorReference;
import info.u_team.overworld_mirror.block.OverworldMirrorPortalBlock;
import info.u_team.u_team_core.api.registry.BlockRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;

public class OverworldMirrorBlocks {
	
	public static final BlockRegister BLOCKS = BlockRegister.create(OverworldMirrorReference.MODID);
	
	public static final RegistryEntry<OverworldMirrorPortalBlock> PORTAL = BLOCKS.registerBlock("portal", OverworldMirrorPortalBlock::new);
	
	static void register() {
		BLOCKS.register();
	}
	
}
