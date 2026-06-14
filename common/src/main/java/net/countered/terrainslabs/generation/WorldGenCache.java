package net.countered.terrainslabs.generation;

import net.countered.terrainslabs.TerrainSlabs;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class WorldGenCache {

    // Should not be a memory leak...
    private static final int INITIAL_WARNING_SIZE = 1000;
    private static final double GROWTH_FACTOR = 1.5;

    private int warningSize = INITIAL_WARNING_SIZE;
    final private Map<ChunkAccess, ArrayList<BlockPos>> CACHE = new HashMap<>( INITIAL_WARNING_SIZE );
    final private Function<BlockState, Boolean> filter;

    public WorldGenCache() {
        this.filter = state -> true;
    }

    public WorldGenCache( Function<BlockState, Boolean> filter ) {
        this.filter = filter;
    }

    public boolean containsChunk( ChunkAccess chunk ) {
        return CACHE.containsKey( chunk );
    }

    public <L extends LevelAccessor> boolean addBlockPos( L level, BlockPos pos, BlockState state ) {
        if ( !filter.apply( state ) ) {
            return false;
        }

        return addBlockPosUncheched( level, pos );
    }

    protected <L extends LevelAccessor> boolean addBlockPosUncheched( L level, BlockPos pos ) {
        ChunkAccess chunk = level.getChunk( pos );
        if ( containsChunk( chunk ) ) {
            CACHE.get( chunk ).add( pos );
            return true;
        } else if ( mapChunk( chunk ) ) {
            CACHE.get( chunk ).add( pos );
            return true;
        }

        return false;
    }

    public void forEachPos( ChunkAccess chunk, Consumer<BlockPos> handler ) {
        if( !containsChunk( chunk ) ) {
            return;
        }

        ArrayList<BlockPos> list = this.getList( chunk );
        assert list != null;
        for ( BlockPos pos : list ) {
            handler.accept( pos );
        }
    }

    private ArrayList<BlockPos> getList( ChunkAccess chunk ) {
        if ( containsChunk( chunk ) ) {
            return CACHE.get( chunk );
        }

        return null;
    }

    protected boolean mapChunk( ChunkAccess chunk ) {
        if ( containsChunk( chunk ) || chunk.getStatus().isOrAfter( ChunkStatus.INITIALIZE_LIGHT ) ) {
            return false;
        }

        if ( CACHE.size() >= warningSize) {
            trimCache();
        }

        CACHE.put( chunk, new ArrayList<>( 200 ) );
        return true;
    }

    protected void removeChunk( ChunkAccess chunk ) {
        CACHE.remove( chunk );
    }

    /**
     * Should not normally occur. Will cause offsets to fail
     */
    private void trimCache() {
//        List<ChunkAccess> chunksToRemove = new ArrayList<>();
//        CACHE.forEach( (chunk, stack ) -> {
//            if ( shouldTrimChunk( chunk ) ) chunksToRemove.add( chunk );
//        } );
//
//        chunksToRemove.forEach( this::removeChunk ); //; trimmed to size {}, CACHE.size()
        LoggerFactory.getLogger( TerrainSlabs.MOD_ID ).warn( "Placed Slab Cache grew to size {}.", warningSize );
        warningSize = Math.max( (int) ( CACHE.size() * GROWTH_FACTOR ), INITIAL_WARNING_SIZE );
    }

//    private boolean shouldTrimChunk(ChunkAccess chunk ) {
//        if ( !containsChunk( chunk ) ) {
//            return false;
//        }
//
//        return chunk.getStatus().isOrAfter( ChunkStatus.SPAWN );
//    }
}
