package net.countered.terrainslabs.block.interfaces;

import net.countered.terrainslabs.api.IFacedOffsetable;
import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.countered.terrainslabs.api.IConditionalOffset;
import net.countered.terrainslabs.block.OffsetProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * Handles like 95% of offset logic. Should always use these methods when possible
 */
public interface IOffsetState {

    static <L extends LevelAccessor> BlockState getCorrectState( L level, BlockPos pos, BlockState initialState ) {
        if ( shouldBeOntopState( level, pos, initialState ) ) {
            return ((IOffsetState) initialState).terrain_slabs$getOntopState( level, pos, initialState );

        } else if ( shouldBeOnbottomState( level, pos, initialState ) ) {
            return ((IOffsetState) initialState).terrain_slabs$getOnbottomState( level, pos, initialState );
        }

        return initialState;
    }

    static boolean shouldBeOntopState(BlockGetter level, BlockPos pos, BlockState state) {
        if ( state.getBlock() instanceof IFacedOffsetable) {
            return ((IOffsetState) state).terrain_slabs$isOffsetAbove();
        }
        return canGenerateOntop(level, pos, state);
    }

    static boolean canGenerateOntop(BlockGetter level, BlockPos pos, BlockState state) {
        if ( !ontopStateEnabled( state ) || !IConditionalOffset.allowOntop( level, pos, state ) ) {
            return false;
        }

        BlockState belowState = level.getBlockState( pos.below() );
        return ISlabCopy.isBottomSlab( belowState ) || ( (IOffsetState) belowState ).terrain_slabs$isOffsetAbove();
    }

    static boolean ontopStateEnabled(BlockState state ) {
        Block block = state.getBlock();
        if ( !((IOffsetState) state).terrain_slabs$hasOntopState() ) {
            return false;
        }

        return !PlatformConfigHooks.excludeOntop( block );

//        if ( OffsetClasses.isDefaultOntop( block ) ) {
//            return !PlatformConfigHooks.excludeOntop( block );
//        }
//
//        return false;
    }

    static boolean shouldBeOnbottomState(BlockGetter level, BlockPos pos, BlockState state) {
        if ( state.getBlock() instanceof IFacedOffsetable) {
            return ((IOffsetState) state).terrain_slabs$isOffsetBelow();
        }
        return canGenerateOnbottom(level, pos, state);
    }

    static boolean canGenerateOnbottom(BlockGetter level, BlockPos pos, BlockState state) {
        if ( !onbottomStateEnabled( state ) || !IConditionalOffset.allowOnbottom( level, pos, state ) ) {
            return false;
        }

        BlockState aboveState = level.getBlockState( pos.above() );
        return  ISlabCopy.isTopSlab( aboveState ) || ( (IOffsetState) aboveState ).terrain_slabs$isOffsetBelow();
    }

    static boolean onbottomStateEnabled(BlockState state ) {
        Block block = state.getBlock();
        if ( !((IOffsetState) state).terrain_slabs$hasOnbottomState() ) {
            return false;
        }

        return !PlatformConfigHooks.excludeOnbottom( block );

//        if ( OffsetClasses.isDefaultOnbottom( block ) ) {
//            return !PlatformConfigHooks.excludeOnbottom( block );
//        }
//
//        return false;
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
