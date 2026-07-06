package net.countered.terrainslabs.util;

import net.countered.terrainslabs.api.SlabHelper;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MixinHelper {

    public static boolean terrain_slabs$isStateValidOnTop(BlockState state) {
        return SlabHelper.isValidOntop(state);
    }

    /**
     * Builds {@code newSlab}'s state carrying over TYPE, WATERLOGGED and (when
     * both slabs have it) GENERATED from the slab being replaced. Shared by the
     * disk/ore feature mixins so replaced slabs keep their shape, waterlogging,
     * and worldgen loot behavior.
     */
    public static BlockState withCopiedSlabProperties(BlockState existing, Block newSlab) {
        BlockState newState = newSlab.defaultBlockState()
                .setValue(SlabBlock.TYPE, existing.getValue(SlabBlock.TYPE))
                .setValue(SlabBlock.WATERLOGGED, existing.getValue(SlabBlock.WATERLOGGED));
        if (existing.hasProperty(CustomSlab.GENERATED) && newState.hasProperty(CustomSlab.GENERATED)) {
            newState = newState.setValue(CustomSlab.GENERATED, existing.getValue(CustomSlab.GENERATED));
        }
        return newState;
    }
}
