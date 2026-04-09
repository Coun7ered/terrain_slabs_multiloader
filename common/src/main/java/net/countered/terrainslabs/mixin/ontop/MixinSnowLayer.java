package net.countered.terrainslabs.mixin.ontop;

import com.llamalad7.mixinextras.sugar.Local;
import net.countered.platform.PlatformConfigHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowLayerBlock.class)
public class MixinSnowLayer {

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void disableSnowOnSlabs(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = level.getBlockState(pos.below());
        if (blockState.is(BlockTags.SLABS) && !PlatformConfigHooks.isSnowOnSlabsEnabled()) cir.setReturnValue(false);
    }
}
