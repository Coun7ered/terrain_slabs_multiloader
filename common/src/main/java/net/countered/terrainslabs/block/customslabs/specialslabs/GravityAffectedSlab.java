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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class GravityAffectedSlab extends CustomSlab implements Fallable {
    public GravityAffectedSlab( Block block ) {
        super( block );
    }

    public GravityAffectedSlab( Block block, BlockBehaviour.Properties properties ) {
        super( block, properties );
    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, this.getDelayAfterPlace());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()
                || state.getValue(TYPE) == SlabType.TOP
        ) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, state);
            this.falling(fallingBlockEntity);
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (scheduleFallOnUpdate()) level.scheduleTick(pos, this, this.getDelayAfterPlace());
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    protected boolean scheduleFallOnUpdate() {
        return true;
    }

    protected void falling(FallingBlockEntity entity) {
    }

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
        return getGravityStateForPlacement(super.getStateForPlacement(context), canPlaceAsTop());
    }

    protected boolean canPlaceAsTop() {
        return false;
    }

    @Override
    public void onBrokenAfterFall( Level level, BlockPos pos, FallingBlockEntity fallingBlockEntity ) {
        GravityAffectedSlab.mergeFallingSlab( this, level, pos, fallingBlockEntity );
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        if (state.getValue(TYPE) == SlabType.TOP) {
            level.setBlockAndUpdate(pos, this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM));
        }
    }

    public static BlockState getGravityStateForPlacement(BlockState superState, boolean canPlaceAsTop) {
        if ( !canPlaceAsTop && superState.getValue(TYPE) == SlabType.TOP ) {
            return superState.setValue(TYPE, SlabType.BOTTOM);
        }

        return superState;
    }

    public static void mergeFallingSlab( CustomSlab thisBlock, Level level, BlockPos pos, FallingBlockEntity fallingBlockEntity ) {
        BlockState fallingBlockState = fallingBlockEntity.getBlockState();
        BlockState landedOnBlockState = level.getBlockState( pos );

        //No need to check state, would only trigger on bottom slab
        if ( landedOnBlockState.is( thisBlock ) ) {
            Block originBlock = ( (ISlabCopy) fallingBlockState.getBlock() ).getOriginBlock();

            if ( fallingBlockState.getValue( TYPE ).equals( SlabType.DOUBLE ) ) {
                BlockState aboveState = level.getBlockState( pos.above() );
                if ( !( aboveState.is( BlockTags.REPLACEABLE ) || aboveState.isAir() || aboveState.is( Blocks.WATER ) ) ) {
                    popResource( level, pos, new ItemStack( thisBlock.getOriginItem() ) );
                } else {
                    level.setBlockAndUpdate( pos.above(), thisBlock.withPropertiesOf( landedOnBlockState )
                            .setValue( TYPE, SlabType.BOTTOM ) );
                }

            }

            if ( landedOnBlockState.getValue( GENERATED ) ) {
                level.setBlockAndUpdate( pos, originBlock.withPropertiesOf( landedOnBlockState ) );
            } else {
                level.setBlockAndUpdate( pos, thisBlock.defaultBlockState().setValue( TYPE, SlabType.DOUBLE ));
            }

            return;
        }

        // Loot if checks fail
        if ( fallingBlockState.getValue(TYPE).equals( SlabType.DOUBLE) ) {
            popResource(level, pos, new ItemStack( thisBlock.getOriginItem(), 2) );
        } else {
            popResource(level, pos, new ItemStack( thisBlock.getOriginItem()) );
        }
    }
}