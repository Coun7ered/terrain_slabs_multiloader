package net.countered.terrainslabs.mixin.ontop.place;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Bonemealing short seagrass on a slab grows tall seagrass, a separate
 * {@link TallSeagrassBlock} whose {@code mayPlaceOn} is not covered by
 * {@code MixinSeagrassBlock} (that targets only the short
 * {@code SeagrassBlock}). Without this, the tall seagrass fails its placement
 * check on the slab and breaks immediately. This mirrors the short-seagrass
 * allowance so tall seagrass may also sit on slabs.
 */
@Mixin(TallSeagrassBlock.class)
public class MixinTallSeagrassBlock {

    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void allowPlacementOnSlabs(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(BlockTags.SLABS)) {
            cir.setReturnValue(true);
        }
    }
}
