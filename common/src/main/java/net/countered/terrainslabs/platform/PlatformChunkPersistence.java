package net.countered.terrainslabs.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.List;

public class PlatformChunkPersistence {

    @ExpectPlatform
    public static List<BlockPos> getBotSlabPositions(ChunkAccess chunk) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setBotSlabPositions(ChunkAccess chunk, List<BlockPos> blockPosList) {
        throw new AssertionError();
    }
}
