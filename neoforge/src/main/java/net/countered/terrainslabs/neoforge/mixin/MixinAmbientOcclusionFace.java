package net.countered.terrainslabs.neoforge.mixin;

import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.countered.terrainslabs.util.AOHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;

@Mixin(targets = "net.minecraft.client.renderer.block.ModelBlockRenderer$AmbientOcclusionFace")
public abstract class MixinAmbientOcclusionFace {
    @Final
    @Shadow
    float[] brightness;

    @Inject(
        method = "calculate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;[FLjava/util/BitSet;Z)V",
        at = @At("RETURN")
    )
    private void terrain_slabs$applyAoDamping(BlockAndTintGetter level, BlockState state, BlockPos pos, Direction direction, float[] shape, BitSet shapeFlags, boolean shade, CallbackInfo ci) {
        boolean isSlab = state.is(BlockTags.SLABS);
        boolean isSnowSlab = AOHelper.SNOW_SLAB_POS.get() != null;

        if (direction == Direction.UP && (isSlab || isSnowSlab)) {
            float multiplier = PlatformConfigHooks.getAoStrength();
            for (int i = 0; i < 4; i++) {
                brightness[i] = 1.0f - (1.0f - brightness[i]) * multiplier;
            }
        }
    }
}