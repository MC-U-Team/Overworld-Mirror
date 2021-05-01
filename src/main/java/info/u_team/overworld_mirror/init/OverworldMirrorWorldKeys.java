package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class OverworldMirrorWorldKeys {
	
	public static final RegistryKey<World> MIRROR_OVERWORLD = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(OverworldMirrorMod.MODID, "overworld"));
	
}
