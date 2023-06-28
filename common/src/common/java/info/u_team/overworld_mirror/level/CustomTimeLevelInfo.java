package info.u_team.overworld_mirror.level;

import java.util.UUID;

import info.u_team.u_team_core.util.LevelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.border.WorldBorder.Settings;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.timers.TimerQueue;

public class CustomTimeLevelInfo implements ServerLevelData {
	
	private final ServerLevelData info;
	
	private boolean firstTick;
	private long dayTime;
	
	public CustomTimeLevelInfo(ServerLevelData info) {
		this.info = info;
	}
	
	// Update and save methods
	
	public void tick(ServerLevel level) {
		if (!firstTick) {
			firstTick = true;
			setDayTime(getSavedData(level).getDayTime());
		}
		if (getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
			setDayTime(getDayTime() + 1);
		}
		getSavedData(level).updateDayTime(getDayTime());
	}
	
	public DimensionDataLevelSavedData getSavedData(ServerLevel level) {
		final String name = "overworldmirror_dimensiondata";
		return LevelUtil.getSaveData(level, name, DimensionDataLevelSavedData::load, DimensionDataLevelSavedData::new);
	}
	
	// Custom dimension time
	
	@Override
	public void setDayTime(long time) {
		dayTime = time;
	}
	
	@Override
	public long getDayTime() {
		return dayTime;
	}
	
	// TODO delegate for now but consider adding them for extra weather in dimension
	
	@Override
	public void setThundering(boolean thunderingIn) {
		info.setThundering(thunderingIn);
	}
	
	@Override
	public int getRainTime() {
		return info.getRainTime();
	}
	
	@Override
	public boolean isThundering() {
		return info.isThundering();
	}
	
	@Override
	public void setRainTime(int time) {
		info.setRainTime(time);
	}
	
	@Override
	public boolean isRaining() {
		return info.isRaining();
	}
	
	@Override
	public void setThunderTime(int time) {
		info.setThunderTime(time);
	}
	
	@Override
	public void setRaining(boolean isRaining) {
		info.setRaining(isRaining);
	}
	
	@Override
	public int getThunderTime() {
		return info.getThunderTime();
	}
	
	@Override
	public int getClearWeatherTime() {
		return info.getClearWeatherTime();
	}
	
	@Override
	public void setClearWeatherTime(int time) {
		info.setClearWeatherTime(time);
	}
	
	// Delegated methods
	
	@Override
	public void setXSpawn(int x) {
		info.setXSpawn(x);
	}
	
	@Override
	public int getXSpawn() {
		return info.getXSpawn();
	}
	
	@Override
	public void setYSpawn(int y) {
		info.setYSpawn(y);
	}
	
	@Override
	public int getYSpawn() {
		return info.getYSpawn();
	}
	
	@Override
	public void setZSpawn(int z) {
		info.setZSpawn(z);
	}
	
	@Override
	public String getLevelName() {
		return info.getLevelName();
	}
	
	@Override
	public int getZSpawn() {
		return info.getZSpawn();
	}
	
	@Override
	public void setSpawnAngle(float angle) {
		info.setSpawnAngle(angle);
	}
	
	@Override
	public float getSpawnAngle() {
		return info.getSpawnAngle();
	}
	
	@Override
	public void setSpawn(BlockPos spawnPoint, float angle) {
		info.setSpawn(spawnPoint, angle);
	}
	
	@Override
	public long getGameTime() {
		return info.getGameTime();
	}
	
	@Override
	public boolean isHardcore() {
		return info.isHardcore();
	}
	
	@Override
	public GameRules getGameRules() {
		return info.getGameRules();
	}
	
	@Override
	public Difficulty getDifficulty() {
		return info.getDifficulty();
	}
	
	@Override
	public boolean isDifficultyLocked() {
		return info.isDifficultyLocked();
	}
	
	@Override
	public int getWanderingTraderSpawnDelay() {
		return info.getWanderingTraderSpawnDelay();
	}
	
	@Override
	public void setWanderingTraderSpawnDelay(int delay) {
		info.setWanderingTraderSpawnDelay(delay);
	}
	
	@Override
	public int getWanderingTraderSpawnChance() {
		return info.getWanderingTraderSpawnChance();
	}
	
	@Override
	public void setWanderingTraderSpawnChance(int chance) {
		info.setWanderingTraderSpawnChance(chance);
	}
	
	@Override
	public UUID getWanderingTraderId() {
		return info.getWanderingTraderId();
	}
	
	@Override
	public void setWanderingTraderId(UUID id) {
		info.setWanderingTraderId(id);
	}
	
	@Override
	public GameType getGameType() {
		return info.getGameType();
	}
	
	@Override
	public void setWorldBorder(Settings serializer) {
		info.setWorldBorder(serializer);
	}
	
	@Override
	public Settings getWorldBorder() {
		return info.getWorldBorder();
	}
	
	@Override
	public boolean isInitialized() {
		return info.isInitialized();
	}
	
	@Override
	public void setInitialized(boolean initializedIn) {
		info.setInitialized(initializedIn);
	}
	
	@Override
	public boolean getAllowCommands() {
		return info.getAllowCommands();
	}
	
	@Override
	public void setGameType(GameType type) {
		info.setGameType(type);
	}
	
	@Override
	public TimerQueue<MinecraftServer> getScheduledEvents() {
		return info.getScheduledEvents();
	}
	
	@Override
	public void setGameTime(long time) {
		info.setGameTime(time);
	}
}
