package net.countered.terrainslabs.forge.mixin.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin( BlockRenderer.class )
public class MixinEmbeddiumBlockRenderer {

    // TODO: Replace with wrapOperation for stability
    @Redirect( method = "renderModel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;hasOffsetFunction()Z")
    )
    private boolean terrain_slabs$forceHasOffset( BlockState instance ) {
        if ( ((IOffsetState) instance ).terrain_slabs$hasOffsetState() ) {
            return true;
        }

        return instance.hasOffsetFunction();
    }
}