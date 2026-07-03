package net.countered.terrainslabs.mixin.ontop.place;

import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.countered.terrainslabs.block.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BushBlock.class)
public class MixinBushBlock {

    /**
     * Allows placement of most vegetation on slabs for player and worldgen.
     */
    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void terrain_slabs$mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if ((state.is(ModBlockTags.DIRT_SLABS) || state.is(ModBlockTags.SAND_SLABS)) &&
                !state.getValue(BlockStateProperties.WATERLOGGED) &&
                PlatformConfigHooks.isVegetationOnSlabsEnabled() &&
                !((Object) this instanceof SaplingBlock))
        {
            cir.setReturnValue(true);
        }
    }

    /**
     * Some plants (notably many from other mods, e.g. Biomes We've Gone)
     * override {@code mayPlaceOn} without calling super, which means the
     * injection above never runs for them. Those plants still inherit
     * {@link BushBlock#canSurvive}, so we apply the same allowance here: if the
     * block directly below is a dirt-type slab, vegetation may survive on it,
     * subject to the same gates (config on, not waterlogged, not a sapling).
     */
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void terrain_slabs$canSurvive(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState below = level.getBlockState(pos.below());
        if ((below.is(ModBlockTags.DIRT_SLABS) || below.is(ModBlockTags.SAND_SLABS)) &&
                !below.getValue(BlockStateProperties.WATERLOGGED) &&
                PlatformConfigHooks.isVegetationOnSlabsEnabled() &&
                !((Object) this instanceof SaplingBlock))
        {
            cir.setReturnValue(true);
        }
    }
}
