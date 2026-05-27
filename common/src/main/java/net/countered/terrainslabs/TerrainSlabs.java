package net.countered.terrainslabs;

import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.registries.ModItemsRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO
// fix podzol placed under slabs && in mud in mangrove
// fix slabs not being correct in lush caves
// add farmland slabs
// fix grass spread (grass to slabs, slabs to grass)
// fix leaf litter not generating on slabs
public final class TerrainSlabs {

    public static final String MOD_ID = "terrain_slabs";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Terrain Slabs mod...");
        ModBlocksRegistry.registerModBlocks();
        ModItemsRegistry.registerModItems();
    }
}
