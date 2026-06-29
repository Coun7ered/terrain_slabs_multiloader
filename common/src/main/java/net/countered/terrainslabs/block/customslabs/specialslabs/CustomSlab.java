package net.countered.terrainslabs.block.customslabs.specialslabs;

import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;

public class CustomSlab extends SlabBlock implements ISlabCopy {
    public static final BooleanProperty GENERATED;
    private final Block originBlock;

    static {
        GENERATED = BooleanProperty.create("generated");
    }

    public CustomSlab( Block block ) {
        super( BlockBehaviour.Properties.copy( block ) );
        this.originBlock = block;
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
    }

    public CustomSlab( Block block, BlockBehaviour.Properties properties ) {
        super(properties);
        this.originBlock = block;
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
    }

    @Override
    public Block getOriginBlock() {
        return originBlock;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED, GENERATED);
    }
}
