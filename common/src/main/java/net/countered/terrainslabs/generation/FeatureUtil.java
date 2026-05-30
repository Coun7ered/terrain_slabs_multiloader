package net.countered.terrainslabs.generation;

import net.countered.terrainslabs.TerrainSlabs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public final class FeatureUtil {

    public static void iterateChunkBlocks( WorldGenLevel level, BlockPos origin, BiConsumer<BlockPos, Integer> handler ) {
        ChunkPos chunkPos = new ChunkPos(origin);
        level.getChunk( chunkPos.x, chunkPos.z );

        int minY = level.getMinBuildHeight();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkPos.getMinBlockX() + x;
                int worldZ = chunkPos.getMinBlockZ() + z;
                int maxY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ);
                for (int y = maxY; y >= minY; y--) {
                    BlockPos currentPos = new BlockPos(worldX, y, worldZ);
                    handler.accept( currentPos, minY );
                }
            }
        }
    }

    public static void iterateAbove(WorldGenLevel level, BlockPos pos, int maxY, BiFunction<BlockPos, BlockState, Boolean> func) {
        while ( pos.getY() <= maxY && func.apply( pos, level.getBlockState( pos ) ) ) {
            pos = pos.above();
        }
    }

    public static void iterateBelow(WorldGenLevel level, BlockPos pos, int minY, BiFunction<BlockPos, BlockState, Boolean> func) {
        while ( pos.getY() >= minY && func.apply( pos, level.getBlockState( pos ) ) ) {
            pos = pos.below();
        }
    }

    protected static class BlockPosCache {

        // Should not be a memory leak... should not be...
        final private static int INITIAL_WARNING_SIZE = 100;
        private static int warningSize = INITIAL_WARNING_SIZE;
        final protected static Map<ChunkAccess, Stack<BlockPos>> PLACED_SLABS = new HashMap<>();

        public static <L extends LevelAccessor> void addSlabPos(L level, BlockPos pos ) {
            ChunkAccess chunk = level.getChunk( pos );

            if ( PLACED_SLABS.containsKey( chunk ) ) {
                PLACED_SLABS.get( chunk ).push( pos );
            } else {
                mapChunk( chunk );
                PLACED_SLABS.get( chunk ).push( pos );
            }
        }

        protected static Optional<Stack<BlockPos>> getChunk(ChunkAccess chunk ) {
            if ( PLACED_SLABS.containsKey( chunk ) ) {
                return Optional.of( PLACED_SLABS.get( chunk ) );
            }

            return Optional.empty();
        }

        protected static Optional<Stack<BlockPos>> popChunk( ChunkAccess chunk ) {
            Optional<Stack<BlockPos>> stackOption = getChunk( chunk );
            PLACED_SLABS.remove( chunk );

            return stackOption;
        }

        private static void mapChunk( ChunkAccess chunk ) {
            if ( PLACED_SLABS.containsKey( chunk ) || chunk.getStatus().isOrAfter( ChunkStatus.INITIALIZE_LIGHT ) ) {
                return;
            }

            if ( PLACED_SLABS.size() >= warningSize) {
                trimCache();
            }

            PLACED_SLABS.put( chunk, new Stack<>() );
        }

        private static void trimCache() {
            PLACED_SLABS.forEach( ( chunk, stack ) -> {
                if ( chunk.getStatus().isOrAfter( ChunkStatus.INITIALIZE_LIGHT ) ) {
                    PLACED_SLABS.remove( chunk );
                }
            } );

            TerrainSlabs.LOGGER.warn( "Placed Slab Cache grew to size {}; trimmed to size {}.", warningSize, PLACED_SLABS.size() );
            warningSize = Math.max( PLACED_SLABS.size() * 2, INITIAL_WARNING_SIZE );
        }
    }
}
