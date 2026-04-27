package net.countered.terrainslabs.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;

import java.util.List;

public class PlatformChunkPersistence {

    @ExpectPlatform
    public static List<BlockPos> getBotSlabPositions() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setBotSlabPositions(List<BlockPos> blockPosList) {
        throw new AssertionError();
    }
}
