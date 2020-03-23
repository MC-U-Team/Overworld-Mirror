package info.u_team.overworld_mirror;

import info.u_team.overworld_mirror.config.ServerConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(OverworldMirrorMod.MODID)
public class OverworldMirrorMod {
	
	public static final String MODID = "overworldmirror";
	
	public OverworldMirrorMod() {
		ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.CONFIG);
	}
	
}