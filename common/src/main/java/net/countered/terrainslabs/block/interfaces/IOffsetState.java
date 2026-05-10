package net.countered.terrainslabs.block.interfaces;

import net.minecraft.world.level.block.state.BlockState;

public interface IOffsetState {

    boolean terrain_slabs$getOffset();

    boolean terrain_slabs$hasOffsetState();

    BlockState terrain_slabs$getOppositeState();

    BlockState asState();

}
