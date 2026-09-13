package net.countered.terrainslabs.block.customslabs.apiSlabs;

import net.countered.terrainslabs.block.ModSlabsMap;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.block.interfaces.ISpreadableSlab;
import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public abstract class SpreadableSlab extends CustomSlab implements ISpreadableSlab {
    private final ISlabCopy duel;

    public SpreadableSlab(Block block, ISlabCopy duel) {
        super(block);
        this.duel = duel;
        if (canSpread()) registerGrassy(block, spreadableType());
    }

    public SpreadableSlab(Block block, ISlabCopy duel, BlockBehaviour.Properties properties) {
        super(block, properties);
        this.duel = duel;
        registerGrassy(block, spreadableType());
    }

    protected abstract boolean canSpread();

    protected abstract String spreadableType();

    @Override
    public boolean canPropagate(BlockState localState, Level level, BlockPos pos) {
        return ISpreadableSlab.canBeGrass(localState, level, pos) && !level.getFluidState(pos.above()).is(FluidTags.WATER);
    }

    @Override
    public BlockState spreadStateHandler(BlockState previewState, ServerLevel level, BlockPos pos) {
        return previewState;
    }

    @Override
    public ISlabCopy getDuel() {
        return duel;
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        super.spawnDestroyParticles(level, player, pos, this.getDuelBlock().withPropertiesOf(state));
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return state;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!ISpreadableSlab.canBeGrass(state, level, pos)) {
            if (!level.isLoaded(pos)) {
                return;
            }

            level.setBlockAndUpdate(pos, this.getDuel().getBlock().withPropertiesOf(state));
        } else {
            if (canSpread() && level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                for (int i = 0; i < 4; i++) {
                    BlockPos growingPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (!level.isLoaded(growingPos)) {
                        return;
                    }

                    BlockState localState = level.getBlockState(growingPos);
                    Block localGrassVersion = ModSlabsMap.getGrassy(localState.getBlock(), spreadableType());
                    ISpreadableSlab grassySlab = ModSlabsMap.getGrassySlab(localState.getBlock(), spreadableType());

                    if (grassySlab != null && grassySlab.canPropagate(localState, level, growingPos)) {
                        assert localGrassVersion != null;
                        level.setBlockAndUpdate(growingPos, grassySlab.spreadStateHandler(
                                localGrassVersion.withPropertiesOf(localState), level, pos));
                    }
                }
            }
        }
    }

    private void registerGrassy(Block block, String type) {
        if ( !ModSlabsMap.addGrassMappings( this, type ) ) {
            throw new IllegalArgumentException( "Cannot add spreading mapping for block "
                    + block.getName().getString() + " because block already has spreading mapping." );
        }
    }
}