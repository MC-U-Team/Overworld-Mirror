package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;

public class OverworldMirrorDimensionKeys {
	
	public static final RegistryKey<Dimension> MIRROR_OVERWORLD = RegistryKey.getOrCreateKey(Registry.DIMENSION_KEY, new ResourceLocation(OverworldMirrorMod.MODID, "overworld"));
	
}
