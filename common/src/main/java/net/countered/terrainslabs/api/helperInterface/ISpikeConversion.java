package net.countered.terrainslabs.api.helperInterface;

import net.countered.terrainslabs.api.ICustomOffsetConversion;
import net.countered.terrainslabs.block.OffsetProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

/**
 * An implemented conversion class for offset "spikes" (such as dripstone) to ensure disconnected tips do not merge.
 *
 * @param <T> Type of the elements for the spike thickness property (such as the DripstoneThickness enum class)
 */
public interface ISpikeConversion <T extends Comparable<T>> extends ICustomOffsetConversion {

    /**
     * @return property for the "spike" thickness
     */
    @SuppressWarnings("unchecked")
    default Property<T> thicknessProperty() {
        return (Property<T>) PointedDripstoneBlock.THICKNESS;
    }

    /**
     * @return merge enum element
     */
    @SuppressWarnings("unchecked")
    default T mergeValue() {
        return (T) DripstoneThickness.TIP_MERGE;
    }

    /**
     * @return tip enum element
     */
    @SuppressWarnings("unchecked")
    default T tipValue() {
        return (T) DripstoneThickness.TIP;
    }

    /**
     * @return Direction Property of the "Spike" (expected UP or DOWN, others not handled)
     */
    default DirectionProperty tipDirectionProperty() {
        return PointedDripstoneBlock.TIP_DIRECTION;
    }


    //-------------//
    // Implemented //
    //-------------//


    @Override
    default <L extends LevelAccessor> @NotNull BlockState onSetOntop(L level, BlockPos pos, BlockState state) {
        return handleOffset(level, pos, state, OffsetProperty.OffsetType.ONTOP);
    }

    @Override
    default <L extends LevelAccessor> @NotNull BlockState onSetOnbottom(L level, BlockPos pos, BlockState state) {
        return handleOffset(level, pos, state, OffsetProperty.OffsetType.ONBOTTOM);
    }

    private <L extends LevelAccessor> BlockState handleOffset(L level, BlockPos pos, BlockState state, OffsetProperty.OffsetType type){
        if ( state.getValue( thicknessProperty() ) == mergeValue() ) {
            BlockPos offPos = pos.relative( state.getValue( tipDirectionProperty() ) );
            BlockState offState = level.getBlockState( offPos );

            level.setBlock( offPos, offState.setValue( thicknessProperty(), tipValue() ), PointedDripstoneBlock.UPDATE_CLIENTS );
            return state.setValue( thicknessProperty(), tipValue() );
        }

        return state;
    }

}
