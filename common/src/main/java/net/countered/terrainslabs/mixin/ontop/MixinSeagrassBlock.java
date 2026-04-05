package net.countered.terrainslabs.mixin.ontop;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SeagrassBlock.class)
public class MixinSeagrassBlock {

    /**
     * Allows Seagrass to generate and be placed on slabs
     */
    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void allowPlacementOnSlabs(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(BlockTags.SLABS)) {
            cir.setReturnValue(true);
        }
    }
}
