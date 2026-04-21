package net.countered.terrainslabs.neoforge.mixin.compat;

import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.countered.terrainslabs.util.MixinHelper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockRenderer.class)
public class MixinSodiumBlockRenderer {

    @Redirect(
            method = "renderModel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;hasOffsetFunction()Z")
    )
    private boolean terrain_slabs$forceHasOffset(BlockState instance) {
        if (MixinHelper.terrain_slabs$isStateValidOnTop(instance)) {
            return true;
        }
        return instance.hasOffsetFunction();
    }
}