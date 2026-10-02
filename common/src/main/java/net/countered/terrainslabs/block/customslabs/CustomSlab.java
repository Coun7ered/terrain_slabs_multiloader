package net.countered.terrainslabs.block.customslabs;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;

public class CustomSlab extends SlabBlock {
    public static final BooleanProperty GENERATED;

    static {
        GENERATED = BooleanProperty.create("generated");
    }

    public CustomSlab(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
    }

    public static BlockState markGenerated(BlockState state) {
        return state.hasProperty(GENERATED) ? state.setValue(GENERATED, true) : state;
    }

    /**
     * Replace the slab at the given position with another slab block while preserving the slab TYPE (BOTTOM/TOP/DOUBLE).
     */
    protected static void replaceWithSlabPreserveType(Level level, BlockPos pos, BlockState currentState, Block targetSlab) {
        SlabType slabType = currentState.getValue(TYPE);
        level.setBlockAndUpdate(pos, targetSlab.defaultBlockState().setValue(TYPE, slabType));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED, GENERATED);
    }
}
