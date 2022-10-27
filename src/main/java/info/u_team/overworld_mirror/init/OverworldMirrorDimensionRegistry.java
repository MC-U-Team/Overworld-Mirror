package info.u_team.overworld_mirror.init;

import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;

import info.u_team.overworld_mirror.config.ServerConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.level.Level;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class OverworldMirrorDimensionRegistry {
	
	@SuppressWarnings("deprecation")
	private static void serverStarting(FMLServerStartingEvent event) {
		final MinecraftServer server = event.getServer();
		
		// Recreate func_240787_a_ at the end
		
		final Map<ResourceKey<Level>, ServerLevel> worlds = server.forgeGetWorldMap();
		// Check if dimension is not already registered
		if (!worlds.containsKey(OverworldMirrorWorldKeys.MIRROR_OVERWORLD)) {
			// Get some important fields
			final WorldData serverConfig = server.getServerConfiguration();
			final DimensionGeneratorSettings dimensionSettings = serverConfig.getDimensionGeneratorSettings();
			final DynamicRegistries registries = server.getDynamicRegistries();
			
			// Calculate used seed
			final long seed = ServerConfig.getInstance().seedType.get().calculateSeed(ServerConfig.getInstance().seedValue.get(), dimensionSettings.getSeed());
			
			// Get dimension type
			final DimensionType type = registries.func_230520_a_().getOrThrow(OverworldMirrorDimensionTypeKeys.MIRROR_OVERWORLD);
			
			// Create chunk generator
			final ChunkGenerator generator = DimensionGeneratorSettings.func_242750_a(registries.getRegistry(Registry.BIOME_KEY), registries.getRegistry(Registry.NOISE_SETTINGS_KEY), seed);
			
			// Create dimension
			final Dimension dimension = new Dimension(() -> type, generator);
			
			// Register dimension
			dimensionSettings.func_236224_e_().register(OverworldMirrorDimensionKeys.MIRROR_OVERWORLD, dimension, Lifecycle.experimental());
			
			// Create world
			final DerivedWorldInfo worldInfo = new DerivedWorldInfo(serverConfig, serverConfig.getServerWorldInfo());
			final ServerWorld world = new ServerWorld(server, server.backgroundExecutor, server.anvilConverterForAnvilFile, worldInfo, OverworldMirrorWorldKeys.MIRROR_OVERWORLD, dimension.getDimensionType(), server.chunkStatusListenerFactory.create(11), dimension.getChunkGenerator(), dimensionSettings.hasDebugChunkGenerator(), BiomeManager.getHashedSeed(seed), ImmutableList.of(), false);
			
			// Add world border listener
			server.getWorld(World.OVERWORLD).getWorldBorder().addListener(new IBorderListener.Impl(world.getWorldBorder()));
			
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
