package net.countered.terrainslabs.data.ontop;

import net.countered.terrainslabs.block.ModBlocksRegistry;
import net.minecraft.world.item.Items;

/**
 * Registriert alle Vanilla-Items, die auf Slabs platziert werden können.
 */
public class PlaceableItemsData {

    public static void init() {
        // Snow
        PlaceableItemRegistry.register(
                Items.SNOW,
                ModBlocksRegistry.SNOW_ON_TOP,
                PlaceableItemRegistry.PlaceableItemType.SNOW
        );
        
        PlaceableItemRegistry.register(
                Items.POPPY, 
                ModBlocksRegistry.POPPY_ON_TOP, 
                PlaceableItemRegistry.PlaceableItemType.VEGETATION
        );
        PlaceableItemRegistry.register(
                Items.DANDELION, 
                ModBlocksRegistry.DANDELION_ON_TOP, 
                PlaceableItemRegistry.PlaceableItemType.VEGETATION
        );
        PlaceableItemRegistry.register(
                Items.AZURE_BLUET, 
                ModBlocksRegistry.AZURE_BLUET_ON_TOP, 
                PlaceableItemRegistry.PlaceableItemType.VEGETATION
        );
        PlaceableItemRegistry.register(
                Items.CORNFLOWER,
                ModBlocksRegistry.CORNFLOWER_ON_TOP,
                PlaceableItemRegistry.PlaceableItemType.VEGETATION
        );

        // Vegetation
        PlaceableItemRegistry.register(
                Items.GRASS,
                ModBlocksRegistry.SHORT_GRASS_ON_TOP,
                PlaceableItemRegistry.PlaceableItemType.VEGETATION
        );
        PlaceableItemRegistry.register(
                Items.FERN,
                ModBlocksRegistry.FERN_ON_TOP,
                PlaceableItemRegistry.PlaceableItemType.VEGETATION
        );
        PlaceableItemRegistry.register(
                Items.DEAD_BUSH,
                ModBlocksRegistry.DEAD_BUSH_ON_TOP,
                PlaceableItemRegistry.PlaceableItemType.VEGETATION
        );
        PlaceableItemRegistry.register(
                Items.BROWN_MUSHROOM,
                ModBlocksRegistry.BROWN_MUSHROOM_ON_TOP,
                PlaceableItemRegistry.PlaceableItemType.VEGETATION
        );
        PlaceableItemRegistry.register(
                Items.RED_MUSHROOM,
                ModBlocksRegistry.RED_MUSHROOM_ON_TOP,
                PlaceableItemRegistry.PlaceableItemType.VEGETATION
        );

        // Aquatic
        PlaceableItemRegistry.register(
                Items.SEAGRASS,
                ModBlocksRegistry.SEAGRASS_ON_TOP,
                PlaceableItemRegistry.PlaceableItemType.AQUATIC
        );
    }
}
