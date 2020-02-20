package info.u_team.overworld_mirror.dimension;

import java.util.Optional;

import com.mojang.datafixers.Dynamic;

import info.u_team.overworld_mirror.config.ServerConfig;
import info.u_team.u_team_core.util.world.WorldUtil;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.world.World;
import net.minecraft.world.dimension.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.server.ServerWorld;

public class OverworldMirrorDimension extends OverworldDimension {
	
	private TimeWorldSavedData timeData;
	
	public OverworldMirrorDimension(World world, DimensionType type) {
		super(world, type);
		world.worldInfo = new OverworldMirrorWorldInfo(world.worldInfo);
	}
	
	@Override
	public double getMovementFactor() {
		return ServerConfig.getInstance().movementFactor.get();
	}
	
	@Override
	public void setWorldTime(long time) {
		super.setWorldTime(time);
		getTimeData().ifPresent(data -> {
			data.setTime(time);
		});
	}
	
	@Override
	public long getWorldTime() {
		// Init time
		if (timeData == null && world instanceof ServerWorld) {
			getTimeData().ifPresent(data -> super.setWorldTime(data.getTime()));
		}
		return super.getWorldTime();
	}
	
	private Optional<TimeWorldSavedData> getTimeData() {
		if (timeData == null && world instanceof ServerWorld) {
			final String name = "time";
			timeData = WorldUtil.getSaveData((ServerWorld) world, name, () -> new TimeWorldSavedData(name));
		}
		return Optional.ofNullable(timeData);
	}
	
}
