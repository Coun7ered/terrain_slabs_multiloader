package net.countered.terrainslabs.util;

import net.countered.terrainslabs.block.ModBlockTags;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MixinHelper {

    public static boolean terrain_slabs$isStateValidOnTop(BlockState state) {
        return state.is(ModBlockTags.ON_TOP_BLOCKS) ||
                state.getBlock() instanceof BushBlock;
    }
}
