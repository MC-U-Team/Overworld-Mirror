package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.block.OverworldMirrorPortalBlock;
import info.u_team.u_team_core.util.registry.BlockDeferredRegister;
import info.u_team.u_team_core.util.registry.BlockRegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.eventbus.api.IEventBus;

public class OverworldMirrorBlocks {
	
	public static final BlockDeferredRegister BLOCKS = BlockDeferredRegister.create(OverworldMirrorMod.MODID);
	
	public static final BlockRegistryObject<OverworldMirrorPortalBlock, BlockItem> PORTAL = BLOCKS.register("portal", OverworldMirrorPortalBlock::new);
	
	public static void registerMod(IEventBus bus) {
		BLOCKS.register(bus);
	}
	
}
