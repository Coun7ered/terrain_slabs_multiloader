package net.countered.terrainslabs.util;

import net.countered.terrainslabs.block.ModBlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MixinHelper {

    public static boolean terrain_slabs$isStateValidOnTop(BlockState state) {
        return state.is(ModBlockTags.ON_TOP_BLOCKS) // TODO move away from tags as they are loaded too late
                || state.getBlock() instanceof VegetationBlock
                || state.is(Blocks.SNOW);
    }
}
