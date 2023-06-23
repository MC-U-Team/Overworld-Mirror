package info.u_team.overworld_mirror.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import info.u_team.overworld_mirror.OverworldMirrorReference;
import info.u_team.overworld_mirror.util.ConfigValueHolder;
import info.u_team.u_team_core.UCoreReference;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.util.Mth;

public class FabricCommonConfig {
	
	private static final FabricCommonConfig INSTANCE = new FabricCommonConfig();
	
	public static FabricCommonConfig getInstance() {
		return INSTANCE;
	}
	
	public final ConfigValueHolder<Double> portalSearchDistanceOverworld;
	public final ConfigValueHolder<Double> portalSearchDistanceOverworldMirror;
	
	private final Path path = FabricLoader.getInstance().getConfigDir().resolve(OverworldMirrorReference.MODID + ".properties");
	private final Properties properties;
	
	private FabricCommonConfig() {
		properties = new Properties();
		
		if (Files.exists(path)) {
			load();
		}
		
		properties.computeIfAbsent("portalSearchDistanceOverworld", unused -> "30");
		properties.computeIfAbsent("portalSearchDistanceOverworldMirror", unused -> "30");
		
		portalSearchDistanceOverworld = new ConfigValueHolder<>(() -> {
			return Mth.clamp(Double.valueOf(properties.getProperty("portalSearchDistanceOverworld", "30")), 1, 1e10);
		}, value -> {
			properties.put("portalSearchDistanceOverworld", value.toString());
			Util.ioPool().submit(this::save);
		});
		
		portalSearchDistanceOverworldMirror = new ConfigValueHolder<>(() -> {
			return Mth.clamp(Double.valueOf(properties.getProperty("portalSearchDistanceOverworldMirror", "30")), 1, 1e10);
		}, value -> {
			properties.put("portalSearchDistanceOverworldMirror", value.toString());
			Util.ioPool().submit(this::save);
		});
		
		if (!Files.exists(path)) {
			save();
		}
	}
	
	private void load() {
		try (final Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			properties.load(reader);
		} catch (final IOException ex) {
			UCoreReference.LOGGER.warn("Could not read property file '" + path.toAbsolutePath() + "'", ex);
		}
	}
	
	private void save() {
		try (final BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			properties.store(writer, "Configuration file for Overworld Mirror mod");
		} catch (final IOException ex) {
			UCoreReference.LOGGER.warn("Could not read property file '" + path.toAbsolutePath() + "'", ex);
		}
	}
	
	public static class Impl extends CommonConfig {
		
		@Override
		public ConfigValueHolder<Double> portalSearchDistanceOverworld() {
			return INSTANCE.portalSearchDistanceOverworld;
		}
		
		@Override
		public ConfigValueHolder<Double> portalSearchDistanceOverworldMirror() {
			return INSTANCE.portalSearchDistanceOverworldMirror;
		}
	}
}
