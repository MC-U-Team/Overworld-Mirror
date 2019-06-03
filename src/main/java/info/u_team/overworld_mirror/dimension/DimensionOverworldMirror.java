package info.u_team.overworld_mirror.dimension;

import com.google.gson.*;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;

import info.u_team.overworld_mirror.config.CommonConfig;
import info.u_team.u_team_core.util.world.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.*;
import net.minecraft.world.dimension.*;
import net.minecraft.world.gen.*;

public class DimensionOverworldMirror extends OverworldDimension {
	
	private WorldSaveDataDimension saveData;
	private CommonConfig config;
	
	public DimensionOverworldMirror(DimensionType type) {
		super(type);
	}
	
	@Override
	protected void init() {
		super.init();
		saveData = getSaveData();
		config = CommonConfig.getInstance();
	}
	
	// Can unload chunk
	
	@Override
	public boolean canDropChunk(int x, int z) {
		return false;
	}
	
	// Time
	
	@Override
	public void setWorldTime(long time) {
		saveData.setTime(time);
	}
	
	@Override
	public long getWorldTime() {
		return saveData.getTime();
	}
	
	// Seed
	
	@Override
	public long getSeed() {
		return config.seedType.get().calculateSeed(config.seedValue.get(), super.getSeed());
	}
	
	// Movement factor
	@Override
	public double getMovementFactor() {
		return config.movementFactor.get();
	}
	
	// Override gen settings
	
