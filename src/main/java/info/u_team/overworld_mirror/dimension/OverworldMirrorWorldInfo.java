package info.u_team.overworld_mirror.dimension;

import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;

import info.u_team.overworld_mirror.config.ServerConfig;
import net.minecraft.nbt.*;
import net.minecraft.util.JSONUtils;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.*;

public class OverworldMirrorWorldInfo extends DerivedWorldInfo {
	
	private static final ServerConfig CONFIG = ServerConfig.getInstance();
	
	private final WorldType type;
	private final CompoundNBT options;
	
	private long dayTime;
	
	public OverworldMirrorWorldInfo(WorldInfo worldInfo) {
		super(worldInfo);
		
		type = initType();
		options = initOptions();
	}
	
	private WorldType initType() {
		final WorldType worldType = WorldType.byName(CONFIG.generatorType.get());
		if (worldType != null) {
			return worldType;
		}
		return WorldType.DEFAULT;
	}
	
	private CompoundNBT initOptions() {
		final String settings = CONFIG.generatorSettings.get();
		
		final JsonObject json;
		if (type == WorldType.FLAT) {
			json = new JsonObject();
			json.addProperty("flat_world_options", settings);
		} else if (!settings.isEmpty()) {
			json = JSONUtils.fromJson(settings);
		} else {
			json = new JsonObject();
		}
		return (CompoundNBT) Dynamic.convert(JsonOps.INSTANCE, NBTDynamicOps.INSTANCE, json);
	}
	
	@Override
	public WorldType getGenerator() {
		return type;
	}
	
	@Override
	public CompoundNBT getGeneratorOptions() {
		return options;
	}
	
	@Override
	public long getSeed() {
		return CONFIG.seedType.get().calculateSeed(CONFIG.seedValue.get(), super.getSeed());
	}
	
	@Override
	public long getDayTime() {
		return dayTime;
	}
	
	@Override
	public void setDayTime(long time) {
		dayTime = time;
	}
}
