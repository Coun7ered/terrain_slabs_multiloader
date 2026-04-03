package net.countered.terrainslabs.block.customslabs.soilslabs;

import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;


public class GrassSlab extends CustomSlab {
    public static final BooleanProperty SNOWY;
    static {
        SNOWY = BlockStateProperties.SNOWY;
    }

    public GrassSlab(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(SNOWY, false)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        if (state.getValue(TYPE) == SlabType.DOUBLE) {
            super.spawnDestroyParticles(level, player, pos, Blocks.DIRT.defaultBlockState());
        }
        else if (state.getValue(TYPE) == SlabType.TOP) {
            super.spawnDestroyParticles(level, player, pos, ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState().setValue(TYPE, SlabType.TOP));
        }
        else {
            super.spawnDestroyParticles(level, player, pos, ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState());
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP) {
            state = state.setValue(SNOWY, isSnow(neighborState));
        }

        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        if (blockState.is(this)) {
            return blockState.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, false);
        }
        BlockState blockAbove = context.getLevel().getBlockState(blockPos.above());
        BlockState defaultState = this.defaultBlockState().setValue(SNOWY, isSnow(blockAbove));

        FluidState fluidState = context.getLevel().getFluidState(blockPos);
        BlockState blockState2 = defaultState.setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        Direction direction = context.getClickedFace();
        return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - blockPos.getY() > 0.5))
                    ? blockState2
                    : blockState2.setValue(TYPE, SlabType.TOP);

    }

    private static boolean isSnow(BlockState state) {
        return state.is(BlockTags.SNOW);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SNOWY, TYPE, WATERLOGGED, GENERATED);
    }

    private static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState blockState = levelReader.getBlockState(blockPos);
        if (blockState.is(Blocks.SNOW) && (Integer)blockState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockState.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(levelReader, Blocks.GRASS_BLOCK.defaultBlockState(), pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(levelReader, blockPos));
            return i < levelReader.getMaxLightLevel();
        }
    }

    private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.above();
        return canBeGrass(state, level, pos) && !level.getFluidState(blockPos).is(FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            if (state.getValue(TYPE) == SlabType.TOP) {
                level.setBlockAndUpdate(pos, ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState().setValue(TYPE, SlabType.TOP));
            }
            else if (state.getValue(TYPE) == SlabType.DOUBLE) {
                level.setBlockAndUpdate(pos, ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState().setValue(TYPE, SlabType.DOUBLE));
            }
            else if (state.getValue(TYPE) == SlabType.BOTTOM){
                level.setBlockAndUpdate(pos, ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState().setValue(TYPE, SlabType.BOTTOM));
            }
        } else {
            if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                BlockState blockState = this.defaultBlockState().setValue(TYPE, level.getBlockState(pos).getValue(TYPE));

                for (int i = 0; i < 4; i++) {
                    BlockPos blockPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (level.getBlockState(blockPos).is(ModBlocksRegistry.DIRT_SLAB.get()) && canPropagate(blockState, level, blockPos)) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(SNOWY, level.getBlockState(blockPos.above()).is(Blocks.SNOW)).setValue(TYPE, level.getBlockState(pos).getValue(TYPE)));
                    }
                }
            }
        }
    }
}
