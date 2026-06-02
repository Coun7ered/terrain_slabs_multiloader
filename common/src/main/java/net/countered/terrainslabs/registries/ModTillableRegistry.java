package net.countered.terrainslabs.registries;

import dev.architectury.hooks.item.tool.HoeItemHooks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class ModTillableRegistry {

    private static final Map<Block, BlockState> tillablesMap = new HashMap<>(
            Map.of(
                    ModBlocksRegistry.GRASS_SLAB.get(), ModBlocksRegistry.FARMLAND_SLAB.get().defaultBlockState(),
                    ModBlocksRegistry.PATH_SLAB.get(), ModBlocksRegistry.FARMLAND_SLAB.get().defaultBlockState(),
                    ModBlocksRegistry.DIRT_SLAB.get(), ModBlocksRegistry.FARMLAND_SLAB.get().defaultBlockState(),
                    ModBlocksRegistry.COARSE_SLAB.get(), ModBlocksRegistry.FARMLAND_SLAB.get().defaultBlockState(),
                    ModBlocksRegistry.ROOTED_DIRT_SLAB.get(), ModBlocksRegistry.FARMLAND_SLAB.get().defaultBlockState()
            )
    );

    public static void addTillable(Block input, BlockState tillable) {
        tillablesMap.put(input, tillable);
    }

    public static void registerTillables() {
        for (Map.Entry<Block, BlockState> entry : tillablesMap.entrySet()) {
            HoeItemHooks.addTillable(
                    entry.getKey(),
                    (ctx) -> true,
                    (context) -> {},
                    (context) -> entry.getValue()
            );
        }
    }
}
