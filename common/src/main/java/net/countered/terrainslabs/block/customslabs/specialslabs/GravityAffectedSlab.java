package net.countered.terrainslabs.block.customslabs.specialslabs;

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
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class GravityAffectedSlab extends CustomSlab implements Fallable {

    public GravityAffectedSlab(BlockBehaviour.Properties properties) {
        super(properties);
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
        BlockState below = level.getBlockState(pos.below());

        // Merge check (runs regardless of whether there is air below): if this
        // slab can reach a same-block BOTTOM slab by descending through its own
        // empty lower half and any free space beneath, merge into a DOUBLE slab
        // there. Covers both the "falling" case and the case where this slab
        // already rests directly on top of a bottom slab with no air gap.
        BlockPos landing = findMergeLanding(level, pos);
        if (landing != null && pos.getY() >= level.getMinBuildHeight()) {
            BlockState landingState = level.getBlockState(landing);
            level.removeBlock(pos, false);
            level.setBlockAndUpdate(landing, landingState.setValue(TYPE, SlabType.DOUBLE));
            return;
        }

        // Case 1: this is a TOP slab whose own lower half is empty. If the block
        // directly below offers a surface flush with our bottom (a full block, or
        // a TOP/DOUBLE slab), settle into the lower half of this same position by
        // becoming a BOTTOM slab. This closes the "half-slab gap" that the
        // full-block falling-entity path can't fill.
        if (state.getValue(TYPE) == SlabType.TOP && hasSupportBelow(below)) {
            level.setBlockAndUpdate(pos, state.setValue(TYPE, SlabType.BOTTOM));
            return;
        }

        // Case 2: normal full-block fall when the space directly below is free.
        if (isFree(below) && pos.getY() >= level.getMinBuildHeight()) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, state);
            this.falling(fallingBlockEntity);
        }
    }

    /**
     * Scans straight down from {@code pos} through the free (air/replaceable)
     * column. Returns the position of a same-block BOTTOM slab that this slab
     * would come to rest on (and can merge into a DOUBLE slab), or {@code null}
     * if the slab would instead land on something else / fall out of the world.
     */
    private BlockPos findMergeLanding(ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos cursor = pos.mutable().move(Direction.DOWN);
        while (cursor.getY() >= level.getMinBuildHeight()) {
            BlockState s = level.getBlockState(cursor);
            if (isFree(s)) {
                cursor.move(Direction.DOWN);
                continue;
            }
            // First non-free block encountered: mergeable only if it's a
            // BOTTOM slab of this same block.
            if (s.is(this) && s.getValue(TYPE) == SlabType.BOTTOM) {
                return cursor.immutable();
            }
            return null;
        }
        return null;
    }

    /**
     * True when the block below presents a solid surface at its top that a
     * bottom slab can rest on: a full block, or a bottom/double slab (whose
     * occupied half reaches the position's vertical midline). A TOP slab of any
     * kind below does NOT count, because its lower half is empty.
     */
    /**
     * True when the block below presents a solid surface flush with the bottom
     * of this slab's position, so a BOTTOM slab settled here would rest on it.
     * <p>A full block qualifies. A slab below qualifies when its solid half
     * reaches that shared boundary: a TOP slab (solid upper half, flush with our
     * bottom) or a DOUBLE slab. A BOTTOM slab below does NOT — its solid half is
     * at the bottom of its own space, leaving our lower half unsupported.
     */
    private static boolean hasSupportBelow(BlockState below) {
        if (below.getBlock() instanceof SlabBlock) {
            SlabType t = below.getValue(BlockStateProperties.SLAB_TYPE);
            return t == SlabType.TOP || t == SlabType.DOUBLE;
        }
        return below.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
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
        return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced();
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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        if (blockState.is(this)) {
            return blockState.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, false);
        } else {
            FluidState fluidState = context.getLevel().getFluidState(blockPos);
            BlockState blockState2 = this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
            Direction direction = context.getClickedFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - blockPos.getY() > 0.5))
                    ? blockState2
                    : blockState2.setValue(TYPE, SlabType.TOP);
        }
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlock) {
        // If the entity broke instead of placing, try to merge into a same-block
        // BOTTOM slab at the break position or just below it (the exact resting
        // blockpos varies), forming a DOUBLE slab rather than dropping an item.
        for (BlockPos candidate : new BlockPos[]{ pos, pos.below() }) {
            BlockState candidateState = level.getBlockState(candidate);
            if (candidateState.is(this) && candidateState.getValue(TYPE) == SlabType.BOTTOM) {
                level.setBlockAndUpdate(candidate, candidateState.setValue(TYPE, SlabType.DOUBLE));
                // The entity drops its block on break unless told not to; the
                // material is now part of the double slab, so suppress that drop.
                fallingBlock.disableDrop();
                return;
            }
        }

        // No merge possible: preserve the original drop behaviour.
        if (fallingBlock.getBlockState().getValue(TYPE) == SlabType.DOUBLE) {
            popResource(level, pos, new ItemStack(this.asItem()));
        }
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        // If we land directly above a BOTTOM slab of the same block, merge
        // downward into a DOUBLE slab in that lower position instead of resting
        // as a separate slab on top. This closes the gap in stacked-overhang
        // cases where a top and bottom slab of the same material end up adjacent.
        if (belowState.is(this) && belowState.getValue(TYPE) == SlabType.BOTTOM) {
            level.setBlockAndUpdate(belowPos, belowState.setValue(TYPE, SlabType.DOUBLE));
            // 'pos' itself is left as whatever it was (air/replaceable); the
            // falling block has been consumed into the double slab below.
            return;
        }

        // Otherwise settle a landed TOP slab into the bottom of its own space,
        // as before.
        if (state.getValue(TYPE) == SlabType.TOP) {
            level.setBlockAndUpdate(pos, this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM));
        }
    }
}

