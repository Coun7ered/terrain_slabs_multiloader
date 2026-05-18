package net.countered.terrainslabs.mixin.ontop.place;

import net.countered.platform.PlatformConfigHooks;
import net.countered.terrainslabs.block.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BushBlock.class)
public class MixinBushBlock {

    /**
     * Allows placement of most vegetation on slabs for player and worldgen
     */
    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void terrain_slabs$mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(ModBlockTags.DIRT_SLABS) &&
                !state.getValue(BlockStateProperties.WATERLOGGED) &&
                PlatformConfigHooks.isVegetationOnSlabsEnabled())
        {
            cir.setReturnValue(true);
        }
    }
}
