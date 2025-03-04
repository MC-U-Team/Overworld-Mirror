package info.u_team.overworld_mirror.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.datafixers.DataFixer;

import info.u_team.overworld_mirror.init.OverworldMirrorLevelKeys;
import info.u_team.overworld_mirror.util.SeedUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

@Mixin(ServerLevel.class)
abstract class ServerLevelMixin {
	
	@Inject(method = "Lnet/minecraft/server/level/ServerLevel;getSeed()J", at = @At(value = "HEAD"), cancellable = true)
	private void overworldmirror$getSeed(CallbackInfoReturnable<Long> callbackInfo) {
		final ServerLevel level = (ServerLevel) (Object) this;
		if (level.dimension() == OverworldMirrorLevelKeys.MIRROR_OVERWORLD) {
			callbackInfo.setReturnValue(SeedUtil.getOverworldMirrorSeed(level.getServer()));
		}
	}
	
	@ModifyArg(method = "Lnet/minecraft/server/level/ServerLevel;<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/StructureCheck;<init>(Lnet/minecraft/world/level/chunk/storage/ChunkScanAccess;Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/LevelHeightAccessor;Lnet/minecraft/world/level/biome/BiomeSource;JLcom/mojang/datafixers/DataFixer;)V", ordinal = 0), index = 8)
	private long overworldmirror$init(ChunkScanAccess storageAccess, RegistryAccess registryAccess, StructureTemplateManager structureTemplateManager, ResourceKey<Level> dimension, ChunkGenerator chunkGenerator, RandomState randomState, LevelHeightAccessor heightAccessor, BiomeSource biomeSource, long seed, DataFixer fixerUpper) {
		if (dimension == OverworldMirrorLevelKeys.MIRROR_OVERWORLD) {
			return SeedUtil.getOverworldMirrorSeed(((ServerLevel) (Object) this).getServer());
		} else {
			return seed;
		}
	}
	
}
