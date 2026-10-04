package net.countered.terrainslabs.mixin.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpreadingSnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpreadingSnowyBlock.class)
public abstract class SpreadingSnowyDirtBlockMixin {

    @Invoker("canPropagate")
    private static boolean callCanPropagate(BlockState state, LevelReader level, BlockPos pos) {
        throw new AssertionError();
    }

    @WrapOperation(
            method = "randomTick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z")
    )
    private boolean dirtCheck(BlockState instance, Object o, Operation<Boolean> original) {
        if ((Block) o == Blocks.DIRT) {
            return original.call(instance, o) || instance.is(ModBlocksRegistry.DIRT_SLAB.get());
        }
        return original.call(instance, o);
    }

    @WrapOperation(
            method = "randomTick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
                    ordinal = 1)
    )
    private boolean slabSpreading(ServerLevel level, BlockPos targetPos, BlockState vanillaNewState,
                                  Operation<Boolean> original) {
        BlockState current = level.getBlockState(targetPos);

        if (current.is(ModBlocksRegistry.DIRT_SLAB.get())) {
            BlockState slab;
            if (vanillaNewState.is(Blocks.GRASS_BLOCK)) {
                slab = ModBlocksRegistry.GRASS_SLAB.get().defaultBlockState();
            } else if (vanillaNewState.is(Blocks.MYCELIUM)) {
                slab = ModBlocksRegistry.MYCELIUM_SLAB.get().defaultBlockState();
            } else {
                return false;
            }

            slab = slab
                    .setValue(BlockStateProperties.SLAB_TYPE, current.getValue(BlockStateProperties.SLAB_TYPE))
                    .setValue(BlockStateProperties.WATERLOGGED, current.getValue(BlockStateProperties.WATERLOGGED))
                    .setValue(BlockStateProperties.SNOWY, vanillaNewState.getValue(BlockStateProperties.SNOWY));

            return callCanPropagate(slab, level, targetPos)
                    ? original.call(level, targetPos, slab)
                    : false;
        }
        return original.call(level, targetPos, vanillaNewState);
    }
}