package net.countered.terrainslabs.block;

import net.countered.terrainslabs.TerrainSlabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {

    public static final TagKey<Block> DIRT_SLABS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TerrainSlabs.MOD_ID, "dirt_slabs"));
    public static final TagKey<Block> TERRACOTTA_SLABS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TerrainSlabs.MOD_ID, "terracotta_slabs"));
    public static final TagKey<Block> ON_TOP_BLOCKS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TerrainSlabs.MOD_ID, "on_top_blocks"));
    /**
     * Ontop blocks that keep the visual offset even when the slab below is
     * waterlogged: water-standing plants (e.g. cattails) that should sink onto
     * the slab surface inside the water instead of floating above it.
     */
    public static final TagKey<Block> WATER_OFFSET_BLOCKS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TerrainSlabs.MOD_ID, "water_offset_blocks"));

}
