package info.u_team.overworld_mirror.init;

import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;

import info.u_team.overworld_mirror.config.ServerConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class OverworldMirrorDimensionRegistry {
	
	@SuppressWarnings("deprecation")
	private static void serverStarting(FMLServerStartingEvent event) {
		final MinecraftServer server = event.getServer();
		
		// Recreate createLevels at the end
		
		final Map<ResourceKey<Level>, ServerLevel> worlds = server.forgeGetWorldMap();
		// Check if dimension is not already registered
		if (!worlds.containsKey(OverworldMirrorWorldKeys.MIRROR_OVERWORLD)) {
			// Get some important fields
			final WorldData serverConfig = server.getWorldData();
			final WorldGenSettings dimensionSettings = serverConfig.worldGenSettings();
			final RegistryAccess registries = server.registryAccess();
			
			// Calculate used seed
			final long seed = ServerConfig.getInstance().seedType.get().calculateSeed(ServerConfig.getInstance().seedValue.get(), dimensionSettings.seed());
			
			// Get dimension type
			final DimensionType type = registries.dimensionTypes().getOrThrow(OverworldMirrorDimensionTypeKeys.MIRROR_OVERWORLD);
			
			// Create chunk generator
			final ChunkGenerator generator = WorldGenSettings.makeDefaultOverworld(registries.registryOrThrow(Registry.BIOME_REGISTRY), registries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY), seed);
			
			// Create dimension
			final LevelStem dimension = new LevelStem(() -> type, generator);
			
			// Register dimension
			dimensionSettings.dimensions().register(OverworldMirrorDimensionKeys.MIRROR_OVERWORLD, dimension, Lifecycle.experimental());
			
			// Create world
			final DerivedLevelData worldInfo = new DerivedLevelData(serverConfig, serverConfig.overworldData());
			final ServerLevel world = new ServerLevel(server, server.executor, server.storageSource, worldInfo, OverworldMirrorWorldKeys.MIRROR_OVERWORLD, dimension.type(), server.progressListenerFactory.create(11), dimension.generator(), dimensionSettings.isDebug(), BiomeManager.obfuscateSeed(seed), ImmutableList.of(), false);
			
			// Add world border listener
			server.getLevel(Level.OVERWORLD).getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(world.getWorldBorder()));
			
			// Register world
			worlds.put(OverworldMirrorWorldKeys.MIRROR_OVERWORLD, world);
			server.markWorldsDirty();
			
			// Post world load event
			MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(worlds.get(OverworldMirrorWorldKeys.MIRROR_OVERWORLD)));
		}
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(OverworldMirrorDimensionRegistry::serverStarting);
	}
	
}
