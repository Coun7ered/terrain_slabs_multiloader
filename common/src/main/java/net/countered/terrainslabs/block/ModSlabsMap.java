package net.countered.terrainslabs.block;

import net.countered.terrainslabs.block.interfaces.ISpreadableSlab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class ModSlabsMap {
    private static final Map<Block, Block> SLAB_MAP = new HashMap<>();

    private static final Map<Block, Map<String, Block>> GRASSABLE_SLAB_MAP = new HashMap<>(20);
    private static final Map<Block, Map<String, Block>> GRASSABLE_MAP = new HashMap<>(20);

    public static boolean addMapping(Block fullBlock, SlabBlock slabBlock) {
        if ( SLAB_MAP.containsKey(fullBlock) ) {
            return false;
        }
        SLAB_MAP.put(fullBlock, slabBlock);
        return true;
    }

    public static boolean addGrassMappings(ISpreadableSlab grassySlab, String type) {
        Block grassableSlab = grassySlab.getDuel().getBlock();
        Block grassable = grassySlab.getDuelBlock();
        if (GRASSABLE_SLAB_MAP.containsKey(grassableSlab)) {
            if (GRASSABLE_SLAB_MAP.get(grassableSlab).containsKey(type)) {
                return false;
            }
        } else {
            GRASSABLE_SLAB_MAP.put(grassableSlab, new HashMap<>(3));
            GRASSABLE_MAP.put(grassable, new HashMap<>(3));
        }

        GRASSABLE_SLAB_MAP.get(grassableSlab).put(type, grassySlab.getBlock());
        GRASSABLE_MAP.get(grassable).put(type, grassySlab.getOriginBlock());
        return true;
    }

    public static @Nullable Block getGrassy(Block block, String type) {
        Block slab = !GRASSABLE_SLAB_MAP.containsKey(block) || !GRASSABLE_SLAB_MAP.get(block).containsKey(type) ? null
                : GRASSABLE_SLAB_MAP.get(block).get(type);
        Block full = !GRASSABLE_MAP.containsKey(block) || !GRASSABLE_MAP.get(block).containsKey(type) ? null
                : GRASSABLE_MAP.get(block).get(type);

        return slab == null ? full : slab;
    }
    public static @Nullable ISpreadableSlab getGrassySlab(Block block, String type) {
        Block slab = !GRASSABLE_SLAB_MAP.containsKey(block) || !GRASSABLE_SLAB_MAP.get(block).containsKey(type) ? null
                : GRASSABLE_SLAB_MAP.get(block).get(type);
        Block full = !GRASSABLE_MAP.containsKey(block) || !GRASSABLE_MAP.get(block).containsKey(type) ? null
                : GRASSABLE_MAP.get(block).get(type);

        if (slab != null) {
            return (ISpreadableSlab) slab;
        } else if (full != null) {
            return (ISpreadableSlab) ModSlabsMap.getSlabForBlock(full);
        }

        return null;
    }

    public static @Nullable Block getSlabForBlock(Block block) {
        return SLAB_MAP.get(block);
    }
}