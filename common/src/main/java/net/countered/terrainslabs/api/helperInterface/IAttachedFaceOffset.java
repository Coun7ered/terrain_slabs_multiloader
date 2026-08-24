package net.countered.terrainslabs.api.helperInterface;

import net.countered.terrainslabs.api.IConditionalOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Implemented version of IConditionalOffset for the "facing" property
 */
public interface IAttachedFaceOffset extends IConditionalOffset {

    @Override
    default <L extends BlockGetter> boolean couldBeOntop(L level, BlockPos pos, BlockState state ) {
        if ( !state.hasProperty( BlockStateProperties.FACING ) ) {
            return false;
        }

        return state.getValue( BlockStateProperties.FACING ) == Direction.UP;
    }

    @Override
    default <L extends BlockGetter> boolean couldBeOnbottom(L level, BlockPos pos, BlockState state ) {
        if ( !state.hasProperty( BlockStateProperties.FACING ) ) {
            return false;
        }

        return state.getValue( BlockStateProperties.FACING ) == Direction.DOWN;
    }
}
