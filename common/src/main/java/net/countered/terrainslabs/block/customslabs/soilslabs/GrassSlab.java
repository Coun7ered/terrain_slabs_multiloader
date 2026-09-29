package net.countered.terrainslabs.block.customslabs.soilslabs;

import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;


public class GrassSlab extends SpreadingSoilSlab {

    public GrassSlab(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getVanillaFullBlockCounterpart() {
        return Blocks.GRASS_BLOCK.defaultBlockState();
    }

    @Override
    public void spawnDestroyParticles(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(TYPE) == SlabType.DOUBLE) {
            super.spawnDestroyParticles(level, pos, Blocks.DIRT.defaultBlockState());
        }
        else if (state.getValue(TYPE) == SlabType.TOP) {
            super.spawnDestroyParticles(level, pos, ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState().setValue(TYPE, SlabType.TOP));
        }
        else {
            super.spawnDestroyParticles(level, pos, ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState());
        }
    }
}
