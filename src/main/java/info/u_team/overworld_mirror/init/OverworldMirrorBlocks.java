package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorConstants;
import info.u_team.overworld_mirror.block.BlockOverworldMirrorPortal;
import info.u_team.u_team_core.registry.BlockRegistry;
import info.u_team.u_team_core.util.RegistryUtil;
import net.minecraft.block.Block;

public class OverworldMirrorBlocks {
	
	public static final BlockOverworldMirrorPortal portal = new BlockOverworldMirrorPortal("portal");
	
	public static void preinit() {
		BlockRegistry.register(OverworldMirrorConstants.MODID, RegistryUtil.getRegistryEntries(Block.class, OverworldMirrorBlocks.class));
	}
	
}
