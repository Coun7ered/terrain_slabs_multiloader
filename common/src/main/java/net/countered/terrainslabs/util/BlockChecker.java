package net.countered.terrainslabs.util;

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
        if ( locations.isEmpty() ) {
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
        String[] resourceStr = block.getDescriptionId().split("\\.");
        return set.contains( resourceStr[1] + ":" + resourceStr[2] );
    }
}
