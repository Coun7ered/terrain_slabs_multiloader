package net.countered.terrainslabs.block;

import net.countered.terrainslabs.block.interfaces.IDuelSlab;
import net.countered.terrainslabs.block.interfaces.IGrassySlab;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ModSlabsMap {

    private static final Map<Block, Block> SLAB_MAP = new HashMap<>();

    private static final Map<Block, Block> GRASSABLE_SLAB_MAP = new HashMap<>();
    private static final Map<Block, Block> GRASSABLE_MAP = new HashMap<>();

    public static boolean addMapping(Block fullBlock, SlabBlock slabBlock) {
        if ( SLAB_MAP.containsKey(fullBlock) ) {
            return false;
        }
        SLAB_MAP.put(fullBlock, slabBlock);
        return true;
    }

    public static boolean addGrassMappings(IGrassySlab grassySlab) {
        Block grassableSlab = grassySlab.getDuel().getBlock();
        Block grassable = grassySlab.getDuelBlock();
        if ( GRASSABLE_SLAB_MAP.containsKey(grassableSlab) || GRASSABLE_MAP.containsKey(grassable)) {
            return false;
        }

        GRASSABLE_SLAB_MAP.put(grassableSlab, grassySlab.getBlock());
        GRASSABLE_MAP.put(grassable, grassySlab.getOriginBlock());
        return true;
    }

    public static @Nullable Block getGrassy(Block block) {
        Block slab = GRASSABLE_SLAB_MAP.get(block);
        Block full = GRASSABLE_MAP.get(block);
        return slab == null ? full : slab;
    }
    public static @Nullable IGrassySlab getGrassySlab(Block block) {
        Block slab = GRASSABLE_SLAB_MAP.get(block);
        Block full = GRASSABLE_MAP.get(block);
        if (slab != null) {
            return (IGrassySlab) slab;
        }

        return (IGrassySlab) ModSlabsMap.getSlabForBlock(full);
    }

    public static @Nullable Block getSlabForBlock(Block block) {
        return SLAB_MAP.get(block);
    }
}
