package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.level.Level;

public class OverworldMirrorWorldKeys {
	
	public static final ResourceKey<Level> MIRROR_OVERWORLD = ResourceKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(OverworldMirrorMod.MODID, "overworld"));
	
}
