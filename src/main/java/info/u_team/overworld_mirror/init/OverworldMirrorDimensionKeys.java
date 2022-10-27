package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.dimension.LevelStem;

public class OverworldMirrorDimensionKeys {
	
	public static final ResourceKey<LevelStem> MIRROR_OVERWORLD = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation(OverworldMirrorMod.MODID, "overworld"));
	
}
