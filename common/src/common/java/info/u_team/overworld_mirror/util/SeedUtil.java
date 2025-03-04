package info.u_team.overworld_mirror.util;

import info.u_team.overworld_mirror.config.CommonConfig;
import info.u_team.u_team_core.util.LevelUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

public class SeedUtil {
	
	public static long getOverworldMirrorSeed(MinecraftServer server) {
		return LevelUtil.getSaveData(server.overworld(), "overworldmirror_seed", OverworldMirrorSeedSavedData::load, () -> {
			final OverworldMirrorSeedSavedData data = new OverworldMirrorSeedSavedData(getOverworldMirrorSeedConfigValue(server));
			data.setDirty();
			return data;
		}).getSeed();
	}
	
	private static long getOverworldMirrorSeedConfigValue(MinecraftServer server) {
		final long seedValue = CommonConfig.getInstance().seed().get();
		
		if (CommonConfig.getInstance().seedAddition().get()) {
			final long overworldSeed = server.getWorldData().worldGenOptions().seed();
			return overworldSeed + seedValue;
		} else {
			return seedValue;
		}
	}
	
	private static class OverworldMirrorSeedSavedData extends SavedData {
		
		private long seed;
		
		public OverworldMirrorSeedSavedData(long seed) {
			this.seed = seed;
		}
		
		public static OverworldMirrorSeedSavedData load(CompoundTag compound, HolderLookup.Provider provider) {
			return new OverworldMirrorSeedSavedData(compound.getLong("seed"));
		}
		
		@Override
		public CompoundTag save(CompoundTag compound, HolderLookup.Provider provider) {
			compound.putLong("seed", seed);
			return compound;
		}
		
		public long getSeed() {
			return seed;
		}
	}
}
