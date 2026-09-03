package net.countered.terrainslabs.mixin.blocks;

import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpreadingSnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpreadingSnowyBlock.class)
public abstract class SpreadingSnowyDirtBlockMixin {

    @Invoker("canPropagate")
    private static boolean callCanPropagate(BlockState state, net.minecraft.world.level.LevelReader level, BlockPos pos) {
        throw new AssertionError();
    }

    @Redirect(
            method = "randomTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z")
    )
    private boolean redirectDirtCheck(BlockState targetState, Object o) {
        if ((Block) o == Blocks.DIRT) {
            return targetState.is(Blocks.DIRT) || targetState.is(ModBlocksRegistry.DIRT_SLAB.get());
        }
        return targetState.is((Block) o);
    }

    @Redirect(
            method = "randomTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private boolean injectSlabSpreading(ServerLevel level, BlockPos targetPos, BlockState vanillaNewState) {
        BlockState currentTargetState = level.getBlockState(targetPos);

        if (currentTargetState.is(ModBlocksRegistry.DIRT_SLAB.get())) {
            BlockState targetSlabState = vanillaNewState;

            if (targetSlabState.is(Blocks.GRASS_BLOCK)) {
                targetSlabState = ModBlocksRegistry.GRASS_SLAB.get().defaultBlockState();
            } else if (targetSlabState.is(Blocks.MYCELIUM)) {
                targetSlabState = ModBlocksRegistry.MYCELIUM_SLAB.get().defaultBlockState();
            } else {
                return false;
            }

            targetSlabState = targetSlabState
                    .setValue(BlockStateProperties.SLAB_TYPE, currentTargetState.getValue(BlockStateProperties.SLAB_TYPE))
                    .setValue(BlockStateProperties.WATERLOGGED, currentTargetState.getValue(BlockStateProperties.WATERLOGGED))
                    .setValue(BlockStateProperties.SNOWY, vanillaNewState.getValue(BlockStateProperties.SNOWY));

            if (callCanPropagate(targetSlabState, level, targetPos)) {
                return level.setBlockAndUpdate(targetPos, targetSlabState);
            }
            return false;
        }
        return level.setBlockAndUpdate(targetPos, vanillaNewState);
    }
}