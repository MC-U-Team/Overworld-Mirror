package info.u_team.overworld_mirror.dimension;

import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;

import info.u_team.overworld_mirror.config.ServerConfig;
import info.u_team.u_team_core.util.world.WorldUtil;
import net.minecraft.nbt.*;
import net.minecraft.util.JSONUtils;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.*;

public class OverworldMirrorWorldInfo extends DerivedWorldInfo {
	
	private static final ServerConfig CONFIG = ServerConfig.getInstance();
	
	private final TimeWorldSavedData timeData;
	
	public OverworldMirrorWorldInfo(World world, WorldInfo worldInfo) {
		super(worldInfo);
		timeData = world instanceof ServerWorld ? getSavedData((ServerWorld) world) : new TimeWorldSavedData("dummy");
	}
	
	@Override
	public WorldType getGenerator() {
		final WorldType type = WorldType.byName(CONFIG.generatorType.get());
		if (type != null) {
			return type;
		}
		return super.getGenerator();
	}
	
	@Override
	public CompoundNBT getGeneratorOptions() {
		final String generatorSettings = CONFIG.generatorSettings.get();
		
		JsonObject json = new JsonObject();
		if (getGenerator() == WorldType.FLAT) {
			json.addProperty("flat_world_options", generatorSettings);
		} else if (!generatorSettings.isEmpty()) {
			json = JSONUtils.fromJson(generatorSettings);
		}
		
		return (CompoundNBT) Dynamic.convert(JsonOps.INSTANCE, NBTDynamicOps.INSTANCE, json);
	}
	
	@Override
	public long getSeed() {
		return CONFIG.seedType.get().calculateSeed(CONFIG.seedValue.get(), super.getSeed());
	}
	
	@Override
	public long getGameTime() {
		return timeData.getTime();
	}
	
	@Override
	public void setGameTime(long time) {
		timeData.setTime(time);
	}
	
	public static TimeWorldSavedData getSavedData(ServerWorld world) {
		final String name = "overworldmirror_time";
		return WorldUtil.getSaveData(world, name, () -> new TimeWorldSavedData(name));
	}
}
