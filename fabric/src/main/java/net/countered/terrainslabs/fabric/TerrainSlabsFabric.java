package net.countered.terrainslabs.fabric;

import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.fabric.feature.ModAddedFeatures;
import net.countered.terrainslabs.registries.ModFlattenablesRegistry;
import net.countered.terrainslabs.registries.ModTillableRegistry;
import net.fabricmc.api.ModInitializer;

public final class TerrainSlabsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // Run our common setup.
        TerrainSlabs.init();

        ModFlattenablesRegistry.registerFlattenables();
        ModTillableRegistry.registerTillables();

        ModAddedFeatures.registerFeatures();
    }
}
