package net.countered.terrainslabs.registries;

import dev.architectury.hooks.item.tool.ShovelItemHooks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class ModFlattenablesRegistry {

    private static final Map<Block, BlockState> flattenablesMap = new HashMap<>(
            Map.of(
                    ModBlocksRegistry.DIRT_SLAB.get(), ModBlocksRegistry.PATH_SLAB.get().defaultBlockState(),
                    ModBlocksRegistry.GRASS_SLAB.get(), ModBlocksRegistry.PATH_SLAB.get().defaultBlockState(),
                    ModBlocksRegistry.PODZOL_SLAB.get(), ModBlocksRegistry.PATH_SLAB.get().defaultBlockState(),
                    ModBlocksRegistry.MYCELIUM_SLAB.get(), ModBlocksRegistry.PATH_SLAB.get().defaultBlockState()
            )
    );

    public static void addFlattenable(Block input, BlockState flattened) {
        flattenablesMap.put(input, flattened);
    }

    public static void registerFlattenables() {
        for (Map.Entry<Block, BlockState> entry : flattenablesMap.entrySet()) {
            try {
                ShovelItemHooks.addFlattenable(entry.getKey(), entry.getValue());
            } catch (IllegalAccessError e) {
                // Fallback for Fabric where module access restrictions prevent Architectury hooks
                addFlattenableReflection(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void addFlattenableReflection(Block input, BlockState output) {
        try {
            var field = net.minecraft.world.item.ShovelItem.class.getDeclaredField("FLATTENABLES");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<Block, BlockState> flattenables = (Map<Block, BlockState>) field.get(null);
            flattenables.put(input, output);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to register flattenable block via reflection", e);
        }
    }
}
