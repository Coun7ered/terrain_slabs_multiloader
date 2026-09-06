package net.countered.terrainslabs.block.customslabs.apiSlabs;

import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.block.interfaces.IDuelSlab;
import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class GrassySlab extends CustomSlab implements IDuelSlab {
    private final ISlabCopy duel;
    private final boolean canSpread;

    public GrassySlab(Block block, ISlabCopy duel) {
        this(block, duel, true);
    }

    public GrassySlab(Block block, ISlabCopy duel, boolean canSpread) {
        super(block);
        this.duel = duel;
        this.canSpread = canSpread;
    }

    public GrassySlab(Block block, ISlabCopy duel, BlockBehaviour.Properties properties) {
        super(block, properties);
        this.duel = duel;
        canSpread = true;
    }

    @Override
    public ISlabCopy getDuel() {
        return duel;
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        super.spawnDestroyParticles(level, player, pos, this.getDuelBlock().withPropertiesOf(state));
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return state;
    }

    private static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState blockState = levelReader.getBlockState(blockPos);
        if (blockState.is(Blocks.SNOW) && blockState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockState.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(levelReader, Blocks.GRASS_BLOCK.defaultBlockState(), pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(levelReader, blockPos));
            return i < levelReader.getMaxLightLevel();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            this.getDuel().getBlock().withPropertiesOf(state);
        } else {
            if (canSpread && level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                for (int i = 0; i < 4; i++) {
                    BlockPos blockPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    BlockState localState = level.getBlockState(blockPos);
                    if (canPropagate( localState, level, pos )) {
                        if ( localState.is( this.getDuel().getBlock() ) ) {
                            level.setBlockAndUpdate( blockPos, spreadStateHandler(
                                    this.withPropertiesOf(localState), level, pos) );
                        } else if ( localState.is( this.getDuelBlock() ) ) {
                            level.setBlockAndUpdate( blockPos, spreadStateHandler(
                                    this.getOriginBlock().withPropertiesOf(localState), level, pos) );
                        }
                    }
                }
            }
        }
    }

    protected boolean canPropagate(BlockState localState, Level level, BlockPos pos ) {
        return canBeGrass(localState, level, pos) && !level.getFluidState(pos.above()).is(FluidTags.WATER);
    }

    protected BlockState spreadStateHandler(BlockState previewState, ServerLevel level, BlockPos pos) {
        return previewState;
    }
}
