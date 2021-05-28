package info.u_team.overworld_mirror.init;

import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class OverworldMirrorDimensionRegistry {
	
	@SuppressWarnings("deprecation")
	private static void serverStarting(FMLServerStartingEvent event) {
		final MinecraftServer server = event.getServer();
		
		// Recreate func_240787_a_ at the end
		
		final Map<RegistryKey<World>, ServerWorld> worlds = server.forgeGetWorldMap();
		// Check if dimension is not already registered
		if (!worlds.containsKey(OverworldMirrorWorldKeys.MIRROR_OVERWORLD)) {
			// Get some important fields
			final IServerConfiguration serverConfig = server.getServerConfiguration();
			final DimensionGeneratorSettings dimensionSettings = serverConfig.getDimensionGeneratorSettings();
			
			// Create dimension
			final Dimension dimension = new Dimension(() -> server.getDynamicRegistries().func_230520_a_().getOrThrow(OverworldMirrorDimensionTypeKeys.MIRROR_OVERWORLD), DimensionGeneratorSettings.func_242750_a(server.getDynamicRegistries().getRegistry(Registry.BIOME_KEY), server.getDynamicRegistries().getRegistry(Registry.NOISE_SETTINGS_KEY), dimensionSettings.getSeed()));
			
			// Register dimension
			dimensionSettings.func_236224_e_().register(OverworldMirrorDimensionKeys.MIRROR_OVERWORLD, dimension, Lifecycle.experimental());
			
			// Create world
			final DerivedWorldInfo worldInfo = new DerivedWorldInfo(serverConfig, serverConfig.getServerWorldInfo());
			final ServerWorld world = new ServerWorld(server, server.backgroundExecutor, server.anvilConverterForAnvilFile, worldInfo, OverworldMirrorWorldKeys.MIRROR_OVERWORLD, dimension.getDimensionType(), server.chunkStatusListenerFactory.create(11), dimension.getChunkGenerator(), dimensionSettings.hasDebugChunkGenerator(), BiomeManager.getHashedSeed(dimensionSettings.getSeed()), ImmutableList.of(), false);
			
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
