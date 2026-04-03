package net.countered.terrainslabs.registries.data;

import net.countered.terrainslabs.registries.FlattenableBlockRegistry;
import net.countered.terrainslabs.registries.ModBlocksRegistry;

public class FlattenableData {

    public static void init() {

        FlattenableBlockRegistry.register(
                ModBlocksRegistry.DIRT_SLAB,
                ModBlocksRegistry.PATH_SLAB
        );
        FlattenableBlockRegistry.register(
                ModBlocksRegistry.GRASS_SLAB,
                ModBlocksRegistry.PATH_SLAB
        );
        FlattenableBlockRegistry.register(
                ModBlocksRegistry.PODZOL_SLAB,
                ModBlocksRegistry.PATH_SLAB
        );
        FlattenableBlockRegistry.register(
                ModBlocksRegistry.MYCELIUM_SLAB,
                ModBlocksRegistry.PATH_SLAB
        );
    }
}