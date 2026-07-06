package net.countered.terrainslabs.api;

import net.countered.terrainslabs.block.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;

/**
 * Class used to hold methods used for basic block offset behaviour.
 * <p>
 * These methods can be used by other mods' compat mixins (e.g. custom block
 * renderers) to match Terrain Slabs' own ontop checks, instead of duplicating them.
 * <p>
 * Ported from the 1.20.1 branch; on this branch the proxy methods for the
 * offset-state engine do not exist yet, so this exposes the eligibility checks
 * the ontop mixins use.
 */
public class SlabHelper {

    /**
     * Whether this state is eligible for the ontop visual offset, ignoring its
     * surroundings: it is in the {@code terrain_slabs:on_top_blocks} tag, or its
     * block's class is enabled through {@link OffsetClasses}.
     */
    public static boolean isValidOntop( BlockState state ) {
        return state.is( ModBlockTags.ON_TOP_BLOCKS )
                || OffsetClasses.isDefaultOntop( state.getBlock() );
    }

    /**
     * Position-aware version of {@link #isValidOntop(BlockState)} that also honours
     * {@link IConditionalOffset}, letting special-case blocks veto the offset.
     */
    public static <L extends BlockGetter> boolean isValidOntop( L level, BlockPos pos, BlockState state ) {
        return isValidOntop( state ) && IConditionalOffset.allowOntop( level, pos, state );
    }

    /**
     * Whether the state at {@code pos} is actually offset down onto a bottom slab:
     * it passes {@link #isValidOntop(BlockGetter, BlockPos, BlockState)} and the
     * block below it satisfies {@link #isOffsetBase(BlockState)}.
     */
    public static <L extends BlockGetter> boolean isOffsetOntop( L level, BlockPos pos, BlockState state ) {
        return isValidOntop( level, pos, state )
                && isOffsetBase( level.getBlockState( pos.below() ) );
    }

    /**
     * Whether {@code belowState} is a surface that ontop blocks visually offset
     * down onto: a bottom-type slab that is not waterlogged. Waterlogged slabs
     * are excluded so vegetation on slabs at the water's edge does not render
     * sunken into the water.
     */
    public static boolean isOffsetBase( BlockState belowState ) {
        return belowState.is( BlockTags.SLABS )
                && belowState.hasProperty( SlabBlock.TYPE )
                && belowState.getValue( SlabBlock.TYPE ) == SlabType.BOTTOM
                && !( belowState.hasProperty( BlockStateProperties.WATERLOGGED )
                        && belowState.getValue( BlockStateProperties.WATERLOGGED ) );
    }
}
