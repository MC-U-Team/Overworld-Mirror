package info.u_team.overworld_mirror.dimension;

import com.google.gson.*;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;

import info.u_team.overworld_mirror.config.CommonConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.*;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.ResourceLocation;
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
	
	// Override gen settings
	
	@SuppressWarnings("deprecation")
	@Override
	public IChunkGenerator<? extends IChunkGenSettings> createChunkGenerator() {
		WorldType worldtype = this.world.getWorldInfo().getGenerator();
		ChunkGeneratorType<FlatGenSettings, ChunkGeneratorFlat> chunkgeneratortype = ChunkGeneratorType.FLAT;
		ChunkGeneratorType<DebugGenSettings, ChunkGeneratorDebug> chunkgeneratortype1 = ChunkGeneratorType.DEBUG;
		ChunkGeneratorType<NetherGenSettings, ChunkGeneratorNether> chunkgeneratortype2 = ChunkGeneratorType.CAVES;
		ChunkGeneratorType<EndGenSettings, ChunkGeneratorEnd> chunkgeneratortype3 = ChunkGeneratorType.FLOATING_ISLANDS;
		ChunkGeneratorType<OverworldGenSettings, ChunkGeneratorOverworld> chunkgeneratortype4 = ChunkGeneratorType.SURFACE;
		BiomeProviderType<SingleBiomeProviderSettings, SingleBiomeProvider> biomeprovidertype = BiomeProviderType.FIXED;
		BiomeProviderType<OverworldBiomeProviderSettings, MirroredOverworldBiomeProvider> biomeprovidertype1 = BiomeProviderCustomType.MIRRORED_VANILLA_LAYERED;
		BiomeProviderType<CheckerboardBiomeProviderSettings, CheckerboardBiomeProvider> biomeprovidertype2 = BiomeProviderType.CHECKERBOARD;
		if (worldtype == WorldType.FLAT) {
			FlatGenSettings flatgensettings = FlatGenSettings.createFlatGenerator(new Dynamic<>(NBTDynamicOps.INSTANCE, this.world.getWorldInfo().getGeneratorOptions()));
			SingleBiomeProviderSettings singlebiomeprovidersettings1 = biomeprovidertype.createSettings().setBiome(flatgensettings.getBiome());
			return chunkgeneratortype.create(this.world, biomeprovidertype.create(singlebiomeprovidersettings1), flatgensettings);
		} else if (worldtype == WorldType.DEBUG_ALL_BLOCK_STATES) {
			SingleBiomeProviderSettings singlebiomeprovidersettings = biomeprovidertype.createSettings().setBiome(Biomes.PLAINS);
			return chunkgeneratortype1.create(this.world, biomeprovidertype.create(singlebiomeprovidersettings), chunkgeneratortype1.createSettings());
		} else if (worldtype != WorldType.BUFFET) {
			OverworldGenSettings overworldgensettings = chunkgeneratortype4.createSettings();
			OverworldBiomeProviderSettings overworldbiomeprovidersettings = biomeprovidertype1.createSettings().setWorldInfo(this.world.getWorldInfo()).setGeneratorSettings(overworldgensettings);
			return chunkgeneratortype4.create(this.world, biomeprovidertype1.create(overworldbiomeprovidersettings), overworldgensettings);
		} else {
			BiomeProvider biomeprovider = null;
			JsonElement jsonelement = Dynamic.convert(NBTDynamicOps.INSTANCE, JsonOps.INSTANCE, this.world.getWorldInfo().getGeneratorOptions());
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
					SingleBiomeProviderSettings singlebiomeprovidersettings2 = biomeprovidertype.createSettings().setBiome(abiome[0]);
					biomeprovider = biomeprovidertype.create(singlebiomeprovidersettings2);
				}
				
				if (BiomeProviderType.CHECKERBOARD.getKey().equals(resourcelocation)) {
					int j = jsonobject1.has("size") ? jsonobject1.getAsJsonPrimitive("size").getAsInt() : 2;
					CheckerboardBiomeProviderSettings checkerboardbiomeprovidersettings = biomeprovidertype2.createSettings().setBiomes(abiome).setSize(j);
					biomeprovider = biomeprovidertype2.create(checkerboardbiomeprovidersettings);
				}
				
				if (BiomeProviderType.VANILLA_LAYERED.getKey().equals(resourcelocation)) {
					OverworldBiomeProviderSettings overworldbiomeprovidersettings1 = biomeprovidertype1.createSettings().setGeneratorSettings(new OverworldGenSettings()).setWorldInfo(this.world.getWorldInfo());
					biomeprovider = biomeprovidertype1.create(overworldbiomeprovidersettings1);
				}
			}
			
			if (biomeprovider == null) {
				biomeprovider = biomeprovidertype.create(biomeprovidertype.createSettings().setBiome(Biomes.OCEAN));
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
					NetherGenSettings nethergensettings = chunkgeneratortype2.createSettings();
					nethergensettings.setDefautBlock(iblockstate);
					nethergensettings.setDefaultFluid(iblockstate1);
					return chunkgeneratortype2.create(this.world, biomeprovider, nethergensettings);
				}
				
				if (ChunkGeneratorType.FLOATING_ISLANDS.getId().equals(resourcelocation1)) {
					EndGenSettings endgensettings = chunkgeneratortype3.createSettings();
					endgensettings.setSpawnPos(new BlockPos(0, 64, 0));
					endgensettings.setDefautBlock(iblockstate);
					endgensettings.setDefaultFluid(iblockstate1);
					return chunkgeneratortype3.create(this.world, biomeprovider, endgensettings);
				}
			}
			
			OverworldGenSettings overworldgensettings1 = chunkgeneratortype4.createSettings();
			overworldgensettings1.setDefautBlock(iblockstate);
			overworldgensettings1.setDefaultFluid(iblockstate1);
			return chunkgeneratortype4.create(this.world, biomeprovider, overworldgensettings1);
		}
	}
	
	// Save data
	
	private WorldSaveDataDimension getSaveData() {
		final String name = "overworldmirror";
		final DimensionType type = getWorld().getDimension().getType();
		WorldSaveDataDimension instance = getWorld().getSavedData(type, WorldSaveDataDimension::new, name);
		if (instance == null) {
			instance = new WorldSaveDataDimension(name);
			getWorld().setSavedData(type, name, instance);
		}
		return instance;
	}
}
