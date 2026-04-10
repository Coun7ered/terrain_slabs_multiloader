package net.countered.terrainslabs.fabric.mixin;

import net.countered.terrainslabs.util.AOHelper;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
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
public class MixinAoCalculator {

    @Final
    @Shadow
    protected net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo blockInfo;

    @Inject(method = "compute", at = @At("HEAD"), remap = false)
    private void terrain_slabs$setupSnowAO(MutableQuadViewImpl quad, boolean isVanilla, CallbackInfo ci) {
        if (!(blockInfo.blockState.getBlock() instanceof SnowLayerBlock)) {
            AOHelper.SNOW_SLAB_POS.set(null);
            return;
        }
        BlockState belowState = blockInfo.blockView.getBlockState(blockInfo.blockPos.below());
        if (!belowState.is(BlockTags.SLABS)
                || !belowState.hasProperty(SlabBlock.TYPE)
                || belowState.getValue(SlabBlock.TYPE) != SlabType.BOTTOM) {
            AOHelper.SNOW_SLAB_POS.set(null);
            return;
        }
        AOHelper.SNOW_SLAB_POS.set(blockInfo.blockPos.below());
        blockInfo.blockPos = blockInfo.blockPos.below();
        for (int i = 0; i < 4; i++) {
            quad.pos(i, quad.x(i), quad.y(i) + 0.5f, quad.z(i));
        }
    }

    @Inject(method = "compute", at = @At("RETURN"), remap = false)
    private void terrain_slabs$restoreSnowAO(MutableQuadViewImpl quad, boolean isVanilla, CallbackInfo ci) {
        if (AOHelper.SNOW_SLAB_POS.get() == null) return;
        blockInfo.blockPos = AOHelper.SNOW_SLAB_POS.get().above();
        for (int i = 0; i < 4; i++) {
            quad.pos(i, quad.x(i), quad.y(i) - 0.5f, quad.z(i));
        }
    }
}