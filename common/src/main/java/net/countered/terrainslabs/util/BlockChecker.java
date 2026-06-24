package net.countered.terrainslabs.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class BlockChecker {
    private final HashSet<String> set;

    public <T> BlockChecker( List<T> locations ) {
        this.set = new HashSet<>();
        if ( locations.isEmpty() ) {
            return;
        }

        if ( locations.get( 0 ) instanceof ResourceLocation ) {
            for ( T blockLoc : locations ) {
                set.add( blockLoc.toString() );
            }
            return;
        }
        if ( locations.get( 0 ) instanceof String ) {
            for ( T blockLoc : locations ) {
                String str = (String) blockLoc;
                if (!str.contains(":")) {
                    str = "minecraft:" + str;
                }
                set.add( str );
            }
            return;
        }

        throw new IllegalArgumentException("List must contain String or ResourceLocation.");
    }

    public boolean contains( Block block ) {
        String[] resourceStr = block.getDescriptionId().split("\\.");
        return set.contains( resourceStr[1] + ":" + resourceStr[2] );
    }
}
