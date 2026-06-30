package net.countered.terrainslabs.block;

import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ModSlabsMap {

    private static final Map<Block, Block> SLAB_MAP = new HashMap<>();

    public static boolean addMapping(Block fullBlock, SlabBlock slabBlock ) {
        if ( SLAB_MAP.containsKey(fullBlock) ) {
            return false;
        }
        SLAB_MAP.put(fullBlock, slabBlock);
        return true;
    }

    public static @Nullable Block getSlabForBlock(Block block) {
        return SLAB_MAP.get(block);
    }
}
