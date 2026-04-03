package net.countered.terrainslabs.registries;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.*;

/**
 * Registry for blocks that can be placed on slabs.
 */
public class PlaceableItemRegistry {

    private static final Map<Item, PlaceableItemData> REGISTRY = new HashMap<>();
    private static final List<Runnable> PENDING = new ArrayList<>();

    public static void register(Item vanillaItem, RegistrySupplier<Block> onTopBlock, PlaceableItemType type) {
        PENDING.add(() -> REGISTRY.put(vanillaItem, new PlaceableItemData(onTopBlock, type)));
    }

    public static void apply() {
        PENDING.forEach(Runnable::run);
        PENDING.clear();
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

    public record PlaceableItemData(RegistrySupplier<Block> onTopBlock, PlaceableItemType type) {
        public Block getBlock(){
            return onTopBlock.get();
        }
    }
}
