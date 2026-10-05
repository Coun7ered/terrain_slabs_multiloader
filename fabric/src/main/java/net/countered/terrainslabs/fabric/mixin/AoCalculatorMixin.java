package net.countered.terrainslabs.fabric.mixin;

import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.fabricmc.fabric.impl.client.indigo.renderer.aocalc.AoCalculator;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AoCalculator.class)
public abstract class AoCalculatorMixin {

    @Shadow
    private BlockAndTintGetter level;
    @Shadow private BlockState state;
    @Shadow private BlockPos pos;
    @Shadow @Final
    public float[] ao;

    @Inject(method = "compute", at = @At("TAIL"))
    private void terrainslabs$reduceAo(QuadViewImpl quad, boolean vanillaShade, CallbackInfo ci) {
        if (!state.is(BlockTags.SLABS)) return;

        Direction shadeDir = quad.shadeDirectionOverride() != null
                ? quad.shadeDirectionOverride()
                : quad.lightFace();
        float flat = level.cardinalLighting().byFace(shadeDir);

        for (int i = 0; i < 4; i++) {
            ao[i] += (flat - ao[i]) * PlatformConfigHooks.getSlabAoStrength();
        }
    }
}