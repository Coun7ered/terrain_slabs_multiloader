package net.countered.terrainslabs.fabric.mixin;

import net.countered.platform.PlatformConfigHooks;
import net.countered.terrainslabs.util.AOHelper;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.fabricmc.fabric.impl.client.indigo.renderer.aocalc.AoCalculator", remap = false)
public abstract class MixinAoCalculator {

    @Final @Shadow protected net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo blockInfo;
    @Final @Shadow public float[] ao;

    @Inject(method = "compute", at = @At("HEAD"), remap = false)
    private void terrain_slabs$setupSnowAO(MutableQuadViewImpl quad, boolean isVanilla, CallbackInfo ci) {
        BlockState state = blockInfo.blockState;

        if (state.getBlock() instanceof SnowLayerBlock) {
            BlockState belowState = blockInfo.blockView.getBlockState(blockInfo.blockPos.below());

            if (belowState.is(BlockTags.SLABS) && belowState.hasProperty(SlabBlock.TYPE) && belowState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
                AOHelper.SNOW_SLAB_POS.set(blockInfo.blockPos.below());
                blockInfo.blockPos = blockInfo.blockPos.below();

                for (int i = 0; i < 4; i++) {
                    quad.pos(i, quad.x(i), quad.y(i) + 0.5f, quad.z(i));
                }
                return;
            }
        }
        AOHelper.SNOW_SLAB_POS.set(null);
    }

    @Inject(method = "compute", at = @At("RETURN"), remap = false)
    private void terrain_slabs$handlePostAO(MutableQuadViewImpl quad, boolean isVanilla, CallbackInfo ci) {
        BlockState state = blockInfo.blockState;
        boolean isSlab = state.is(BlockTags.SLABS);
        boolean isSnow = state.getBlock() instanceof SnowLayerBlock;

        if (isSnow && AOHelper.SNOW_SLAB_POS.get() != null) {
            blockInfo.blockPos = AOHelper.SNOW_SLAB_POS.get().above();
            for (int i = 0; i < 4; i++) {
                quad.pos(i, quad.x(i), quad.y(i) - 0.5f, quad.z(i));
            }
        }

        if ((isSlab || isSnow) && quad.lightFace() == Direction.UP) {
            float multiplier = PlatformConfigHooks.getAoStrength();

            for (int i = 0; i < 4; i++) {
                ao[i] = 1.0f - (1.0f - ao[i]) * multiplier;
            }
        }
    }
}