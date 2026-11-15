package net.countered.terrainslabs.mixin.world;

import net.countered.terrainslabs.block.customslabs.CustomSlab;
import net.countered.terrainslabs.config.TSConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowingFluid.class)
public abstract class FlowingFluidMixin {

    @Unique
    private static boolean terrain_slabs$isBreakableGeneratedSlab(BlockState state) {
        return state.getBlock() instanceof SlabBlock
                && state.hasProperty(CustomSlab.GENERATED)
                && state.getValue(CustomSlab.GENERATED)
                && state.hasProperty(SlabBlock.TYPE)
                && state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM
                && state.hasProperty(SlabBlock.WATERLOGGED)
                && !state.getValue(SlabBlock.WATERLOGGED);
    }

    /**
     * Allows flowing fluids to consider unwaterlogged generated bottom slabs as passable targets.
     */
    @Inject(method = "canHoldSpecificFluid", at = @At("HEAD"), cancellable = true)
    private static void terrain_slabs$onCanHoldSpecificFluid(
            BlockGetter level,
            BlockPos pos,
            BlockState state,
            Fluid fluid,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (TSConfig.doFluidsDestroyGeneration() && terrain_slabs$isBreakableGeneratedSlab(state)) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Replaces unwaterlogged generated bottom slabs silently with flowing fluid at any fluid level.
     */
    @Inject(method = "spreadTo", at = @At("HEAD"), cancellable = true)
    private void terrain_slabs$onSpreadTo(
            LevelAccessor level,
            BlockPos pos,
            BlockState blockState,
            Direction direction,
            FluidState fluidState,
            CallbackInfo ci
    ) {
        if (!TSConfig.doFluidsDestroyGeneration() || !terrain_slabs$isBreakableGeneratedSlab(blockState)) {
            return;
        }
        level.setBlockAndUpdate(pos, fluidState.createLegacyBlock());
        ci.cancel();
    }
}
