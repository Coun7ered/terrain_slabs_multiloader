package net.countered.terrainslabs;

import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.registries.ModItemsRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO
// fix podzol placed under slabs && in mud in mangrove
// fix leaf litter not generating on slabs
// fix powder snow replaced by slabs
// fix slabs next to lava in deltas
public final class TerrainSlabs {

    public static final String MOD_ID = "terrain_slabs";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Terrain Slabs mod...");
        ModBlocksRegistry.registerModBlocks();
        ModItemsRegistry.registerModItems();
    }
}
