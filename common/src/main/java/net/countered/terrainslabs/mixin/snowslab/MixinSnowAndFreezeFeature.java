package net.countered.terrainslabs.mixin.snowslab;

import net.countered.terrainslabs.snowslab.SnowSlabBlock;
import net.countered.terrainslabs.snowslab.SnowSlabRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Late worldgen pass on vanilla's "freeze_top_layer" step (TOP_LAYER_MODIFICATION),
 * done entirely at TAIL - AFTER vanilla has placed all ground snow in the chunk. Two
 * jobs, in order:
 *
 * 1) FILL-IN: snow-cap any remaining bare eligible slab that now has a snowy FULL-BLOCK
 *    neighbor (neighbor snow is fully present at this point). Slabs are never counted
 *    as neighbors, so there's no slab->slab chain.
 *
 * 2) CLEANUP: remove any vanilla snow layer that ended up sitting directly on top of a
 *    snow slab (our slabs already carry their own snow). Running this AFTER the fill-in
 *    also cleans up over slabs that only just became snow slabs in step 1.
 *
 * (Previously step 2 was an inline @Redirect; moved here so it also covers slabs capped
 *  during this same late pass.)
 */
@Mixin(SnowAndFreezeFeature.class)
public abstract class MixinSnowAndFreezeFeature {

    @Inject(method = "place", at = @At("TAIL"))
    private void lateSnowSlabPass(FeaturePlaceContext<NoneFeatureConfiguration> ctx,
                                  CallbackInfoReturnable<Boolean> cir) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();
        BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int wx = origin.getX() + dx;
                int wz = origin.getZ() + dz;
                int topY = level.getHeight(Heightmap.Types.WORLD_SURFACE, wx, wz);

                // 1) FILL-IN: cap a bare eligible slab with a snowy full-block neighbor.
                for (int y = topY; y >= topY - 1; y--) {
                    m.set(wx, y, wz);
                    if (SnowSlabBlock.isWorldgenSnowCapCandidate(level, m)
                            && SnowSlabBlock.hasSnowyFullBlockNeighbor(level, m)) {
                        SnowSlabBlock.formOnSlabWorldgen(level, m.immutable(),
                                level.getBlockState(m));
                        break;
                    }
                }

                // 2) CLEANUP: remove a vanilla snow layer sitting on top of a snow slab.
                for (int y = topY + 1; y >= topY - 1; y--) {
                    m.set(wx, y, wz);
                    if (level.getBlockState(m).is(Blocks.SNOW)) {
                        BlockState below = level.getBlockState(m.below());
                        if (below.is(SnowSlabRegistry.SNOW_SLAB.get())) {
                            level.setBlock(m.immutable(), Blocks.AIR.defaultBlockState(),
                                    Block.UPDATE_CLIENTS);
                        }
                    }
                }
            }
        }
    }
}
