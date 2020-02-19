package info.u_team.overworld_mirror.proxy;

import info.u_team.overworld_mirror.config.CommonConfig;
import info.u_team.overworld_mirror.event.PortalCreationEventHandler;
import info.u_team.overworld_mirror.init.*;
import info.u_team.u_team_core.api.IModProxy;
import info.u_team.u_team_core.registry.util.CommonRegistry;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CommonProxy implements IModProxy {

	@Override
	public void construct() {
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.config);
		OverworldMirrorBlocks.construct();
		OverworldMirrorDimensions.construct();
		CommonRegistry.registerEventHandler(PortalCreationEventHandler.class);
	}

	@Override
	public void setup() {
	}

	@Override
	public void complete() {
	}

}
