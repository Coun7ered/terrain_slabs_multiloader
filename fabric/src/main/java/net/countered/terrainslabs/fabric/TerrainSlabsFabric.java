package net.countered.terrainslabs.fabric;

import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.fabric.feature.ModAddedFeatures;
import net.countered.terrainslabs.generation.ChunkPostProcessor;
import net.countered.terrainslabs.platform.fabric.PlatformConfigHooksImpl;
import net.countered.terrainslabs.registries.FlattenableBlockRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;

public final class TerrainSlabsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        TerrainSlabs.init();

        ModAddedFeatures.registerFeatures();
        MidnightConfig.init(TerrainSlabs.MOD_ID, PlatformConfigHooksImpl.class);

        FlattenableBlockRegistry.apply();

        // Post generation processing
        ServerChunkEvents.CHUNK_GENERATE.register((level, chunk) ->
                new ChunkPostProcessor(level, chunk).process());
    }
}
