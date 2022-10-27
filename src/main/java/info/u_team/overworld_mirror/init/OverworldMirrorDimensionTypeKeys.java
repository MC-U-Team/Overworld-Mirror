package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.dimension.DimensionType;

public class OverworldMirrorDimensionTypeKeys {
	
	public static final ResourceKey<DimensionType> MIRROR_OVERWORLD = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(OverworldMirrorMod.MODID, "overworld_mirror"));
	
}
