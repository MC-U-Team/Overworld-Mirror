package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.level.dimension.LevelStem;

public class OverworldMirrorDimensionKeys {
	
	public static final ResourceKey<LevelStem> MIRROR_OVERWORLD = ResourceKey.getOrCreateKey(Registry.DIMENSION_KEY, new ResourceLocation(OverworldMirrorMod.MODID, "overworld"));
	
}
