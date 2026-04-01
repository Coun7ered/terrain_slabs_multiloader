package net.countered.terrainslabs.fabric;

import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.platform.fabric.PlatformConfigHooksImpl;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.fabric.feature.ModAddedFeatures;
import net.fabricmc.api.ModInitializer;

public final class TerrainSlabsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ModAddedFeatures.registerFeatures();
        MidnightConfig.init(TerrainSlabs.MOD_ID, PlatformConfigHooksImpl.class);

        // Run our common setup.
        TerrainSlabs.init();
    }
}
