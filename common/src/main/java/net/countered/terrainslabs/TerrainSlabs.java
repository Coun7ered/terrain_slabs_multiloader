package net.countered.terrainslabs;

import net.countered.terrainslabs.block.ModBlocksRegistry;
import net.countered.terrainslabs.callbacks.PlaceOnTop;
import net.countered.terrainslabs.data.ontop.PlaceableItemsData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO datagen,
// worldgen,
// config,
// shovel path slabs,
// item tab img,
// raytrace fix,
// player place on top,
// textures (on top & blocks),
// on top placement fix (grass on stone...)
// tags for soil slabs
// water replace slabs
// fix on top placing on block when clicking side
public final class TerrainSlabs {

    public static final String MOD_ID = "terrain_slabs";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Terrain Slabs mod...");
        ModBlocksRegistry.registerModBlocks();
        PlaceableItemsData.init();
        PlaceOnTop.registerPlaceOnTopCallback();
    }
}
