package net.countered.terrainslabs.mixin.ontop.place;

import net.countered.terrainslabs.block.ModBlockTags;
import net.countered.terrainslabs.config.TSConfig;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.util.OnTopHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.SlabBlock;
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
     * Veto survival on slabs if disabled or waterlogged (unless water vegetation like seagrass)
     */
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void checkSlabSurvival(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        if (belowState.is(BlockTags.SLABS)) {
            if (!TSConfig.isVegetationOnSlabsEnabled()) {
                cir.setReturnValue(false);
            } else if (belowState.hasProperty(SlabBlock.WATERLOGGED)
                    && belowState.getValue(SlabBlock.WATERLOGGED)
                    && !((Object) this instanceof SeagrassBlock)) {
                cir.setReturnValue(false);
            }
        }
    }

    /**
     * Method for allowing on top blocks to survive on slabs
     */
    @Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
    private void canSurvive(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && OnTopHelper.isStateValidOnTop(state)) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            if (belowState.is(BlockTags.SLABS) && mayPlaceOn(belowState, level, belowPos)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void terrain_slabs$mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(ModBlockTags.DIRT_SLABS) || state.is(ModBlocksRegistry.FARMLAND_SLAB.get())) {
            if (!TSConfig.isVegetationOnSlabsEnabled()
                    || (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))) {
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(true);
            }
        }
    }
}
