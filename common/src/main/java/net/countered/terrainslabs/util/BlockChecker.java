package net.countered.terrainslabs.util;

import net.countered.terrainslabs.mixin.offset.state.BlockAccessor;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.List;

/**
 * Handles a group of block strings in a HashSet for quick reference.
 */
public class BlockChecker {
    private final HashSet<String> set;

    public BlockChecker( List<String> locations ) {
        this.set = new HashSet<>();
        if ( locations == null || locations.isEmpty() ) {
            return;
        }

        for ( String str : locations ) {
            if (!str.contains(":")) {
                str = "minecraft:" + str;
            }
            set.add( str );
        }
    }

    public boolean contains( Block block ) {
        String[] resourceStr = safeGetDescriptionId( block ).split("\\.");
        return set.contains( resourceStr[1] + ":" + resourceStr[2] );
    }

    public static String safeGetDescriptionId( Block block ) {
        if (((BlockAccessor) block).terrain_slabs$getDescriptionId() == null) {
            return Util.makeDescriptionId("block", BuiltInRegistries.BLOCK.getKey(block));
        }

        return block.getDescriptionId();
    }
}
