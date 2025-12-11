package net.countered.terrainslabs.fabric;

import net.countered.terrainslabs.fabric.feature.ModAddedFeatures;
import net.fabricmc.api.ModInitializer;

import net.countered.terrainslabs.TerrainSlabs;

public final class TerrainSlabsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ModAddedFeatures.registerFeatures();
        // Run our common setup.
        TerrainSlabs.init();
    }
}
