package net.countered.terrainslabs.block.customslabs.soilslabs;

import net.countered.terrainslabs.block.customslabs.apiSlabs.GrassySlab;
import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.jetbrains.annotations.NotNull;


public class SnowyGrassySlab extends GrassySlab {
    public static final BooleanProperty SNOWY;
    static {
        SNOWY = BlockStateProperties.SNOWY;
    }

    public SnowyGrassySlab(Block block, ISlabCopy duel) {
        this(block, duel, true);
    }

    public SnowyGrassySlab(Block block, ISlabCopy duel, boolean canSpread) {
        super(block, duel, canSpread);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(SNOWY, false)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
    }

    public SnowyGrassySlab(Block block, ISlabCopy duel, BlockBehaviour.Properties properties) {
        super(block, duel, properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(SNOWY, false)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
    }

    @Override
    public ISlabCopy getDuel() {
        return (ISlabCopy) ModBlocksRegistry.DIRT_SLAB.get();
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP) {
            state = state.setValue(SNOWY, isSnow(neighborState));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockAbove = context.getLevel().getBlockState( context.getClickedPos().above() );
        return super.getStateForPlacement(context).setValue(SNOWY, isSnow(blockAbove));
    }

    protected static boolean isSnow(BlockState state) {
        return state.is(BlockTags.SNOW);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SNOWY, TYPE, WATERLOGGED, GENERATED);
    }

    @Override
    protected BlockState spreadStateHandler(BlockState previewState, ServerLevel level, BlockPos pos) {
        BlockState blockAbove = level.getBlockState( pos.above() );
        return super.spreadStateHandler(previewState, level, pos).setValue( SNOWY, isSnow(blockAbove));
    }
}
