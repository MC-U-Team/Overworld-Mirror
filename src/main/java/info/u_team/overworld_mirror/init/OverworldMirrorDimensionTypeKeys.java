package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;

public class OverworldMirrorDimensionTypeKeys {
	
	public static final RegistryKey<DimensionType> MIRROR_OVERWORLD = RegistryKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, new ResourceLocation(OverworldMirrorMod.MODID, "overworld_mirror"));
	
}
