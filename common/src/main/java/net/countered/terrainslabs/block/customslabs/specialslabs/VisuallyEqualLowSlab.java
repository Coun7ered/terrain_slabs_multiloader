package net.countered.terrainslabs.block.customslabs.specialslabs;

import net.countered.terrainslabs.block.customslabs.apiSlabs.LowSlab;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class VisuallyEqualLowSlab extends LowSlab {
    protected static final VoxelShape BOTTOM_SHAPE_OUT = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape TOP_SHAPE_OUT = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);

    public VisuallyEqualLowSlab(Block block, BlockBehaviour.Properties properties) {
        super( block, properties);
    }

    public VisuallyEqualLowSlab(Block block) {
        super(block);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        SlabType slabType = state.getValue(TYPE);
        return switch (slabType) {
            case DOUBLE -> Shapes.block();
            case TOP -> TOP_SHAPE_OUT;
            default -> BOTTOM_SHAPE_OUT;
        };
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        SlabType slabType = state.getValue(TYPE);
        return switch (slabType) {
            case DOUBLE -> Shapes.block();
            case TOP -> TOP_SHAPE_OUT;
            default -> BOTTOM_SHAPE_OUT;
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
