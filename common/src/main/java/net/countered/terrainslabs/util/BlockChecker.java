package net.countered.terrainslabs.util;

import net.countered.terrainslabs.TerrainSlabs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;

public class BlockChecker {
    private final HashSet<Block> set;

    public BlockChecker( List<ResourceLocation> locations ) {
        this.set = new HashSet<>();
        for ( ResourceLocation blockLoc : locations ) {
            LoggerFactory.getLogger( TerrainSlabs.MOD_ID ).info( blockLoc.toString() );
            set.add( BuiltInRegistries.BLOCK.get( blockLoc ) );
        }
    }

    public boolean contains( Block block ) {
        return set.contains( block );
    }
}
