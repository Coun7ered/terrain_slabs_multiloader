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

    static <L extends BlockGetter> boolean allowOntop( L level, BlockPos pos, BlockState state ) {
        return !( state.getBlock() instanceof IConditionalOffset conditional && !conditional.couldBeOntop( level, pos, state ) );
    }
    static <L extends BlockGetter> boolean allowOnbottom( L level, BlockPos pos, BlockState state ) {
        return !( state.getBlock() instanceof IConditionalOffset conditional && !conditional.couldBeOnbottom( level, pos, state ) );
    }

    /**
     * return false to cancel ontop offset. ICustomOffsetConversion will not be applied if cancelled.
     */
    <L extends BlockGetter> boolean couldBeOntop(L level, BlockPos pos, BlockState state );

    /**
     * return false to cancel onbottom offset. ICustomOffsetConversion will not be applied if cancelled.
     */
    <L extends BlockGetter> boolean couldBeOnbottom(L level, BlockPos pos, BlockState state );

}
