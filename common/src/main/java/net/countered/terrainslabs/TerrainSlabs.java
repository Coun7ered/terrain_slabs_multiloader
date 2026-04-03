package net.countered.terrainslabs;

import net.countered.terrainslabs.callbacks.PlaceOnTop;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.registries.data.FlattenableData;
import net.countered.terrainslabs.registries.data.PlaceableItemsData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO
// worldgen (seagrass air pockets),
// on top placement fix (grass on stone...)
// tags for soil slabs & more
// add water replace slabs
// fix on top placing on block when clicking side
// fix grass turn snow when placed on top of it
// incorrect loottables for "terrain..." and ontop with silktouch
// fix snow not generating on slabs (also dont place on snow slabs?)
public final class TerrainSlabs {

    public static final String MOD_ID = "terrain_slabs";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Terrain Slabs mod...");
        ModBlocksRegistry.registerModBlocks();
        PlaceOnTop.registerPlaceOnTopCallback();
        FlattenableData.init();
        PlaceableItemsData.init();
    }
}
