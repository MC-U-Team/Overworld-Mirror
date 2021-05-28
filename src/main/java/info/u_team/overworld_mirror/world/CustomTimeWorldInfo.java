package info.u_team.overworld_mirror.world;

import java.util.UUID;

import net.minecraft.command.TimerCallbackManager;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.border.WorldBorder.Serializer;
import net.minecraft.world.storage.IServerWorldInfo;

public class CustomTimeWorldInfo implements IServerWorldInfo {
	
	private final IServerWorldInfo info;
	
	private long dayTime;
	
	public CustomTimeWorldInfo(IServerWorldInfo info) {
		this.info = info;
	}
	
	public void tick() {
		if (getGameRulesInstance().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
			setDayTime(getDayTime() + 1);
		}
	}
	
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
	public void setSpawnX(int x) {
		info.setSpawnX(x);
	}
	
	@Override
	public int getSpawnX() {
		return info.getSpawnX();
	}
	
	@Override
	public void setSpawnY(int y) {
		info.setSpawnY(y);
	}
	
	@Override
	public int getSpawnY() {
		return info.getSpawnY();
	}
	
	@Override
	public void setSpawnZ(int z) {
		info.setSpawnZ(z);
	}
	
	@Override
	public String getWorldName() {
		return info.getWorldName();
	}
	
	@Override
	public int getSpawnZ() {
		return info.getSpawnZ();
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
	public void addToCrashReport(CrashReportCategory category) {
		info.addToCrashReport(category);
	}
	
	@Override
	public GameRules getGameRulesInstance() {
		return info.getGameRulesInstance();
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
	public void setWanderingTraderID(UUID id) {
		info.setWanderingTraderID(id);
	}
	
	@Override
	public GameType getGameType() {
		return info.getGameType();
	}
	
	@Override
	public void setWorldBorderSerializer(Serializer serializer) {
		info.setWorldBorderSerializer(serializer);
	}
	
	@Override
	public Serializer getWorldBorderSerializer() {
		return info.getWorldBorderSerializer();
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
	public boolean areCommandsAllowed() {
		return info.areCommandsAllowed();
	}
	
	@Override
	public void setGameType(GameType type) {
		info.setGameType(type);
	}
	
	@Override
	public TimerCallbackManager<MinecraftServer> getScheduledEvents() {
		return info.getScheduledEvents();
	}
	
	@Override
	public void setGameTime(long time) {
		info.setGameTime(time);
	}
}
