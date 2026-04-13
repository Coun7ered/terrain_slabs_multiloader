package net.countered.terrainslabs.mixin.ontop;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlockSodiumFix {

    @Inject(method = "hasDynamicShape", at = @At("HEAD"), cancellable = true)
    private void terrain_slabs$makeSnowDynamic(CallbackInfoReturnable<Boolean> cir) {
        Block block = (Block) (Object) this;
        if (block instanceof SnowLayerBlock) {
            cir.setReturnValue(true);
        }
    }
}
