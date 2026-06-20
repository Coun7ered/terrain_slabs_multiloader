package net.countered.terrainslabs.generation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public final class FeatureUtil {

    public static <L extends LevelAccessor> void forEachSurfaceBlock( L level, ChunkAccess chunk, Heightmap.Types heightType, BiConsumer<BlockPos, Integer> handler ) {
        forEachSurfaceBlock( level, chunk, heightType, 0, handler);
    }

    public static <L extends LevelAccessor> void forEachSurfaceBlock( L level, ChunkAccess chunk, Heightmap.Types heightType, int buffer, BiConsumer<BlockPos, Integer> handler ) {
        ChunkPos chunkPos = chunk.getPos();

        int minY = level.getMinBuildHeight();
        for (int x = -buffer; x < 16 + buffer; x++) {
            for (int z = -buffer; z < 16 + buffer; z++) {
                int worldX = chunkPos.getMinBlockX() + x;
                int worldZ = chunkPos.getMinBlockZ() + z;
                int maxY = level.getHeight(heightType, worldX, worldZ);
                BlockPos topPos = new BlockPos( worldX, maxY, worldZ );
                handler.accept( topPos, minY );
            }
        }
    }

    public static <L extends LevelAccessor> void forEachChunkBlock( L level, ChunkAccess chunk, Heightmap.Types heightType, BiConsumer<BlockPos, Integer> handler ) {
        forEachChunkBlock( level, chunk, heightType, 0, handler);
    }

    public static <L extends LevelAccessor> void forEachChunkBlock( L level, ChunkAccess chunk, Heightmap.Types heightType, int buffer, BiConsumer<BlockPos, Integer> handler ) {
        forEachSurfaceBlock( level, chunk, heightType, buffer, ( topPos, minY ) -> {
            int maxY = topPos.getY();
            for (int y = maxY; y >= minY; y--) {
                BlockPos currentPos = new BlockPos( topPos.getX(), y, topPos.getZ() );
                handler.accept( currentPos, maxY );
            }
        } );
    }

    public static <L extends LevelAccessor> boolean iterateDirUntilFail( L level, BlockPos pos, Direction dir, BiFunction<BlockPos, BlockState, Boolean> func) {
        BlockPos initPos = pos;
        int yLimit = dir == Direction.UP ? level.getMaxBuildHeight() + 1 : level.getMinBuildHeight() - 1;

        while ( pos.getY() != yLimit && func.apply( pos, level.getBlockState( pos ) ) ) {
            pos = pos.relative( dir );
        }

        return pos != initPos;
    }
}
