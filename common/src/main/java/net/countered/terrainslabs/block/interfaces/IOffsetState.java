package net.countered.terrainslabs.block.interfaces;

import net.countered.terrainslabs.block.OffsetProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public interface IOffsetState {

    boolean terrain_slabs$isOffset();

    boolean terrain_slabs$hasOffsetState();

    EnumProperty<OffsetProperty.OffsetType> terrain_slabs$getOffsetProperty();

    BlockState terrain_slabs$getOppositeState();

}
