package net.countered.terrainslabs.forge.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.countered.terrainslabs.util.AOHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.lighting.ForgeModelBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeModelBlockRenderer.class)
public class MixinForgeModelBlockRenderer {

    @Inject(method = "tesselateWithAO", at = @At("HEAD"))
    private void terrain_slabs$setSnowFlag(
            BlockAndTintGetter level, BakedModel model, BlockState state,
            BlockPos pos, PoseStack poseStack, VertexConsumer vertexConsumer,
            boolean checkSides, RandomSource rand, long seed, int packedOverlay,
            ModelData modelData, RenderType renderType, CallbackInfo ci) {

        if (!(state.getBlock() instanceof SnowLayerBlock)) {
            AOHelper.SNOW_SLAB_POS.set(null);
            return;
        }
        BlockState belowState = level.getBlockState(pos.below());
        if (!belowState.is(BlockTags.SLABS)
                || !belowState.hasProperty(SlabBlock.TYPE)
                || belowState.getValue(SlabBlock.TYPE) != SlabType.BOTTOM) {
            AOHelper.SNOW_SLAB_POS.set(null);
            return;
        }

        AOHelper.SNOW_SLAB_POS.set(pos.below());
    }
}