	@SuppressWarnings("deprecation")
	@Override
	public IChunkGenerator<? extends IChunkGenSettings> createChunkGenerator() {
		// Get settings from config
		WorldType worldtype = WorldType.byName(config.generatorType.get());
		if (worldtype == null) {
			worldtype = WorldType.DEFAULT;
		}
		
		final String configGeneratorOptionsString = config.generatorSettings.get();
		JsonObject configGeneratorOptionsJson = new JsonObject();
		if (!configGeneratorOptionsString.isEmpty()) {
			configGeneratorOptionsJson = JsonUtils.fromJson(configGeneratorOptionsString, true);
		}
		
		NBTTagCompound generationOptions = (NBTTagCompound) Dynamic.convert(JsonOps.INSTANCE, NBTDynamicOps.INSTANCE, configGeneratorOptionsJson);
		
		if (generationOptions == null) {
			generationOptions = new NBTTagCompound();
		}
		
		// Copy from super class with additions and settings
		
		final ChunkGeneratorType<FlatGenSettings, ChunkGeneratorFlat> flatChunkGenerator = ChunkGeneratorType.FLAT;
		final ChunkGeneratorType<DebugGenSettings, ChunkGeneratorDebug> debugChunkGenerator = ChunkGeneratorType.DEBUG;
		final ChunkGeneratorType<NetherGenSettings, ChunkGeneratorNether> cavesChunkGenerator = ChunkGeneratorType.CAVES;
		final ChunkGeneratorType<EndGenSettings, ChunkGeneratorEnd> floatingIslandsChunkGenerator = ChunkGeneratorType.FLOATING_ISLANDS;
		final ChunkGeneratorType<OverworldGenSettings, ChunkGeneratorOverworld> surfaceChunkGenerator = ChunkGeneratorType.SURFACE;
		
		final BiomeProviderType<SingleBiomeProviderSettings, SingleBiomeProvider> fixedBiomeProvider = BiomeProviderType.FIXED;
		final BiomeProviderType<OverworldBiomeProviderSettings, OverworldBiomeProvider> vanillaLayeredBiomeProvider = BiomeProviderType.VANILLA_LAYERED;
		final BiomeProviderType<CheckerboardBiomeProviderSettings, CheckerboardBiomeProvider> checkerBoardBiomeProvider = BiomeProviderType.CHECKERBOARD;
		
		if (worldtype == WorldType.FLAT) {
			FlatGenSettings flatgensettings = FlatGenSettings.createFlatGenerator(new Dynamic<>(NBTDynamicOps.INSTANCE, generationOptions));
			SingleBiomeProviderSettings singlebiomeprovidersettings1 = fixedBiomeProvider.createSettings().setBiome(flatgensettings.getBiome());
			return flatChunkGenerator.create(this.world, fixedBiomeProvider.create(singlebiomeprovidersettings1), flatgensettings);
		} else if (worldtype == WorldType.DEBUG_ALL_BLOCK_STATES) {
			SingleBiomeProviderSettings singlebiomeprovidersettings = fixedBiomeProvider.createSettings().setBiome(Biomes.PLAINS);
			return debugChunkGenerator.create(this.world, fixedBiomeProvider.create(singlebiomeprovidersettings), debugChunkGenerator.createSettings());
		} else if (worldtype != WorldType.BUFFET) {
			OverworldGenSettings overworldgensettings = surfaceChunkGenerator.createSettings();
			OverworldBiomeProviderSettings overworldbiomeprovidersettings = vanillaLayeredBiomeProvider.createSettings().setWorldInfo(new SeedFixWorldInfo(world.getWorldInfo())).setGeneratorSettings(overworldgensettings);
			return surfaceChunkGenerator.create(this.world, vanillaLayeredBiomeProvider.create(overworldbiomeprovidersettings), overworldgensettings);
		} else {
			BiomeProvider biomeprovider = null;
			JsonElement jsonelement = Dynamic.convert(NBTDynamicOps.INSTANCE, JsonOps.INSTANCE, generationOptions);
			JsonObject jsonobject = jsonelement.getAsJsonObject();
			if (jsonobject.has("biome_source") && jsonobject.getAsJsonObject("biome_source").has("type") && jsonobject.getAsJsonObject("biome_source").has("options")) {
				ResourceLocation resourcelocation = new ResourceLocation(jsonobject.getAsJsonObject("biome_source").getAsJsonPrimitive("type").getAsString());
				JsonObject jsonobject1 = jsonobject.getAsJsonObject("biome_source").getAsJsonObject("options");
				Biome[] abiome = new Biome[] { Biomes.OCEAN };
				if (jsonobject1.has("biomes")) {
					JsonArray jsonarray = jsonobject1.getAsJsonArray("biomes");
					abiome = jsonarray.size() > 0 ? new Biome[jsonarray.size()] : new Biome[] { Biomes.OCEAN };
					
					for (int i = 0; i < jsonarray.size(); ++i) {
						Biome biome = IRegistry.BIOME.get(new ResourceLocation(jsonarray.get(i).getAsString()));
						abiome[i] = biome != null ? biome : Biomes.OCEAN;
					}
				}
				
				if (BiomeProviderType.FIXED.getKey().equals(resourcelocation)) {
					SingleBiomeProviderSettings singlebiomeprovidersettings2 = fixedBiomeProvider.createSettings().setBiome(abiome[0]);
					biomeprovider = fixedBiomeProvider.create(singlebiomeprovidersettings2);
				}
				
				if (BiomeProviderType.CHECKERBOARD.getKey().equals(resourcelocation)) {
					int j = jsonobject1.has("size") ? jsonobject1.getAsJsonPrimitive("size").getAsInt() : 2;
					CheckerboardBiomeProviderSettings checkerboardbiomeprovidersettings = checkerBoardBiomeProvider.createSettings().setBiomes(abiome).setSize(j);
					biomeprovider = checkerBoardBiomeProvider.create(checkerboardbiomeprovidersettings);
				}
				
				if (BiomeProviderType.VANILLA_LAYERED.getKey().equals(resourcelocation)) {
					OverworldBiomeProviderSettings overworldbiomeprovidersettings1 = vanillaLayeredBiomeProvider.createSettings().setGeneratorSettings(new OverworldGenSettings()).setWorldInfo(new SeedFixWorldInfo(world.getWorldInfo()));
					biomeprovider = vanillaLayeredBiomeProvider.create(overworldbiomeprovidersettings1);
				}
			}
			
			if (biomeprovider == null) {
				biomeprovider = fixedBiomeProvider.create(fixedBiomeProvider.createSettings().setBiome(Biomes.OCEAN));
			}
			
			IBlockState iblockstate = Blocks.STONE.getDefaultState();
			IBlockState iblockstate1 = Blocks.WATER.getDefaultState();
			if (jsonobject.has("chunk_generator") && jsonobject.getAsJsonObject("chunk_generator").has("options")) {
				if (jsonobject.getAsJsonObject("chunk_generator").getAsJsonObject("options").has("default_block")) {
					String s = jsonobject.getAsJsonObject("chunk_generator").getAsJsonObject("options").getAsJsonPrimitive("default_block").getAsString();
					Block block = IRegistry.BLOCK.getOrDefault(new ResourceLocation(s));
					if (block != null) {
						iblockstate = block.getDefaultState();
					}
				}
				
				if (jsonobject.getAsJsonObject("chunk_generator").getAsJsonObject("options").has("default_fluid")) {
					String s1 = jsonobject.getAsJsonObject("chunk_generator").getAsJsonObject("options").getAsJsonPrimitive("default_fluid").getAsString();
					Block block1 = IRegistry.BLOCK.getOrDefault(new ResourceLocation(s1));
					if (block1 != null) {
						iblockstate1 = block1.getDefaultState();
					}
				}
			}
			
			if (jsonobject.has("chunk_generator") && jsonobject.getAsJsonObject("chunk_generator").has("type")) {
				ResourceLocation resourcelocation1 = new ResourceLocation(jsonobject.getAsJsonObject("chunk_generator").getAsJsonPrimitive("type").getAsString());
				if (ChunkGeneratorType.CAVES.getId().equals(resourcelocation1)) {
					NetherGenSettings nethergensettings = cavesChunkGenerator.createSettings();
					nethergensettings.setDefautBlock(iblockstate);
					nethergensettings.setDefaultFluid(iblockstate1);
					return cavesChunkGenerator.create(this.world, biomeprovider, nethergensettings);
				}
				
				if (ChunkGeneratorType.FLOATING_ISLANDS.getId().equals(resourcelocation1)) {
					EndGenSettings endgensettings = floatingIslandsChunkGenerator.createSettings();
					endgensettings.setSpawnPos(new BlockPos(0, 64, 0));
					endgensettings.setDefautBlock(iblockstate);
					endgensettings.setDefaultFluid(iblockstate1);
					return floatingIslandsChunkGenerator.create(this.world, biomeprovider, endgensettings);
				}
			}
			
			OverworldGenSettings overworldgensettings1 = surfaceChunkGenerator.createSettings();
			overworldgensettings1.setDefautBlock(iblockstate);
			overworldgensettings1.setDefaultFluid(iblockstate1);
			return surfaceChunkGenerator.create(this.world, biomeprovider, overworldgensettings1);
		}
	}
	
	// Save data
	
	private WorldSaveDataDimension getSaveData() {
		return WorldUtil.getSaveData(world, "overworldmirror", WorldSaveDataDimension::new);
	}
}
