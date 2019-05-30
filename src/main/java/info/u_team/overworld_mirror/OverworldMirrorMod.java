package info.u_team.overworld_mirror;

import info.u_team.overworld_mirror.proxy.*;
import info.u_team.u_team_core.api.IModProxy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OverworldMirrorMod.modid)
public class OverworldMirrorMod {
	
	public static final String modid = "overworldmirror";
	
	public static final IModProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	
	public OverworldMirrorMod() {
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		proxy.construct();
	}
	
	@SubscribeEvent
	public void setup(FMLCommonSetupEvent event) {
		proxy.setup();
	}
	
	@SubscribeEvent
	public void ready(FMLLoadCompleteEvent event) {
		proxy.complete();
	}
	
}