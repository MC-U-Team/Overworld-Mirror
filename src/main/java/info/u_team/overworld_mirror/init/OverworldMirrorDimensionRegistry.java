package info.u_team.overworld_mirror.init;

import java.util.Map;

import org.apache.logging.log4j.LogManager;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;

import info.u_team.overworld_mirror.config.ServerConfig;
import info.u_team.overworld_mirror.util.JsonUtil;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenSettingsExport;
import net.minecraft.util.registry.WorldSettingsImport;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.gen.ChunkGenerator;
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
			
			// Calculate used seed
			final long seed = ServerConfig.getInstance().seedType.get().calculateSeed(ServerConfig.getInstance().seedValue.get(), dimensionSettings.getSeed());
			
			// Create chunk generator
			ChunkGenerator generator;
			
			try {
				final JsonObject jsonObject = new JsonParser().parse(ServerConfig.getInstance().chunkGenerator.get()).getAsJsonObject();
				JsonUtil.replaceNamedLongElement(jsonObject, "seed", oldSeed -> oldSeed == 0, seed);
				// final WorldGenSettingsExport<JsonElement> ops = WorldGenSettingsExport.create(JsonOps.INSTANCE,
				// server.getDynamicRegistries());
				
				final WorldSettingsImport<JsonElement> ops = WorldSettingsImport.create(JsonOps.INSTANCE, server.getDataPackRegistries().getResourceManager(), DynamicRegistries.func_239770_b_());
				
				// generator = Registry.CHUNK_GENERATOR_CODEC.decode(ops, jsonObject).map(pair -> pair.getFirst().decode(ops,
				// pair.getSecond()).get().orThrow()).get().orThrow().getFirst();
				generator = ChunkGenerator.field_235948_a_.decode(ops, jsonObject).get().orThrow().getFirst();
				System.out.println(generator);
			} catch (RuntimeException ex) {
				LogManager.getLogger().warn("Cannot create overworld mirror chunkgenerator from config. Fallback to overworld. The supplier json string is {}", ServerConfig.getInstance().chunkGenerator.get(), ex);
				generator = DimensionGeneratorSettings.func_242750_a(server.getDynamicRegistries().getRegistry(Registry.BIOME_KEY), server.getDynamicRegistries().getRegistry(Registry.NOISE_SETTINGS_KEY), seed);
			}
			
			// Create dimension
			final Dimension dimension = new Dimension(() -> server.getDynamicRegistries().func_230520_a_().getOrThrow(OverworldMirrorDimensionTypeKeys.MIRROR_OVERWORLD), generator);
			
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
