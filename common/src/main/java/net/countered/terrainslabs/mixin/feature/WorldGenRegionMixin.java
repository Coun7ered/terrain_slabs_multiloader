package net.countered.terrainslabs.mixin.feature;

import net.countered.terrainslabs.block.ModSlabsMap;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {

    /**
     * fix for grass slabs on village paths
     */
    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At("HEAD"), cancellable = true)
    private void onSetBlock(BlockPos pos, BlockState state, int flags, int recursionLeft, CallbackInfoReturnable<Boolean> cir) {
        if (!state.is(Blocks.DIRT_PATH)) return;

        WorldGenRegion level = (WorldGenRegion)(Object) this;
        BlockState aboveState = level.getBlockState(pos.above());

        if (ModSlabsMap.SOIL_SLAB_BLOCKS.contains(aboveState.getBlock())) {
            cir.setReturnValue(level.setBlock(pos, Blocks.DIRT.defaultBlockState(), flags, recursionLeft));
            level.setBlock(pos.above(), ModBlocksRegistry.PATH_SLAB.get().defaultBlockState()
                    .setValue(BlockStateProperties.SLAB_TYPE, aboveState.getValue(BlockStateProperties.SLAB_TYPE))
                    .setValue(CustomSlab.GENERATED, true), flags);
        }
    }
}