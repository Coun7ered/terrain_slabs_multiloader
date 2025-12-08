package net.countered.terrainslabs.block.customslabs.specialslabs.dimensions;

import net.countered.terrainslabs.block.ModBlocksRegistry;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.SlabType;

public class NetherrackSlab extends CustomSlab implements BonemealableBlock {

    public NetherrackSlab(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED, GENERATED);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        if (!level.getBlockState(pos.above()).propagatesSkylightDown(level, pos)) {
            return false;
        } else {
            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                if (level.getBlockState(blockPos).is(BlockTags.NYLIUM)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        boolean hasWarped = false;
        boolean hasCrimson = false;

        // Check neighboring blocks for nylium
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(Blocks.WARPED_NYLIUM) || blockState.is(ModBlocksRegistry.WARPED_NYLIUM_SLAB.get())) {
                hasWarped = true;
            }

            if (blockState.is(Blocks.CRIMSON_NYLIUM) || blockState.is(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get())) {
                hasCrimson = true;
            }

            if (hasWarped && hasCrimson) {
                break;
            }
        }

        // Determine the new state based on neighboring nylium
        BlockState newState = null;
        if (hasWarped && hasCrimson) {
            newState = random.nextBoolean() ? ModBlocksRegistry.WARPED_NYLIUM_SLAB.get().defaultBlockState() : ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get().defaultBlockState();
        } else if (hasWarped) {
            newState = ModBlocksRegistry.WARPED_NYLIUM_SLAB.get().defaultBlockState();
        } else if (hasCrimson) {
            newState = ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get().defaultBlockState();
        }

        // If a new state was determined, copy the slab type and update the block
        if (newState != null) {
            SlabType slabType = state.getValue(SlabBlock.TYPE); // Get current slab type
            newState = newState.setValue(SlabBlock.TYPE, slabType); // Apply it to the new state
            level.setBlock(pos, newState, 3);
        }
    }
}
