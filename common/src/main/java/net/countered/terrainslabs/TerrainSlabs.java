package net.countered.terrainslabs;

import net.countered.terrainslabs.block.ModBlocksRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO datagen, worldgen, config, shovel path slabs, item tab img,
public final class TerrainSlabs {
    public static final String MOD_ID = "terrain_slabs";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Terrain Slabs mod...");
        ModBlocksRegistry.registerModBlocks();
        // Write common init code here.
    }
}
