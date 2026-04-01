package net.countered.terrainslabs.fabric;

import net.countered.terrainslabs.fabric.feature.ModAddedFeatures;
import net.fabricmc.api.ModInitializer;

import net.countered.terrainslabs.TerrainSlabs;

public final class TerrainSlabsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ModAddedFeatures.registerFeatures();
        // Run our common setup.
        TerrainSlabs.init();
    }
}
