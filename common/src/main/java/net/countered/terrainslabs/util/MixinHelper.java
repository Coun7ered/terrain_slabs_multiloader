package net.countered.terrainslabs.util;

import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;

public class MixinHelper {
    public static boolean notBottomSlab(BlockState state ) {
        return !( state.getBlock() instanceof ISlabCopy)
                || state.getValue( SlabBlock.TYPE ) != SlabType.BOTTOM;
    }

    private static boolean isBottomSlab( BlockState state ) {
        return state.getBlock() instanceof ISlabCopy && state.getValue( SlabBlock.TYPE ) == SlabType.BOTTOM;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean shouldBeOnTopState( BlockGetter level, BlockPos pos, BlockState state ) {
        if ( !((IOffsetState) state ).terrain_slabs$hasOffsetState() ) {
            return false;
        }

        // TODO: implement in double plant block
        BlockPos belowPos = pos.below();
        if (state.getBlock() instanceof DoublePlantBlock && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
            belowPos = pos.below(2);
        }

        BlockState belowState = level.getBlockState(belowPos);
        return isBottomSlab( belowState ) || ( (IOffsetState) belowState ).terrain_slabs$isOffset();
    }
}
