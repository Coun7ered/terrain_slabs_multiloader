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
                 skipModifyOntop( offPos, state, stateAtOffset, pos )
                 && skipModifyOnbottom( offPos, state, stateAtOffset, pos )
        )) {
            return stateAtOffset;
        }

        return ISlabCopy.getOriginState( stateAtOffset );
    }

    public static boolean terrain_slabs$slabsSupportCenter(
            LevelReader instance, BlockPos offsetPos, Direction direction, Operation<Boolean> original,
            BlockState state, LevelReader level, BlockPos pos
    ) {
        boolean origOutput = original.call( instance, offsetPos, direction );
        BlockState offsetState = instance.getBlockState( offsetPos );

        return origOutput || ( direction == Direction.UP && !skipModifyOntop( offsetPos, state, offsetState, pos ) )
                || ( direction == Direction.DOWN && !skipModifyOnbottom( offsetPos, state, offsetState, pos ) );
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

    // API to assist with uncommon method use
    public static boolean terrain_slabs$slabsSupportGeneric(
            LevelReader instance, BlockPos offsetPos, Direction direction, boolean origOutput,
            BlockState state, LevelReader level, BlockPos pos
    ) {
        BlockState offsetState = instance.getBlockState( offsetPos );

        return origOutput || ( direction == Direction.UP && !skipModifyOntop( offsetPos, state, offsetState, pos ) )
                || ( direction == Direction.DOWN && !skipModifyOnbottom( offsetPos, state, offsetState, pos ) );
    }


    //================//
    // Helper Methods //
    //================//


    // True if plant cannot be placed on top (offset or not)
    private static boolean skipModifyOntop( BlockPos offPos, BlockState targetState, BlockState stateAtOffset, BlockPos pos ) {
        return !( offPos.getX() == pos.getX() && offPos.getZ() == pos.getZ() && offPos.getY() == pos.getY() - 1 )
                || ( ISlabCopy.isBottomSlab( stateAtOffset ) && !IOffsetState.ontopStateEnabled( targetState ));
    }

    // True if plant cannot be placed on bottom (offset or not)
    private static boolean skipModifyOnbottom( BlockPos offPos, BlockState targetState, BlockState stateAtOffset, BlockPos pos ) {
        return !( offPos.getX() == pos.getX() && offPos.getZ() == pos.getZ() && offPos.getY() == pos.getY() + 1 )
                || (ISlabCopy.isTopSlab( stateAtOffset ) && !IOffsetState.onbottomStateEnabled( targetState ));
    }
}
