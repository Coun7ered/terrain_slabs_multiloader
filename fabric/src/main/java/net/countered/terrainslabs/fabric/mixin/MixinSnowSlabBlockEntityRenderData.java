package net.countered.terrainslabs.fabric.mixin;

import net.countered.terrainslabs.snowslab.SnowSlabBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fabric-only: provide the stored bottom slab as the block entity's render data, so
 * SnowSlabFabricModel can read it via BlockAndTintGetter#getBlockEntityRenderData.
 *
 * getRenderData() is added to BlockEntity by Fabric API, so this override lives in
 * the Fabric source set (it can't go in common, which has no Fabric API).
 */
@Mixin(SnowSlabBlockEntity.class)
public abstract class MixinSnowSlabBlockEntityRenderData {

    @Inject(method = "getRenderData", at = @At("HEAD"), cancellable = true, remap = false)
    private void provideStoredSlab(CallbackInfoReturnable<Object> cir) {
        cir.setReturnValue(((SnowSlabBlockEntity) (Object) this).getBottomSlab());
    }
}
