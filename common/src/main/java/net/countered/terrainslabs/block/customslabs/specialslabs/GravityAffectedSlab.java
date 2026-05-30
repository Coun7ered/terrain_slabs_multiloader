package net.countered.terrainslabs.block.customslabs.specialslabs;

import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class GravityAffectedSlab extends CustomSlab implements Fallable {

    public GravityAffectedSlab( Block block ) {
        super( block );
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED,false)
                .setValue(GENERATED,false));
    }

    public GravityAffectedSlab( Block block, BlockBehaviour.Properties properties ) {
        super( block, properties );
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED,false)
                .setValue(GENERATED,false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED, GENERATED);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, this.getDelayAfterPlace());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, state);
            this.falling(fallingBlockEntity);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        level.scheduleTick(pos, this, this.getDelayAfterPlace());
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    protected void falling(FallingBlockEntity entity) {
    }

    /**
     * Gets the amount of time in ticks this block will wait before attempting to start falling.
     */
    protected int getDelayAfterPlace() {
        return 2;
    }

    public static boolean isFree(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced()
                || ( state.getBlock() instanceof SlabBlock && state.getValue( SlabBlock.TYPE ) == SlabType.BOTTOM );
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(16) == 0) {
            BlockPos blockPos = pos.below();
            if (isFree(level.getBlockState(blockPos))) {
                ParticleUtils.spawnParticleBelow(level, pos, random, new BlockParticleOption(ParticleTypes.FALLING_DUST, state));
            }
        }
    }

    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        return -16777216;
    }

    // Cannot be placed as a top slab.
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        if (blockState.is(this)) {
            return blockState.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, false);
        } else {
            FluidState fluidState = context.getLevel().getFluidState(blockPos);
            return this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        }
    }

    @Override
    public void onBrokenAfterFall( Level level, BlockPos pos, FallingBlockEntity fallingBlockEntity ) {
        BlockState fallingBlockState = fallingBlockEntity.getBlockState();
        BlockState landedOnBlockState = level.getBlockState( pos );

        //No need to check state, would only trigger on bottom slab
        if ( landedOnBlockState.is( this ) ) {
            Block originBlock = ( (ISlabCopy) fallingBlockState.getBlock() ).getOriginBlock();

            if ( fallingBlockState.getValue( TYPE ).equals( SlabType.DOUBLE ) ) {
                BlockState aboveState = level.getBlockState( pos.above() );
                if ( !( aboveState.is( BlockTags.REPLACEABLE ) || aboveState.isAir() || aboveState.is( Blocks.WATER ) ) ) {
                    popResource( level, pos, new ItemStack( this.getOriginItem() ) );
                } else {
                    level.setBlockAndUpdate( pos.above(), this.withPropertiesOf( landedOnBlockState )
                            .setValue( TYPE, SlabType.BOTTOM ) );
                }

            }

            if ( landedOnBlockState.getValue( GENERATED ) ) {
                level.setBlockAndUpdate( pos, originBlock.withPropertiesOf( landedOnBlockState ) );
            } else {
                level.setBlockAndUpdate( pos, this.defaultBlockState().setValue( TYPE, SlabType.DOUBLE ));
            }

            return;
        }

        // Loot if checks fail
        if ( fallingBlockState.getValue(TYPE).equals( SlabType.DOUBLE) ) {
            popResource(level, pos, new ItemStack( this.getOriginItem(), 2) );
        } else {
            popResource(level, pos, new ItemStack( this.getOriginItem()) );
        }
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        if (state.getValue(TYPE) == SlabType.TOP) {
            level.setBlockAndUpdate(pos, this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM));
        }
    }
}

