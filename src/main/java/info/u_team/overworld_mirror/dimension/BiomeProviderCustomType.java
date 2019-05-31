package info.u_team.overworld_mirror.dimension;

import net.minecraft.world.biome.provider.*;

public class BiomeProviderCustomType {
	
	public static final BiomeProviderType<OverworldBiomeProviderSettings, MirroredOverworldBiomeProvider> MIRRORED_VANILLA_LAYERED = BiomeProviderType.register("mirrored_overworld_layered", MirroredOverworldBiomeProvider::new, OverworldBiomeProviderSettings::new);
	
}
