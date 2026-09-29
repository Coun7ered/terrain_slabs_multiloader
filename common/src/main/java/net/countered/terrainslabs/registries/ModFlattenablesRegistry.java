package net.countered.terrainslabs.registries;

import dev.architectury.hooks.item.tool.BlockTransformerHooks;
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
            BlockTransformerHooks.addFlattenable(entry.getKey(), entry.getValue());
        }
    }
}
