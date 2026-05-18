package net.countered.terrainslabs.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord") // Do not want access to set
public class BlockChecker {
    private final HashSet<Block> set;

    public BlockChecker( List<ResourceLocation> locations ) {
        this.set = new HashSet<>();
        for ( ResourceLocation blockLoc : locations ) {
            set.add( BuiltInRegistries.BLOCK.get( blockLoc ) );
        }
    }

    public boolean contains( Block block ) {
        return set.contains( block );
    }
}
