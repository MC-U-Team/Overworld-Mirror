package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.config.ServerConfig;
import info.u_team.overworld_mirror.event.PortalCreationEventHandler;
import info.u_team.overworld_mirror.event.WorldInfoReplaceEventHandler;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.IModConstruct;
import info.u_team.u_team_core.util.registry.BusRegister;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

@Construct(modid = OverworldMirrorMod.MODID)
public class OverworldMirrorCommonConstruct implements IModConstruct {
	
	@Override
	public void construct() {
		ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.CONFIG);
		
		BusRegister.registerMod(OverworldMirrorBlocks::registerMod);
		
		BusRegister.registerForge(OverworldMirrorDimensionRegistry::registerForge);
		BusRegister.registerForge(PortalCreationEventHandler::registerForge);
		BusRegister.registerForge(WorldInfoReplaceEventHandler::registerForge);
	}
	
}
