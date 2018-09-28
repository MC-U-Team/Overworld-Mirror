
package info.u_team.overworld_mirror.config;

import static info.u_team.overworld_mirror.OverworldMirrorConstants.MODID;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = MODID, category = "")
public class CommonConfig {
	
	public static Settings settings = new Settings();
	
	public static class Settings {
		
		@Comment("The worldtype of the mirrored world")
		@Name("World Type")
		public String world_type = "default";
		
		@Comment("The generator settings of the mirrored world")
		@Name("Generator Settings")
		public String generator_settings = "";
		
		@Comment("If you want a completly mirrored world, you need to set this to 0")
		@Name("Seed Shift")
		public int seed_shift = 100000;
		
		@Comment("Set this if you want to have a higher movement factor like the nether has 8")
		@Name("Movement Factor")
		@RangeDouble(min = 0, max = Integer.MAX_VALUE)
		public double movement_factor = 1;
		
	}
	
}
