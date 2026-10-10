package net.countered.terrainslabs.generation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.*;
import java.util.function.BiConsumer;

public final class FeatureUtil {
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

    public static <L extends LevelAccessor> void forEachChunkBlock( L level, ChunkAccess chunk, Heightmap.Types heightType, int buffer, BiConsumer<BlockPos, Integer> handler ) {
        forEachSurfaceBlock( level, chunk, heightType, buffer, ( topPos, minY ) -> {
            int maxY = topPos.getY();
            for (int y = maxY; y >= minY; y--) {
                BlockPos currentPos = new BlockPos( topPos.getX(), y, topPos.getZ() );
                handler.accept( currentPos, maxY );
            }
        } );
    }
}
