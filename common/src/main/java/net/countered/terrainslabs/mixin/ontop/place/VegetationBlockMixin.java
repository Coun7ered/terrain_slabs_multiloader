package net.countered.terrainslabs.mixin.ontop.place;

import net.countered.terrainslabs.block.ModBlockTags;
import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.util.OnTopHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VegetationBlock.class)
public abstract class VegetationBlockMixin {

    @Shadow
    protected abstract boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos);

    /**
     * Method for allowing on top blocks to survive on slabs
     */
    @Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
    private void canSurvive(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (OnTopHelper.terrain_slabs$isStateValidOnTop(state) && PlatformConfigHooks.isVegetationOnSlabsEnabled() && mayPlaceOn(state, level, pos.below())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void terrain_slabs$mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if ((state.is(ModBlockTags.DIRT_SLABS) || state.is(ModBlocksRegistry.FARMLAND_SLAB)) && !state.getValue(BlockStateProperties.WATERLOGGED)) {
            cir.setReturnValue(true);
        }
    }
}
