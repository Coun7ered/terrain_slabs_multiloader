package net.countered.terrainslabs.block;

import com.mojang.logging.LogUtils;
import net.countered.terrainslabs.block.interfaces.ISpreadableSlab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public final class ModSlabsMap {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<Block, Block> SLAB_MAP = new HashMap<>();

    private static final Map<Block, Map<String, Block>> SPREADABLE_SLAB_MAP = new HashMap<>(20);
    private static final Map<Block, Map<String, Block>> SPREADABLE_MAP = new HashMap<>(20);

    public static boolean addMapping(Block fullBlock, SlabBlock slabBlock) {
        if ( SLAB_MAP.containsKey(fullBlock) ) {
            return false;
        }
        SLAB_MAP.put(fullBlock, slabBlock);
        return true;
    }

    public static boolean addSpreadMappings(ISpreadableSlab grassySlab, String type) {
        Block grassableSlab = grassySlab.getDuel().getBlock();
        Block grassable = grassySlab.getDuelBlock();
        if (SPREADABLE_SLAB_MAP.containsKey(grassableSlab)) {
            if (SPREADABLE_SLAB_MAP.get(grassableSlab).containsKey(type)) {
                LOGGER.error("Cannot register new spreadable in place of {}. Unspread: {}, {}",
                        SPREADABLE_SLAB_MAP.get(grassySlab.getDuel().getBlock()).get(type).getName(),
                        grassableSlab.getName(), grassable.getName());
                return false;
            }
        } else {
            SPREADABLE_SLAB_MAP.put(grassableSlab, new HashMap<>(3));
            SPREADABLE_MAP.put(grassable, new HashMap<>(3));
        }

        SPREADABLE_SLAB_MAP.get(grassableSlab).put(type, grassySlab.getBlock());
        SPREADABLE_MAP.get(grassable).put(type, grassySlab.getOriginBlock());
        return true;
    }

    public static @Nullable Block getSpread(Block block, String type) {
        Block slab = !SPREADABLE_SLAB_MAP.containsKey(block) || !SPREADABLE_SLAB_MAP.get(block).containsKey(type) ? null
                : SPREADABLE_SLAB_MAP.get(block).get(type);
        Block full = !SPREADABLE_MAP.containsKey(block) || !SPREADABLE_MAP.get(block).containsKey(type) ? null
                : SPREADABLE_MAP.get(block).get(type);

        return slab == null ? full : slab;
    }
    public static @Nullable ISpreadableSlab getSpreadSlab(Block block, String type) {
        Block slab = !SPREADABLE_SLAB_MAP.containsKey(block) || !SPREADABLE_SLAB_MAP.get(block).containsKey(type) ? null
                : SPREADABLE_SLAB_MAP.get(block).get(type);
        Block full = !SPREADABLE_MAP.containsKey(block) || !SPREADABLE_MAP.get(block).containsKey(type) ? null
                : SPREADABLE_MAP.get(block).get(type);

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