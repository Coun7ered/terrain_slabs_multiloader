package net.countered.terrainslabs.api;

import net.countered.terrainslabs.block.OffsetProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Implement interface to change blockstate and level before offset state is applied, but after IConditionalOffset check.
 * <p>
 * For example, this was added for dripstone which should not use connected states when separated by offset.
 */
public interface ICustomOffsetConversion {

    /**
     * Method for additional state or level modification when a block becomes "ontop"
     * Applied before offset state is applied, but after IConditionalOffset check.
     * Output the input state to skip method.
     */
    <L extends LevelAccessor> @NotNull BlockState onSetOntop(L level, BlockPos pos, BlockState state);

    /**
     * Method for additional state or level modification when a block becomes "onbottom"
     * Applied before offset state is applied, but after IConditionalOffset check.
     * Output the input state to skip method.
     */
    <L extends LevelAccessor> @NotNull BlockState onSetOnbottom( L level, BlockPos pos, BlockState state);

}
