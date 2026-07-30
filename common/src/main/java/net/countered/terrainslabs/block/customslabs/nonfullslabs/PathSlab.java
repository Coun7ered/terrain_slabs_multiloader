package net.countered.terrainslabs.block.customslabs.nonfullslabs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PathSlab extends NonFullSlab {

    protected static final VoxelShape BOTTOM_SHAPE_COL = Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);
    protected static final VoxelShape TOP_SHAPE_COL = Block.box(0.0, 8.0, 0.0, 16.0, 15.0, 16.0);
    protected static final VoxelShape DOUBLE_SHAPE_COL = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

    public PathSlab(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape bottomCollisionShape() {
        return BOTTOM_SHAPE_COL;
    }

    @Override
    protected VoxelShape topCollisionShape() {
        return TOP_SHAPE_COL;
    }

    @Override
    protected VoxelShape doubleCollisionShape() {
        return DOUBLE_SHAPE_COL;
    }

    @Override
    protected VoxelShape bottomOutlineShape() {
        return BOTTOM_SHAPE_COL;
    }

    @Override
    protected VoxelShape topOutlineShape() {
        return TOP_SHAPE_COL;
    }

    @Override
    protected VoxelShape doubleOutlineShape() {
        return DOUBLE_SHAPE_COL;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        FarmlandSlab.turnToDirt(null, state, level, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos.above());
        return !blockState.isSolid() || blockState.getBlock() instanceof FenceGateBlock;
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess ticks,
            BlockPos pos,
            Direction directionToNeighbour,
            BlockPos neighbourPos,
            BlockState neighbourState,
            RandomSource random
    ) {
        if (directionToNeighbour == Direction.UP && !state.canSurvive(level, pos)) {
            ticks.scheduleTick(pos, this, 1);
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }
}
