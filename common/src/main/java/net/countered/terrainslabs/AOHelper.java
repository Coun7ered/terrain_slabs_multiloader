package net.countered.terrainslabs;

import net.minecraft.core.BlockPos;

public class AOHelper {
    public static final ThreadLocal<BlockPos> SNOW_SLAB_POS = ThreadLocal.withInitial(() -> null);
}