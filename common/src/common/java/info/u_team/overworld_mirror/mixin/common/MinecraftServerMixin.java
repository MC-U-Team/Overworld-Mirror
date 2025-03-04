package info.u_team.overworld_mirror.mixin.common;

import java.util.List;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import info.u_team.overworld_mirror.init.OverworldMirrorLevelKeys;
import info.u_team.overworld_mirror.util.SeedUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;

@Mixin(MinecraftServer.class)
abstract class MinecraftServerMixin {
	
	@ModifyArg(method = "Lnet/minecraft/server/MinecraftServer;createLevels(Lnet/minecraft/server/level/progress/ChunkProgressListener;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;Lnet/minecraft/server/level/progress/ChunkProgressListener;ZJLjava/util/List;ZLnet/minecraft/world/RandomSequences;)V", ordinal = 1), index = 8)
	private long overworldmirror$createLevels(MinecraftServer server, Executor dispatcher, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey<Level> dimension, LevelStem levelStem, ChunkProgressListener progressListener, boolean isDebug, long biomeZoomSeed, List<CustomSpawner> customSpawners, boolean tickTime, RandomSequences randomSequences) {
		if (dimension == OverworldMirrorLevelKeys.MIRROR_OVERWORLD) {
			return BiomeManager.obfuscateSeed(SeedUtil.getOverworldMirrorSeed(server));
		} else {
			return biomeZoomSeed;
		}
	}
	
}
