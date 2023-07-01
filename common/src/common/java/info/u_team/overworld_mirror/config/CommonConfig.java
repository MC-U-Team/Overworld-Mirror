package info.u_team.overworld_mirror.config;

import info.u_team.u_team_core.util.ConfigValueHolder;
import info.u_team.u_team_core.util.ServiceUtil;

public abstract class CommonConfig {
	
	private static final CommonConfig INSTANCE = ServiceUtil.loadOne(CommonConfig.class);
	
	public static CommonConfig getInstance() {
		return INSTANCE;
	}
	
	public abstract ConfigValueHolder<Double> portalSearchDistanceOverworld();
	
	public abstract ConfigValueHolder<Double> portalSearchDistanceOverworldMirror();
	
}
