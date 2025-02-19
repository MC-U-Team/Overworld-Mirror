package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.config.NeoForgeCommonConfig;
import info.u_team.overworld_mirror.event.PortalCreationEventHandler;
import info.u_team.overworld_mirror.event.WorldInfoReplaceEventHandler;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.u_team_core.util.registry.BusRegister;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@Construct(modid = OverworldMirrorMod.MODID)
public class OverworldMirrorNeoForgeCommonConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		ModLoadingContext.get().getActiveContainer().registerConfig(Type.COMMON, NeoForgeCommonConfig.CONFIG);
		
		BusRegister.registerNeoForge(bus -> {
			bus.addListener(EventPriority.NORMAL, false, BonemealEvent.class, event -> {
				if (event.getLevel() instanceof final ServerLevel level && event.getPlayer() instanceof ServerPlayer player) {
					PortalCreationEventHandler.onBonemeal(event.getPos(), level, player);
				}
			});
			
			bus.addListener(EventPriority.NORMAL, false, LevelEvent.Load.class, event -> {
				if (event.getLevel() instanceof final ServerLevel level) {
					WorldInfoReplaceEventHandler.onWorldLoad(level);
				}
			});
			bus.addListener(EventPriority.NORMAL, false, LevelTickEvent.Post.class, event -> {
				if (event.getLevel() instanceof final ServerLevel level) {
					WorldInfoReplaceEventHandler.onWorldTick(level);
				}
			});
		});
	}
	
}
