
package info.u_team.overworld_mirror.config;

import static info.u_team.overworld_mirror.OverworldMirrorConstants.MODID;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = MODID, category = "")
public class CommonConfig {
	
	public static Settings settings = new Settings();
	
	public static class Settings {
		
		@RequiresMcRestart
		@Comment("The Dimension ID to avoid conflicts with other mods")
		@Name("Dimension ID")
		public int dimension_id = DimensionManager.getNextFreeDimId();
		
		@Comment("The worldtype of the mirrored world")
		@Name("World Type")
		public String world_type = "default";
		
		@Comment("The generator settings of the mirrored world")
		@Name("Generator Settings")
		public String generator_settings = "";
		
		@Comment("If you want a completly mirrored world, you need to set this to 0")
		@Name("Seed Shift")
		public int seed_shift = 100_000;
		
		@Comment("Set this if you want to have a higher movement factor like the nether has 8")
		@Name("Movement Factor")
		@RangeDouble(min = 0, max = Integer.MAX_VALUE)
		public double movement_factor = 1;
		
		@Comment("How many blocks the portal can be from the normal spawn location to not create a new one. The value is not in sqrt cause of lag reduction. 400 = 20 Blocks, 900 = 30 Blocks, etc")
		@Name("Portal Distance")
		@RangeDouble(min = 1, max = 1e20)
		public double portal_distance = 400;
		
		@Comment("Some mods only use the dimension id to generate their ores and structure in the world. This calls the generator an other time with the dimension id set to 0 (overworld) to generate all stuff. ATTENTION: This might generate way to many ores because mods that only check the world provider may generate twice! Use with caution!")
		@Name("Enable Generation Hackery")
		public boolean enable_generation_hackery = false;
		
	}
	
}
