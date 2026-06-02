package net.countered.terrainslabs.block.customslabs.nonfullslabs;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MudSlab extends NonFullSlab {
    protected static final VoxelShape BOTTOM_SHAPE_COL = Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);
    protected static final VoxelShape TOP_SHAPE_COL = Block.box(0.0, 8.0, 0.0, 16.0, 14.0, 16.0);
    protected static final VoxelShape DOUBLE_SHAPE_COL = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    protected static final VoxelShape BOTTOM_SHAPE_OUT = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape TOP_SHAPE_OUT = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);

    public MudSlab(BlockBehaviour.Properties properties) {
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
        return BOTTOM_SHAPE_OUT;
    }

    @Override
    protected VoxelShape topOutlineShape() {
        return TOP_SHAPE_OUT;
    }

    @Override
    protected VoxelShape doubleOutlineShape() {
        return Shapes.block();
    }
}

