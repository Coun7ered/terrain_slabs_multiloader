package net.countered.terrainslabs.generation;

import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;
import java.util.function.Function;

/**
 * Did not work when run as a feature; missed too many things despite my efforts. Run as a custom final step instead.
 * TODO: Solve why some are still missed occasionally. Probably worldgen region border?
 */
final public class OffsetFeature {

    public static final SlabCache BOTTOM_SLAB_CACHE = new SlabCache( Direction.UP, state ->
            state.getBlock() instanceof SlabBlock && state.getValue( SlabBlock.TYPE ) == SlabType.BOTTOM
    );
    public static final SlabCache TOP_SLAB_CACHE = new SlabCache( Direction.DOWN, state ->
            state.getBlock() instanceof SlabBlock && state.getValue( SlabBlock.TYPE ) == SlabType.TOP
    );

    public static void run( ServerLevel serverLevel, ProtoChunk chunk ) {
        if ( !PlatformConfigHooks.isSlabGenerationEnabled()
                || ( !PlatformConfigHooks.isSnowOnSlabsEnabled() && !PlatformConfigHooks.isVegetationOnSlabsEnabled() )
        ) {
            return;
        }

        LevelAccessor level = new WorldGenRegion( serverLevel, List.of( chunk ), ChunkStatus.SPAWN, 0 );
        if ( !BOTTOM_SLAB_CACHE.containsChunk( chunk ) || !TOP_SLAB_CACHE.containsChunk( chunk ) ) {
            fixChunkOffsets( level, chunk );
        } else {
            fixSurfaceOffsets( level, chunk, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES );
            fixChunkOffsetsCached( level, chunk, BOTTOM_SLAB_CACHE );
            fixChunkOffsetsCached( level, chunk, TOP_SLAB_CACHE );
            BOTTOM_SLAB_CACHE.removeChunk( chunk );
            TOP_SLAB_CACHE.removeChunk( chunk );
        }
    }

    private static void fixChunkOffsetsCached( LevelAccessor level, ChunkAccess chunk, SlabCache cache ) {
        cache.forEachPos( chunk, ( pos ) -> replaceStatesInDir( level, pos, cache.getDirection() ) );
    }

    // This is a fallback method. Cached version runs faster but is not always available.
    private static void fixChunkOffsets( LevelAccessor level, ChunkAccess chunk ) {
        FeatureUtil.forEachChunkBlock( level, chunk, Heightmap.Types.WORLD_SURFACE_WG, (pos, maxY ) -> {
            BlockState state = level.getBlockState( pos );

            if ( !( state.getBlock() instanceof SlabBlock) ) {
                return; //continue
            }

            if ( ( state.getValue( SlabBlock.TYPE ) == SlabType.BOTTOM ) ) {
                replaceStatesInDir( level, pos, Direction.UP );
            } else if ( state.getValue( SlabBlock.TYPE ) == SlabType.TOP ) {
                replaceStatesInDir( level, pos, Direction.DOWN );
            }
        } );
    }

    private static void fixSurfaceOffsets( LevelAccessor level, ChunkAccess chunk, Heightmap.Types heightType ) {
        FeatureUtil.forEachSurfaceBlock( level, chunk, heightType, (topPos, minY ) ->
                replaceStatesInDir( level, topPos, Direction.UP )
        );
    }

    private static void replaceStatesInDir(LevelAccessor level, BlockPos pos, Direction dir ) {
        FeatureUtil.iterateDirUntilFail( level, pos.relative(dir), dir,
                dir == Direction.UP
                        ? (aPos, aState) -> placeOntopState( level, aPos, aState )
                        : (aPos, aState) -> placeOnbottomState( level, aPos, aState )
        );
    }

    private static boolean placeOntopState( LevelAccessor level, BlockPos pos, BlockState state ) {
        if ( IOffsetState.canGenerateOntop( level, pos, state ) ) {
            state = ((IOffsetState) state ).terrain_slabs$getOntopState( level, pos, state );
            return level.setBlock( pos, state, Block.UPDATE_CLIENTS );
        }

        return false;
    }
    private static boolean placeOnbottomState( LevelAccessor level, BlockPos pos, BlockState state ) {
        if (IOffsetState.canGenerateOnbottom(level, pos, state)) {
            state = ((IOffsetState) state).terrain_slabs$getOnbottomState( level, pos, state );
            return level.setBlock(pos, state, Block.UPDATE_CLIENTS);
        }

        return false;
    }

    public static class SlabCache extends WorldGenCache {
        final private Direction direction;

        @SuppressWarnings("unused")
        public SlabCache(Direction dir ) {
            this( dir, state -> true );
        }

        public SlabCache( Direction dir, Function<BlockState, Boolean> filter ) {
            super( filter );
            if ( !dir.getAxis().isVertical() ) {
                throw new IllegalArgumentException(
                        "Direction input to 'fixAllOffsetsCached' in 'OffsetFeature' must have vertical axis" );
            }

            this.direction = dir;
        }

        public Direction getDirection() {
            return direction;
        }

    }
}
