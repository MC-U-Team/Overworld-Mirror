package info.u_team.overworld_mirror;

import info.u_team.overworld_mirror.config.ServerConfig;
import info.u_team.overworld_mirror.init.*;
import info.u_team.u_team_core.util.registry.BusRegister;
import info.u_team.u_team_core.util.verify.JarSignVerifier;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(OverworldMirrorMod.MODID)
public class OverworldMirrorMod {
	
	public static final String MODID = "overworldmirror";
	
	public OverworldMirrorMod() {
		JarSignVerifier.checkSigned(MODID);
		ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.CONFIG);
		register();
	}
	
	private void register() {
		BusRegister.registerMod(OverworldMirrorBlocks::register);
	}
	
}