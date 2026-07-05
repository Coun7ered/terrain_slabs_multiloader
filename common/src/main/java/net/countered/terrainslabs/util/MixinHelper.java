package net.countered.terrainslabs.util;

import net.countered.terrainslabs.api.SlabHelper;
import net.minecraft.world.level.block.state.BlockState;

public class MixinHelper {

    public static boolean terrain_slabs$isStateValidOnTop(BlockState state) {
        return SlabHelper.isValidOntop(state);
    }
}
