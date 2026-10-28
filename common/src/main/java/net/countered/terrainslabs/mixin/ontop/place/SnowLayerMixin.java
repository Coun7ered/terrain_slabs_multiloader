package net.countered.terrainslabs.mixin.ontop.place;

import net.countered.terrainslabs.config.TSConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowLayerBlock.class)
public class SnowLayerMixin {

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void disableSnowOnSlabs(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = level.getBlockState(pos.below());
        if (blockState.is(BlockTags.SLABS)) {
            if (!TSConfig.isSnowOnSlabsEnabled()) {
                cir.setReturnValue(false);
            } else if (blockState.hasProperty(SlabBlock.WATERLOGGED) && blockState.getValue(SlabBlock.WATERLOGGED)) {
                cir.setReturnValue(false);
            }
        }
    }
}
