package net.countered.terrainslabs.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.lighting.LightEngine;

public interface ISpreadableSlab extends IDuelSlab {
    static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = levelReader.getBlockState(abovePos);
        if (aboveState.is(Blocks.SNOW) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (aboveState.getFluidState().getAmount() == 8) {
            return false;
        }

        // getLightBlockInto has poor impl. Working around:
        BlockState lightState = state.getBlock() instanceof ISlabCopy ? ISlabCopy.getOriginState(state) : state;
        if (state.getBlock() instanceof ISlabCopy && state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
            return aboveState.getLightBlock(levelReader, abovePos) < levelReader.getMaxLightLevel();
        } else {
            int i = LightEngine.getLightBlockInto(levelReader, lightState, pos, aboveState, abovePos, Direction.UP,
                    aboveState.getLightBlock(levelReader, abovePos));
            return i < levelReader.getMaxLightLevel();
        }
    }

    boolean canPropagate(BlockState localState, Level level, BlockPos pos);

    BlockState spreadStateHandler(BlockState previewState, ServerLevel level, BlockPos pos);
}