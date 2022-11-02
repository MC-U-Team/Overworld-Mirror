package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class OverworldMirrorLevelKeys {
	
	public static final ResourceKey<Level> MIRROR_OVERWORLD = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(OverworldMirrorMod.MODID, "overworld"));
	
}
