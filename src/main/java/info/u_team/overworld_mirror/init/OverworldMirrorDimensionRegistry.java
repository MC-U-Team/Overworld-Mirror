package info.u_team.overworld_mirror.init;

import java.util.Map;

import com.google.common.collect.ImmutableList;

import info.u_team.overworld_mirror.config.ServerConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class OverworldMirrorDimensionRegistry {
	
	@SuppressWarnings("deprecation")
	private static void serverStarting(ServerStartingEvent event) {
		final MinecraftServer server = event.getServer();
		
		// Recreate createLevels at the end
		
		final Map<ResourceKey<Level>, ServerLevel> worlds = server.forgeGetWorldMap();
		// Check if dimension is not already registered
		if (!worlds.containsKey(OverworldMirrorWorldKeys.MIRROR_OVERWORLD)) {
			// Get some important fields
			final WorldData serverConfig = server.getWorldData();
			final WorldGenSettings worldGenSettings = serverConfig.worldGenSettings();
			final RegistryAccess registries = server.registryAccess();
			
			// Calculate used seed
			final long seed = ServerConfig.getInstance().seedType.get().calculateSeed(ServerConfig.getInstance().seedValue.get(), worldGenSettings.seed());
			
			// Get dimension type
			final DimensionType dimensionType = registries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getOrThrow(OverworldMirrorDimensionTypeKeys.MIRROR_OVERWORLD);
			
			// Create chunk generator
			// final ChunkGenerator generator =
			// WorldGenSettings.makeDefaultOverworld(registries.registryOrThrow(Registry.BIOME_REGISTRY),
			// registries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY), seed);
			
			// Create dimension
			final LevelStem levelStem = worldGenSettings.dimensions().get(LevelStem.OVERWORLD);
			
			// Register dimension
			// worldGenSettings.dimensions().register(null, null, null)
			// worldGenSettings.dimensions().register(OverworldMirrorDimensionKeys.MIRROR_OVERWORLD, levelStem,
			// Lifecycle.experimental());
			
			// Create world
			final DerivedLevelData worldInfo = new DerivedLevelData(serverConfig, serverConfig.overworldData());
			final ServerLevel world = new ServerLevel(server, server.executor, server.storageSource, worldInfo, OverworldMirrorWorldKeys.MIRROR_OVERWORLD, levelStem, server.progressListenerFactory.create(11), worldGenSettings.isDebug(), BiomeManager.obfuscateSeed(seed), ImmutableList.of(), false);
			
			// Add world border listener
			server.getLevel(Level.OVERWORLD).getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(world.getWorldBorder()));
			
			// Register world
			worlds.put(OverworldMirrorWorldKeys.MIRROR_OVERWORLD, world);
			server.markWorldsDirty();
			
			// Post world load event
			MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(worlds.get(OverworldMirrorWorldKeys.MIRROR_OVERWORLD)));
		}
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(OverworldMirrorDimensionRegistry::serverStarting);
	}
	
}
