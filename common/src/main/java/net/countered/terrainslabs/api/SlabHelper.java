package net.countered.terrainslabs.api;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Class holds methods used for basic block offset behaviour.
 * <p>
 * These methods can be used like in "MixinBlocks" to add classes for compatibility.
 */
public final class SlabHelper {


    //===============//
    // Proxy Methods //
    //===============//


    public static BlockState terrain_slabs$convertBlockState(
            LevelReader instance, BlockPos offPos, Operation<BlockState> original,
            BlockState state, LevelReader level, BlockPos pos
    ) {
        BlockState stateAtOffset = original.call( instance, offPos );
        if ( !( stateAtOffset.getBlock() instanceof ISlabCopy ) || (
                 skipModifyAbove( offPos, state, stateAtOffset, pos )
                 && skipModifyBelow( offPos, state, stateAtOffset, pos )
        )) {
            return stateAtOffset;
        }

        return ISlabCopy.getOriginState( stateAtOffset );
    }

    public static void terrain_slabs$offsetParticles(
            Level instance, ParticleOptions particleData,
            double x, double y, double z,
            double xSpeed, double ySpeed, double zSpeed,
            Operation<Void> original,
            BlockState state, Level level, BlockPos pos, RandomSource random
    ) {
        original.call( instance, particleData, x,
                y + state.getOffset( instance, pos ).y(),
                z, xSpeed, ySpeed, zSpeed );
    }

    public static boolean terrain_slabs$slabsSupportCenter(
            LevelReader instance, BlockPos offsetPos, Direction direction, Operation<Boolean> original,
            BlockState state, LevelReader level, BlockPos pos
    ) {
        boolean origOutput = original.call( instance, offsetPos, direction );
        return terrain_slabs$slabsSupportGeneric( instance, offsetPos, direction, origOutput, state, level, pos);
    }

    public static boolean terrain_slabs$slabsSupportGeneric(
            LevelReader instance, BlockPos offsetPos, Direction direction, boolean origOutput,
            BlockState state, LevelReader level, BlockPos pos
    ) {
        BlockState stateAtOffset = instance.getBlockState( offsetPos );

        return origOutput || ( stateAtOffset.getBlock() instanceof ISlabCopy ) && (
                direction == Direction.UP && !skipModifyAbove( offsetPos, state, stateAtOffset, pos )
                || direction == Direction.DOWN && !skipModifyBelow( offsetPos, state, stateAtOffset, pos ));
    }


    //================//
    // Helper Methods //
    //================//


    // TODO: Implement waterlogged solution with better vanilla parity (allow place, break if fluid fills)
    // True if an ISlabCopy instance should not pretend to be its original block for Ontop purposes (offset or not)
    private static boolean skipModifyAbove(BlockPos offPos, BlockState targetState, BlockState stateAtOffset, BlockPos pos ) {

        // Check if this is actually the block below the placement position
        return !( offPos.getX() == pos.getX() && offPos.getZ() == pos.getZ() && offPos.getY() == pos.getY() - 1 )

                // Checks for if this is a bottom slab (offset conditions)
                || ( ISlabCopy.isBottomSlab( stateAtOffset )

                // Check if this block can be offset onto a slab if present
                        && ( !IOffsetState.ontopStateEnabled( targetState )

                // Check if this could be waterlogged to place in shallow water if water is present
                        || ( stateAtOffset.getValue( WATERLOGGED ) && !targetState.hasProperty( WATERLOGGED ) )
        ));
    }

    // True if an ISlabCopy instance should not pretend to be its original block for Onbottom purposes (offset or not)
    private static boolean skipModifyBelow(BlockPos offPos, BlockState targetState, BlockState stateAtOffset, BlockPos pos ) {

        // Check if this is actually the block below the placement position
        return !( offPos.getX() == pos.getX() && offPos.getZ() == pos.getZ() && offPos.getY() == pos.getY() + 1 )

                // Check if this block can be offset onto a slab if present
                || (ISlabCopy.isTopSlab( stateAtOffset ) && !IOffsetState.onbottomStateEnabled( targetState ));
    }
}
