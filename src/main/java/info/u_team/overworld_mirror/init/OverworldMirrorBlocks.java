package info.u_team.overworld_mirror.init;

import java.util.List;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.block.BlockOverworldMirrorPortal;
import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = OverworldMirrorMod.MODID, bus = Bus.MOD)
public class OverworldMirrorBlocks {
	
	public static final BlockOverworldMirrorPortal PORTAL = new BlockOverworldMirrorPortal("portal");
	
	@SubscribeEvent
	public static void register(Register<Block> event) {
		entries = BaseRegistryUtil.getAllRegistryEntriesAndApplyNames(OverworldMirrorMod.MODID, Block.class);
		entries.forEach(event.getRegistry()::register);
	}
	
	@SubscribeEvent
	public static void registerBlockItem(Register<Item> event) {
		BaseRegistryUtil.getBlockItems(entries).forEach(event.getRegistry()::register);
		entries = null; // Dereference list as it is no longer needed
	}
	
	// Just a cache for the block item registry for performance
	private static List<Block> entries;
	
}
