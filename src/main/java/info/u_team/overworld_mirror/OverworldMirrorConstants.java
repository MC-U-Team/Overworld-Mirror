package info.u_team.overworld_mirror;

import org.apache.logging.log4j.*;

public class OverworldMirrorConstants {
	
	public static final String MODID = "overworldmirror";
	public static final String NAME = "Overworld Mirror";
	public static final String VERSION = "${version}";
	public static final String MCVERSION = "${mcversion}";
	public static final String DEPENDENCIES = "required:forge@[14.23.5.2768,);required-after:uteamcore@[2.2.4.107,);after:morpheus";
	public static final String UPDATEURL = "https://api.u-team.info/update/overworldmirror.json";
	
	public static final String COMMONPROXY = "info.u_team.overworld_mirror.proxy.CommonProxy";
	public static final String CLIENTPROXY = "info.u_team.overworld_mirror.proxy.ClientProxy";
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	private OverworldMirrorConstants() {
	}
}
