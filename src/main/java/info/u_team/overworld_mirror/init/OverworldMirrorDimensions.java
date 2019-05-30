package info.u_team.overworld_mirror.init;

import info.u_team.overworld_mirror.config.CommonConfig;
import info.u_team.overworld_mirror.dimension.WorldProviderMirroredSurface;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class OverworldMirrorDimensions {
	
	public static final int dimension_id = CommonConfig.settings.dimension_id;
	
	public static final DimensionType dimension_type = DimensionType.register("mirroredoverworld", "_mirrored_overworld", dimension_id, WorldProviderMirroredSurface.class, true);
	
	public static void construct() {
		DimensionManager.registerDimension(dimension_id, dimension_type);
	}
	
}
