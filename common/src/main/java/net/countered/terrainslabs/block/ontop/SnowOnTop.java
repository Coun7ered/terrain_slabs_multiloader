package net.countered.terrainslabs.block.ontop;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.SlabBlock.TYPE;

public class SnowOnTop extends SnowLayerBlock {

    protected static final VoxelShape[] LAYERS_TO_SHAPE = new VoxelShape[]{
            Shapes.empty(),
            Block.box(0.0, -8.0, 0.0, 16.0, -6.0,  16.0),
            Block.box(0.0, -8.0, 0.0, 16.0, -4.0,  16.0),
            Block.box(0.0, -8.0, 0.0, 16.0, -2.0,  16.0),
            Block.box(0.0, -8.0, 0.0, 16.0, 0.0,  16.0),
            Block.box(0.0, -8.0, 0.0, 16.0, 2.0, 16.0),
            Block.box(0.0, -8.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, -8.0, 0.0, 16.0, 6.0, 16.0),
            Block.box(0.0, -8.0, 0.0, 16.0, 8.0, 16.0)
    };

    public SnowOnTop(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, 1));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return LAYERS_TO_SHAPE[state.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return LAYERS_TO_SHAPE[state.getValue(LAYERS)-1];
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPE_BY_LAYER[state.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_LAYER[state.getValue(LAYERS)];
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos.below());
        if (blockState.getBlock() instanceof SlabBlock) {
            return blockState.getValue(TYPE).equals(SlabType.BOTTOM);
        }
        return false;
    }
}
