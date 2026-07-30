package net.countered.terrainslabs.registries;

import dev.architectury.hooks.item.tool.HoeItemHooks;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
            try {
                HoeItemHooks.addTillable(
                        entry.getKey(),
                        (ctx) -> true,
                        (context) -> {},
                        (context) -> entry.getValue()
                );
            } catch (IllegalAccessError e) {
                // Fallback for Fabric where module access restrictions prevent Architectury hooks
                addTillableReflection(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void addTillableReflection(Block input, BlockState output) {
        try {
            var field = net.minecraft.world.item.HoeItem.class.getDeclaredField("TILLABLES");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<Block, Pair<Predicate<net.minecraft.world.item.context.UseOnContext>, Consumer<net.minecraft.world.item.context.UseOnContext>>> tillables = 
                    (Map<Block, Pair<Predicate<net.minecraft.world.item.context.UseOnContext>, Consumer<net.minecraft.world.item.context.UseOnContext>>>) field.get(null);
            
            Pair<Predicate<net.minecraft.world.item.context.UseOnContext>, Consumer<net.minecraft.world.item.context.UseOnContext>> pair = 
                    new Pair<>(
                            ctx -> true,
                            ctx -> {
                                if (ctx.getLevel() != null && !ctx.getLevel().isClientSide()) {
                                    ctx.getLevel().setBlock(ctx.getClickedPos(), output, 11);
                                }
                            }
                    );
            
            tillables.put(input, pair);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to register tillable block via reflection", e);
        }
    }
}
