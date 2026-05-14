package net.countered.terrainslabs.block.customslabs.specialslabs;



import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;

public class CustomSlab extends SlabBlock {
    public static final BooleanProperty GENERATED;
    private final Block FULL_BLOCK_EQUIVLENT;

    static {
        GENERATED = BooleanProperty.create("generated");
    }

    public CustomSlab(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
        this.FULL_BLOCK_EQUIVLENT = Blocks.SCAFFOLDING;
    }

    public CustomSlab(BlockBehaviour.Properties properties, Block equivlentFullBlock) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
        this.FULL_BLOCK_EQUIVLENT = equivlentFullBlock;
    }

    public CustomSlab(Block equivlentFullBlock) {
        super(BlockBehaviour.Properties.ofFullCopy(equivlentFullBlock));
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
        this.FULL_BLOCK_EQUIVLENT = equivlentFullBlock;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED, GENERATED);
    }

    /**
     * Will return the equivlent full block of this slab, if non is set it will use scaffolding
     * @param stateOfThisSlab The state of the slab being replaced
     * @return The equivlent full block
     */
    public BlockState getEquivlentFullBlock(BlockState stateOfThisSlab){
        BlockState blockToReplaceWith = FULL_BLOCK_EQUIVLENT.defaultBlockState();
        if(blockToReplaceWith.hasProperty(WATERLOGGED)){
            blockToReplaceWith = blockToReplaceWith.setValue(WATERLOGGED, stateOfThisSlab.getValue(WATERLOGGED));
        }
        return blockToReplaceWith;
    }
}
