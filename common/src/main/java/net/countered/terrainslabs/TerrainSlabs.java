package net.countered.terrainslabs;

import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.registries.data.FlattenableData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO
// worldgen (seagrass air pockets, double tall grass lower replaced by slabs 1454210734568876835),
// tags for soil slabs & more
// add water replace slabs
// fix snow not generating on slabs (also dont generate snow on snow slabs & snowy grass?)
// fix ocean ice replaced by slabs
// fix mobs not spawning on slabs
// fix double tall plants on top
public final class TerrainSlabs {

    public static final String MOD_ID = "terrain_slabs";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Terrain Slabs mod...");
        ModBlocksRegistry.registerModBlocks();
        FlattenableData.init();
    }
}
