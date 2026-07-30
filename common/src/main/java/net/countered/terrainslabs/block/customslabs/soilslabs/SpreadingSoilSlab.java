package net.countered.terrainslabs.block.customslabs.soilslabs;

import net.countered.terrainslabs.block.customslabs.CustomSlab;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;

public abstract class SpreadingSoilSlab extends SoilSlab {

    public SpreadingSoilSlab(Properties properties) {
        super(properties);
    }

    /**
     * Shared logic for grass-like blocks (Grass, Mycelium) to determine if they can remain.
     */
    protected static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = levelReader.getBlockState(abovePos);
        if (aboveState.is(Blocks.SNOW) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (aboveState.getFluidState().getAmount() == 8) {
            return false;
        } else {
            if (state.getBlock() instanceof SlabBlock) {
                int lightOpacity = aboveState.getLightDampening();
                return lightOpacity < 15;
            }
            else {
                int i = LightEngine.getLightDampeningInto(state, aboveState, Direction.UP, aboveState.getLightDampening());
                return i < 15;
            }
        }
    }

    protected static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.above();
        return canBeGrass(state, level, pos) && !level.getFluidState(blockPos).is(FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            CustomSlab.replaceWithSlabPreserveType(level, pos, state, ModBlocksRegistry.DIRT_SLAB.get());
        } else {
            if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                for (int i = 0; i < 4; i++) {
                    BlockPos targetPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    BlockState targetState = level.getBlockState(targetPos);
                    if (targetState.is(ModBlocksRegistry.DIRT_SLAB.get())) {
                        BlockState grassSlabState = this.defaultBlockState()
                                .setValue(TYPE, targetState.getValue(TYPE))
                                .setValue(SNOWY, level.getBlockState(targetPos.above()).is(Blocks.SNOW))
                                .setValue(WATERLOGGED, targetState.getValue(WATERLOGGED));

                        if (canPropagate(grassSlabState, level, targetPos)) {
                            level.setBlockAndUpdate(targetPos, grassSlabState);
                        }
                    }
                    else if (targetState.is(Blocks.DIRT) && canPropagate(Blocks.GRASS_BLOCK.defaultBlockState(), level, targetPos)) {
                        BlockState vanillaTargetBlock = getVanillaFullBlockCounterpart();

                        if (vanillaTargetBlock != null) {
                            BlockState spreadingFullState = vanillaTargetBlock.setValue(SNOWY, level.getBlockState(targetPos.above()).is(Blocks.SNOW));
                            level.setBlockAndUpdate(targetPos, spreadingFullState);
                        }
                    }
                }
            }
        }
    }

    public abstract BlockState getVanillaFullBlockCounterpart();
}

