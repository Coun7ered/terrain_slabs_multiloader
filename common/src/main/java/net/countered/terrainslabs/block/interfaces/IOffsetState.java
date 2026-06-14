package net.countered.terrainslabs.block.interfaces;

import net.countered.platform.PlatformConfigHooks;
import net.countered.terrainslabs.api.IConditionalOffset;
import net.countered.terrainslabs.api.OffsetClasses;
import net.countered.terrainslabs.block.OffsetProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * Handles like 95% of offset logic
 */
public interface IOffsetState {

    static <L extends LevelAccessor> BlockState getCorrectState( L level, BlockPos pos, BlockState initialState ) {
        if ( shouldBeOntopState( level, pos, initialState )
                && !((IOffsetState) initialState).terrain_slabs$isOffsetAbove()
        ) {
            return ((IOffsetState) initialState).terrain_slabs$getOntopState( level, pos, initialState );

        } else if ( shouldBeOnbottomState( level, pos, initialState )
                && !((IOffsetState) initialState).terrain_slabs$isOffsetBelow()
        ) {
            return ((IOffsetState) initialState).terrain_slabs$getOnbottomState( level, pos, initialState );
        }

        return initialState;
    }

    static boolean shouldBeOntopState(BlockGetter level, BlockPos pos, BlockState state) {
        if ( !shouldAllowOntopState( state ) || !((IOffsetState) state ).terrain_slabs$hasOntopState() ) {
            return false;
        }

        if ( state.getBlock() instanceof IConditionalOffset conditional && !conditional.terrain_slabs$couldPlaceOntop( level, pos, state ) ) {
            return false;
        }

        BlockState belowState = level.getBlockState( pos.below() );
        return ISlabCopy.isBottomSlab( belowState ) || ( (IOffsetState) belowState ).terrain_slabs$isOffsetAbove();
    }

    static boolean shouldAllowOntopState( BlockState state ) {
        Block block = state.getBlock();
        if ( OffsetClasses.isDefaultOntop( block ) ) {
            return !PlatformConfigHooks.excludeOntop( block );
        }

        return PlatformConfigHooks.includeOntop( block );
    }

    static boolean shouldBeOnbottomState(BlockGetter level, BlockPos pos, BlockState state) {
        if ( !shouldAllowOnbottomState( state ) || !((IOffsetState) state ).terrain_slabs$hasOnbottomState() ) {
            return false;
        }

        if ( state.getBlock() instanceof IConditionalOffset conditional && !conditional.terrain_slabs$couldPlaceOnbottom( level, pos, state ) ) {
            return false;
        }

        BlockState aboveState = level.getBlockState( pos.above() );
        return ISlabCopy.isTopSlab( aboveState ) || ( (IOffsetState) aboveState ).terrain_slabs$isOffsetBelow();
    }

    static boolean shouldAllowOnbottomState( BlockState state ) {
        Block block = state.getBlock();
        if ( OffsetClasses.isDefaultOnbottom( block ) ) {
            return !PlatformConfigHooks.excludeOnbottom( block );
        }

        return PlatformConfigHooks.includeOnbottom( block );
    }

    boolean terrain_slabs$isOffsetAbove();
    boolean terrain_slabs$isOffsetBelow();
    boolean terrain_slabs$isOffset();

    boolean terrain_slabs$hasOntopState();
    boolean terrain_slabs$hasOnbottomState();
    boolean terrain_slabs$hasOffsetState();

    EnumProperty<OffsetProperty.OffsetType> terrain_slabs$getOffsetProperty();

    @SuppressWarnings("unused")
    BlockState terrain_slabs$getNormalState();

    <L extends LevelAccessor> BlockState terrain_slabs$getOntopState( L level, BlockPos pos, BlockState initialState );
    <L extends LevelAccessor> BlockState terrain_slabs$getOnbottomState( L level, BlockPos pos, BlockState initialState );
}
