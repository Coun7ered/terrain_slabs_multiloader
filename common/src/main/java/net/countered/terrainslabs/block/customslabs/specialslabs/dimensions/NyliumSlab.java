package net.countered.terrainslabs.block.customslabs.specialslabs.dimensions;

import net.countered.terrainslabs.block.interfaces.IDuelSlab;
import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.lighting.LightEngine;

final public class NyliumSlab extends CustomSlab implements BonemealableBlock, IDuelSlab {

    public NyliumSlab(Block block) {
        super(block);
    }

    @Override
    public ISlabCopy getDuel() {
        return (ISlabCopy) ModBlocksRegistry.NETHERRACK_SLAB.get();
    }

    private static boolean canBeNylium(BlockState state, LevelReader reader, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState blockState = reader.getBlockState(blockPos);
        int i = LightEngine.getLightBlockInto(reader, Blocks.WARPED_NYLIUM.defaultBlockState(), pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(reader, blockPos));
        return i < reader.getMaxLightLevel();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeNylium(state, level, pos)) {
            if (state.getValue(TYPE) == SlabType.TOP) {
                level.setBlockAndUpdate(pos, ModBlocksRegistry.NETHERRACK_SLAB.get().defaultBlockState().setValue(TYPE, SlabType.TOP));
            }
            else if (state.getValue(TYPE) == SlabType.DOUBLE) {
                level.setBlockAndUpdate(pos, ModBlocksRegistry.NETHERRACK_SLAB.get().defaultBlockState().setValue(TYPE, SlabType.DOUBLE));
            }
            else if (state.getValue(TYPE) == SlabType.BOTTOM){
                level.setBlockAndUpdate(pos, ModBlocksRegistry.NETHERRACK_SLAB.get().defaultBlockState().setValue(TYPE, SlabType.BOTTOM));
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {}
}
