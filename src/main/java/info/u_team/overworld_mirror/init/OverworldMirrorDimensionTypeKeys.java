package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.level.dimension.DimensionType;

public class OverworldMirrorDimensionTypeKeys {
	
	public static final ResourceKey<DimensionType> MIRROR_OVERWORLD = ResourceKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, new ResourceLocation(OverworldMirrorMod.MODID, "overworld_mirror"));
	
}
