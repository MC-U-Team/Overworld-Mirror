package info.u_team.overworld_mirror.dimension;

import java.util.List;

import info.u_team.overworld_mirror.config.CommonConfig;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WrappedChunkGenerator implements IChunkGenerator {
	
	private final World world;
	private final IChunkGenerator wrapped;
	
	public WrappedChunkGenerator(World world, IChunkGenerator wrapped) {
		this.world = world;
		this.wrapped = wrapped;
	}
	
	@Override
	public Chunk generateChunk(int x, int z) {
		return wrapped.generateChunk(x, z);
	}
	
	@Override
	public void populate(int x, int z) {
		wrapped.populate(x, z);
		if (CommonConfig.settings.enable_generation_hackery) {
			// Hackery to simulate some mods another dimension id to generate their stuff in
			// mirrored overworld
			int dimIDBefore = world.provider.getDimension();
			world.provider.setDimension(0);
			GameRegistry.generateWorld(x, z, world, this, world.getChunkProvider());
			world.provider.setDimension(dimIDBefore);
		}
	}
	
	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return wrapped.generateStructures(chunkIn, x, z);
	}
	
	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		return wrapped.getPossibleCreatures(creatureType, pos);
	}
	
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
		return wrapped.getNearestStructurePos(worldIn, structureName, position, findUnexplored);
	}
	
	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {
		wrapped.recreateStructures(chunkIn, x, z);
	}
	
	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		return wrapped.isInsideStructure(worldIn, structureName, pos);
	}
	
}
