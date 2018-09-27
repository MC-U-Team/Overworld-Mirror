package info.u_team.overworldmirror.init;

import info.u_team.overworldmirror.OverworldMirrorConstants;
import info.u_team.overworldmirror.block.BlockOverworldMirrorPortal;
import info.u_team.u_team_core.registry.BlockRegistry;
import info.u_team.u_team_core.util.RegistryUtil;
import net.minecraft.block.Block;

public class OverworldMirrorBlocks {
	
	public static final BlockOverworldMirrorPortal portal = new BlockOverworldMirrorPortal("portal");
	
	public static void preinit() {
		BlockRegistry.register(OverworldMirrorConstants.MODID, RegistryUtil.getRegistryEntries(Block.class, OverworldMirrorBlocks.class));
	}
	
}
