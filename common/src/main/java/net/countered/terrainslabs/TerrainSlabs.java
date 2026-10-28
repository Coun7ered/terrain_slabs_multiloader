package net.countered.terrainslabs;

import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.terrainslabs.config.TSConfig;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.registries.ModItemsRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TerrainSlabs {

    public static final String MOD_ID = "terrain_slabs";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Terrain Slabs mod...");
        MidnightConfig.init(MOD_ID, TSConfig.class);
        ModBlocksRegistry.registerModBlocks();
        ModItemsRegistry.registerModItems();
    }
}
