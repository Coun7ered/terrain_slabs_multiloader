package net.countered.terrainslabs.registries;

import dev.architectury.registry.registries.RegistrySupplier;
import net.countered.terrainslabs.mixin.ShovelItemAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlattenableBlockRegistry {
    private static final List<Runnable> PENDING = new ArrayList<>();

    public static void register(RegistrySupplier<Block> from, RegistrySupplier<Block> to) {
        Map<Block, BlockState> flattenables = ShovelItemAccessor.getFlattenables();
        PENDING.add(() -> flattenables.put(from.get(), to.get().defaultBlockState()));
    }

    public static void apply() {
        PENDING.forEach(Runnable::run);
        PENDING.clear();
    }
}

