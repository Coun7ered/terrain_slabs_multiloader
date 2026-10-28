package net.countered.terrainslabs.util;

import net.countered.terrainslabs.config.TSConfig;
import net.countered.terrainslabs.registries.ModOnTopBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;

public class OnTopHelper {

    /**
     * Checks if the given block is snow that can be placed on top of slabs.
     */
    public static boolean isSnow(BlockState state) {
        return state.is(Blocks.SNOW) || ModOnTopBlocksRegistry.getOnTopBlocks().contains(state.getBlock());
    }

    /**
     * Checks if the given block is vegetation that can be placed on top of slabs (excludes saplings).
     */
    public static boolean isVegetation(BlockState state) {
        return state.getBlock() instanceof VegetationBlock && !(state.getBlock() instanceof SaplingBlock);
    }

    /**
     * Checks if the block could ever be an on-top block (used at model bake time).
     */
    public static boolean isCandidate(BlockState state) {
        return isSnow(state) || isVegetation(state);
    }

    /**
     * Checks if the block is an on-top block AND its respective config is enabled.
     */
    public static boolean isStateValidOnTop(BlockState state) {
        if (isSnow(state)) {
            return TSConfig.isSnowOnSlabsEnabled();
        }
        if (isVegetation(state)) {
            return TSConfig.isVegetationOnSlabsEnabled();
        }
        return false;
    }

    /**
     * Backwards-compatible alias for isStateValidOnTop.
     */
    public static boolean terrain_slabs$isStateValidOnTop(BlockState state) {
        return isStateValidOnTop(state);
    }

    /**
     * Checks if the block at pos is a bottom slab.
     */
    public static boolean isBottomSlab(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM;
    }

    /**
     * Checks if an on-top block at pos should have its model / collision shifted down by 0.5.
     */
    public static boolean shouldShift(BlockGetter level, BlockPos pos, BlockState state) {
        if (!isStateValidOnTop(state)) {
            return false;
        }
        if (isBottomSlab(level, pos.below())) {
            return true;
        }
        if (state.getBlock() instanceof DoublePlantBlock && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
            return isBottomSlab(level, pos.below(2));
        }
        return false;
    }

    /**
     * Checks if the neighbor in the given direction is on a bottom slab.
     */
    public static boolean isNeighborOnSlab(BlockGetter level, BlockPos pos, Direction dir) {
        BlockPos neighborPos = pos.relative(dir);
        return isBottomSlab(level, neighborPos.below());
    }

    /**
     * Checks if any horizontal/vertical neighbor is on a bottom slab.
     */
    public static boolean hasSlabNeighbor(BlockGetter level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (isNeighborOnSlab(level, pos, dir)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the model should be intercepted by SlabOffsetModel.
     */
    public static boolean shouldHandleModel(BlockGetter level, BlockPos pos, BlockState state) {
        if (!isStateValidOnTop(state)) {
            return false;
        }
        return shouldShift(level, pos, state) || hasSlabNeighbor(level, pos);
    }
}
