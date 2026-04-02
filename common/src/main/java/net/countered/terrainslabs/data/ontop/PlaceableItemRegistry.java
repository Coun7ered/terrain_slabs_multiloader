package net.countered.terrainslabs.data.ontop;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Registry for items that can be placed on slabs.
 */
public class PlaceableItemRegistry {

    private static final Map<Item, PlaceableItemData> REGISTRY = new HashMap<>();

    public static void register(Item vanillaItem, Supplier<Block> onTopBlock, PlaceableItemType type) {
        REGISTRY.put(vanillaItem, new PlaceableItemData(onTopBlock, type));
    }

    public static PlaceableItemData get(Item item) {
        return REGISTRY.get(item);
    }

    public static boolean isRegistered(Item item) {
        return REGISTRY.containsKey(item);
    }

    public static Map<Item, PlaceableItemData> getAll() {
        return new HashMap<>(REGISTRY);
    }

    public enum PlaceableItemType {
        SNOW,           // Snow
        VEGETATION,     // Flowers, Grass, etc
        AQUATIC         // Underwater
    }

    public record PlaceableItemData(Supplier<Block> onTopBlock, PlaceableItemType type) {
        public Block getBlock() {
            return onTopBlock.get();
        }
    }
}
