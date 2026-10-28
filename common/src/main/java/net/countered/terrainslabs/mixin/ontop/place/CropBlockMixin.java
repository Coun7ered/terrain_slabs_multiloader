package net.countered.terrainslabs.mixin.ontop.place;

import net.countered.terrainslabs.config.TSConfig;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {

    /**
     * Allows Crops to generate and be placed on farmland slabs
     */
    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void allowPlacementOnSlabs(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(ModBlocksRegistry.FARMLAND_SLAB.get())) {
            if (!TSConfig.isVegetationOnSlabsEnabled()
                    || (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))) {
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(true);
            }
        }
    }
}
