package net.countered.terrainslabs.generation;

import com.mojang.serialization.Codec;
import net.countered.platform.PlatformConfigHooks;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Optional;
import java.util.Stack;

public class OffsetFeature extends Feature<NoneFeatureConfiguration> {

    public OffsetFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place( FeaturePlaceContext<NoneFeatureConfiguration> context ) {
        if ( !PlatformConfigHooks.isSlabGenerationEnabled()
                || ( !PlatformConfigHooks.isSnowOnSlabsEnabled() && !PlatformConfigHooks.isVegetationOnSlabsEnabled() )
        ) {
            return false;
        }

        ChunkAccess chunk = context.level().getChunk( context.origin() );
        Optional<Stack<BlockPos>> slabs = FeatureUtil.BlockPosCache.popChunk( chunk );
        if ( slabs.isPresent() ) {
            fixAllOffsetsCached( slabs.get(), context );
        } else {
            fixAllOffsets( context );
        }
        return true;
    }

    private void fixAllOffsetsCached( Stack<BlockPos> stack, FeaturePlaceContext<NoneFeatureConfiguration> context ) {
        while ( !stack.isEmpty() ) {
            BlockPos pos = stack.pop();
            WorldGenLevel level = context.level();
            BlockState state = context.level().getBlockState( pos );

            if ( ( state.getBlock() instanceof SlabBlock ) ) {
                FeatureUtil.iterateAbove( context.level(), pos.above(), level.getMaxBuildHeight(),
                        ( aPos, aState ) -> offsetState( level, aState, aPos )
                );
            }
        }
    }

    // This is a fallback method (not preferred)
    private void fixAllOffsets( FeaturePlaceContext<NoneFeatureConfiguration> context ) {
        WorldGenLevel level = context.level();
        FeatureUtil.iterateChunkBlocks( level, context.origin(), ( pos, maxY ) -> {
            BlockState state = level.getBlockState( pos );

            if ( ( state.getBlock() instanceof SlabBlock ) ) {
                FeatureUtil.iterateAbove( context.level(), pos.above(), level.getMaxBuildHeight(),
                        ( aPos, aState ) -> offsetState( level, aState, aPos )
                );
            }
        } );
    }

    private boolean offsetState( WorldGenLevel level, BlockState state, BlockPos pos ) {
        return ( (IOffsetState) state ).terrain_slabs$hasOffsetState()
                && level.setBlock( pos, ((IOffsetState) state ).terrain_slabs$getOffsetState(), Block.UPDATE_CLIENTS );
    }
}
