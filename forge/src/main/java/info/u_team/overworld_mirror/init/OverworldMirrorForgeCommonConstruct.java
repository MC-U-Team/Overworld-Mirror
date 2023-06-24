package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.OverworldMirrorMod;
import info.u_team.overworld_mirror.config.ForgeCommonConfig;
import info.u_team.overworld_mirror.event.PortalCreationEventHandler;
import info.u_team.overworld_mirror.event.WorldInfoReplaceEventHandler;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.u_team_core.util.registry.BusRegister;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

@Construct(modid = OverworldMirrorMod.MODID)
public class OverworldMirrorForgeCommonConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		ModLoadingContext.get().registerConfig(Type.COMMON, ForgeCommonConfig.CONFIG);
		
		BusRegister.registerForge(bus -> {
			bus.addListener(EventPriority.NORMAL, false, BonemealEvent.class, event -> {
				if (event.getLevel() instanceof final ServerLevel level && event.getEntity() instanceof ServerPlayer player) {
					PortalCreationEventHandler.onBonemeal(event.getPos(), level, player);
				}
			});
			
			bus.addListener(EventPriority.NORMAL, false, LevelEvent.Load.class, event -> {
				if (event.getLevel() instanceof final ServerLevel level) {
					WorldInfoReplaceEventHandler.onWorldLoad(level);
				}
			});
			bus.addListener(EventPriority.NORMAL, false, LevelTickEvent.class, event -> {
				if (event.phase == Phase.END && event.level instanceof final ServerLevel level) {
					WorldInfoReplaceEventHandler.onWorldTick(level);
				}
			});
		});
	}
	
}
