package net.countered.terrainslabs.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Implement interface for special case blocks to prevent offsetting under certain conditions.
 * <p>
 * Use ICustomOffsetConversion instead if a state should be offset but modified.
 * <p>
 * For example, this was added for lantern blocks.
 */
public interface IConditionalOffset {

    /**
     * return false to prevent ontop offset. ICustomOffsetConversion will not be applied if cancelled.
     */
    <L extends BlockGetter> boolean couldPlaceOntop(L level, BlockPos pos, BlockState state );

    /**
     * return false to prevent onbottom offset. ICustomOffsetConversion will not be applied if cancelled.
     */
    <L extends BlockGetter> boolean couldPlaceOnbottom(L level, BlockPos pos, BlockState state );

}
