package net.countered.terrainslabs.block.customslabs.nonfullslabs;

import net.countered.terrainslabs.block.customslabs.CustomSlab;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Base class for "non-full" slabs (custom collision/outline shapes and shade behavior).
 * Subclasses should provide the concrete VoxelShapes via the abstract methods.
 */
public abstract class NonFullSlab extends CustomSlab {

    public NonFullSlab(Properties properties) {
        super(properties);
    }

    protected abstract VoxelShape bottomCollisionShape();
    protected abstract VoxelShape topCollisionShape();
    protected abstract VoxelShape doubleCollisionShape();

    protected abstract VoxelShape bottomOutlineShape();
    protected abstract VoxelShape topOutlineShape();
    protected abstract VoxelShape doubleOutlineShape();

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        SlabType slabType = state.getValue(TYPE);
        return switch (slabType) {
            case DOUBLE -> doubleCollisionShape();
            case TOP -> topCollisionShape();
            default -> bottomCollisionShape();
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        SlabType slabType = state.getValue(TYPE);
        return switch (slabType) {
            case DOUBLE -> doubleOutlineShape();
            case TOP -> topOutlineShape();
            default -> bottomOutlineShape();
        };
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state) {
        SlabType slabType = state.getValue(TYPE);
        return switch (slabType) {
            case DOUBLE -> doubleOutlineShape();
            case TOP -> topOutlineShape();
            default -> bottomOutlineShape();
        };
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        SlabType slabType = state.getValue(TYPE);
        if (slabType == SlabType.DOUBLE) {
            return 0.2F;
        }
        return 1F;
    }
}
