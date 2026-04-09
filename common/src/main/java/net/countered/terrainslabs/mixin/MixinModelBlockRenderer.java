package net.countered.terrainslabs.mixin;

import net.countered.terrainslabs.AOHelper;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ModelBlockRenderer.class)
public class MixinModelBlockRenderer {

    @ModifyArg(
            method = "renderModelFaceAO",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer$AmbientOcclusionFace;calculate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;[FLjava/util/BitSet;Z)V"
            ),
            index = 2
    )
    private BlockPos terrain_slabs$fixAOPos(BlockPos pos) {
        BlockPos override = AOHelper.SNOW_SLAB_POS.get();
        return override != null ? override : pos;
    }

    @ModifyArg(
            method = "renderModelFaceAO",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer$AmbientOcclusionFace;calculate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;[FLjava/util/BitSet;Z)V"
            ),
            index = 4
    )
    private float[] terrain_slabs$fixAOShape(float[] shape) {
        if (AOHelper.SNOW_SLAB_POS.get() == null) return shape;

        int down = Direction.DOWN.get3DDataValue();
        int up = Direction.UP.get3DDataValue();
        int len = Direction.values().length;

        shape[down] += 0.5f;
        shape[up] += 0.5f;
        shape[down + len] -= 0.5f;
        shape[up + len] -= 0.5f;

        return shape;
    }
}