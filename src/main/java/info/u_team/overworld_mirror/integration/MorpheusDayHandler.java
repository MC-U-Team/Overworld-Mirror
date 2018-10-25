package info.u_team.overworld_mirror.integration;

import info.u_team.overworld_mirror.init.OverworldMirrorDimensions;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.*;
import net.quetzi.morpheus.api.INewDayHandler;

@Optional.Interface(iface = "net.quetzi.morpheus.api.INewDayHandler", modid = "morpheus", striprefs = true)
public class MorpheusDayHandler implements INewDayHandler {
	
	@Override
	public void startNewDay() {
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(OverworldMirrorDimensions.dimension_id);
		world.setWorldTime(world.getWorldTime() + getTimeToSunrise(world));
	}
	
	private long getTimeToSunrise(World world) {
		long dayLength = 24000;
		return dayLength - (world.getWorldTime() % dayLength);
	}
}
