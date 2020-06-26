package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.block.OverworldMirrorPortalBlock;
import info.u_team.u_team_core.util.registry.BlockDeferredRegister;
import net.minecraftforge.eventbus.api.IEventBus;

public class OverworldMirrorBlocks {
	
	public static final BlockDeferredRegister BLOCKS = BlockDeferredRegister.create(OverworldMirrorMod.MODID);
	
	public static final OverworldMirrorPortalBlock PORTAL = new OverworldMirrorPortalBlock("portal");
	
	public static void register(IEventBus bus) {
		BLOCKS.register(bus);
	}
	
}
