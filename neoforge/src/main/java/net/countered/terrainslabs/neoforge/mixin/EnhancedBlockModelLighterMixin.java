package net.countered.terrainslabs.neoforge.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.QuadInstance;
import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.ao.EnhancedBlockModelLighter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EnhancedBlockModelLighter.class)
public abstract class EnhancedBlockModelLighterMixin extends BlockModelLighter {

    @WrapMethod(method = "prepareQuadAmbientOcclusion")
    private void terrainslabs$reduceAo(BlockAndTintGetter level, BlockState state, BlockPos pos,
                                       BakedQuad quad, QuadInstance out, Operation<Void> original) {
        original.call(level, state, pos, quad, out);

        if (!state.is(BlockTags.SLABS)) return;

        QuadInstance flat = new QuadInstance();
        this.prepareQuadFlat(level, state, pos, -1, quad, flat);

        for (int i = 0; i < 4; i++) {
            out.setColor(i, terrainslabs$lerpGray(out.getColor(i), flat.getColor(i), PlatformConfigHooks.getSlabAoStrength()));
        }
    }

    @Unique
    private static int terrainslabs$lerpGray(int ao, int flat, float t) {
        int a = ao & 0xFF;
        int f = flat & 0xFF;
        return ARGB.gray(Math.round(a + (f - a) * t) / 255f);
    }
}