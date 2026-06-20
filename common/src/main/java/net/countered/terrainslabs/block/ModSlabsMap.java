package net.countered.terrainslabs.block;

import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModSlabsMap {

    private static final Map<Block, Block> SLAB_MAP = new HashMap<>();
    // Soil slabs registered at runtime by addons via registerSoilSlab(...).
    private static final java.util.Set<Block> ADDON_SOIL_SLABS = new java.util.HashSet<>();

    static {
        SLAB_MAP.put(Blocks.STONE, ModBlocksRegistry.CUSTOM_STONE_SLAB.get());
        SLAB_MAP.put(Blocks.TUFF, ModBlocksRegistry.CUSTOM_TUFF_SLAB.get());
        SLAB_MAP.put(Blocks.SANDSTONE, ModBlocksRegistry.CUSTOM_SANDSTONE_SLAB.get());
        SLAB_MAP.put(Blocks.RED_SANDSTONE, ModBlocksRegistry.CUSTOM_RED_SANDSTONE_SLAB.get());
        SLAB_MAP.put(Blocks.ANDESITE, ModBlocksRegistry.CUSTOM_ANDESITE_SLAB.get());
        SLAB_MAP.put(Blocks.DIORITE, ModBlocksRegistry.CUSTOM_DIORITE_SLAB.get());
        SLAB_MAP.put(Blocks.GRANITE, ModBlocksRegistry.CUSTOM_GRANITE_SLAB.get());

        SLAB_MAP.put(Blocks.GRASS_BLOCK, ModBlocksRegistry.GRASS_SLAB.get());
        SLAB_MAP.put(Blocks.MYCELIUM, ModBlocksRegistry.MYCELIUM_SLAB.get());
        SLAB_MAP.put(Blocks.PODZOL, ModBlocksRegistry.PODZOL_SLAB.get());
        SLAB_MAP.put(Blocks.DIRT_PATH, ModBlocksRegistry.PATH_SLAB.get());

        SLAB_MAP.put(Blocks.GRAVEL, ModBlocksRegistry.GRAVEL_SLAB.get());
        SLAB_MAP.put(Blocks.SAND, ModBlocksRegistry.SAND_SLAB.get());
        SLAB_MAP.put(Blocks.RED_SAND, ModBlocksRegistry.RED_SAND_SLAB.get());

        SLAB_MAP.put(Blocks.DIRT, ModBlocksRegistry.DIRT_SLAB.get());
        SLAB_MAP.put(Blocks.MUD, ModBlocksRegistry.MUD_SLAB.get());
        SLAB_MAP.put(Blocks.COARSE_DIRT, ModBlocksRegistry.COARSE_SLAB.get());
        SLAB_MAP.put(Blocks.SNOW_BLOCK, ModBlocksRegistry.SNOW_SLAB.get());
        SLAB_MAP.put(Blocks.PACKED_ICE, ModBlocksRegistry.PACKED_ICE_SLAB.get());
        SLAB_MAP.put(Blocks.CLAY, ModBlocksRegistry.CLAY_SLAB.get());
        SLAB_MAP.put(Blocks.DEEPSLATE, ModBlocksRegistry.DEEPSLATE_SLAB.get());
        SLAB_MAP.put(Blocks.MOSS_BLOCK, ModBlocksRegistry.MOSS_SLAB.get());
        //terralith
        SLAB_MAP.put(Blocks.CALCITE, ModBlocksRegistry.CALCITE_SLAB.get());
        SLAB_MAP.put(Blocks.SMOOTH_BASALT, ModBlocksRegistry.SMOOTH_BASALT_SLAB.get());
        SLAB_MAP.put(Blocks.LIGHT_BLUE_TERRACOTTA, ModBlocksRegistry.LIGHT_BLUE_TERRACOTTA_SLAB.get());
        SLAB_MAP.put(Blocks.CYAN_TERRACOTTA, ModBlocksRegistry.CYAN_TERRACOTTA_SLAB.get());
        SLAB_MAP.put(Blocks.COBBLESTONE, ModBlocksRegistry.CUSTOM_COBBLESTONE_SLAB.get());
        SLAB_MAP.put(Blocks.MOSSY_COBBLESTONE, ModBlocksRegistry.CUSTOM_MOSSY_COBBLESTONE_SLAB.get());
        SLAB_MAP.put(Blocks.COBBLED_DEEPSLATE, ModBlocksRegistry.CUSTOM_COBBLED_DEEPSLATE_SLAB.get());
        SLAB_MAP.put(Blocks.ICE, ModBlocksRegistry.ICE_SLAB.get());
        SLAB_MAP.put(Blocks.ROOTED_DIRT, ModBlocksRegistry.ROOTED_DIRT_SLAB.get());
        SLAB_MAP.put(Blocks.PACKED_MUD, ModBlocksRegistry.PACKED_MUD_SLAB.get());
        SLAB_MAP.put(Blocks.BLUE_ICE, ModBlocksRegistry.BLUE_ICE_SLAB.get());
        SLAB_MAP.put(Blocks.BLACK_TERRACOTTA, ModBlocksRegistry.BLACK_TERRACOTTA_SLAB.get());
        SLAB_MAP.put(Blocks.PRISMARINE, ModBlocksRegistry.CUSTOM_PRISMARINE_SLAB.get());

        SLAB_MAP.put(Blocks.TERRACOTTA, ModBlocksRegistry.TERRACOTTA_SLAB.get());
        SLAB_MAP.put(Blocks.RED_TERRACOTTA, ModBlocksRegistry.RED_TERRACOTTA_SLAB.get());
        SLAB_MAP.put(Blocks.ORANGE_TERRACOTTA, ModBlocksRegistry.ORANGE_TERRACOTTA_SLAB.get());
        SLAB_MAP.put(Blocks.LIGHT_GRAY_TERRACOTTA, ModBlocksRegistry.LIGHT_GRAY_TERRACOTTA_SLAB.get());
        SLAB_MAP.put(Blocks.WHITE_TERRACOTTA, ModBlocksRegistry.WHITE_TERRACOTTA_SLAB.get());
        SLAB_MAP.put(Blocks.BROWN_TERRACOTTA, ModBlocksRegistry.BROWN_TERRACOTTA_SLAB.get());
        SLAB_MAP.put(Blocks.YELLOW_TERRACOTTA, ModBlocksRegistry.YELLOW_TERRACOTTA_SLAB.get());

        SLAB_MAP.put(Blocks.SOUL_SAND, ModBlocksRegistry.SOUL_SAND_SLAB.get());
        SLAB_MAP.put(Blocks.SOUL_SOIL, ModBlocksRegistry.SOUL_SOIL_SLAB.get());
        SLAB_MAP.put(Blocks.NETHERRACK, ModBlocksRegistry.NETHERRACK_SLAB.get());
        SLAB_MAP.put(Blocks.WARPED_NYLIUM, ModBlocksRegistry.WARPED_NYLIUM_SLAB.get());
        SLAB_MAP.put(Blocks.CRIMSON_NYLIUM, ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get());
        SLAB_MAP.put(Blocks.BASALT, ModBlocksRegistry.BASALT_SLAB.get());
        SLAB_MAP.put(Blocks.BLACKSTONE, ModBlocksRegistry.CUSTOM_BLACKSTONE_SLAB.get());
        SLAB_MAP.put(Blocks.END_STONE, ModBlocksRegistry.ENDSTONE_SLAB.get());
    }

    public static @Nullable Block getSlabForBlock(Block block) {
        return SLAB_MAP.get(block);
    }

    /**
     * Public compat API: register a terrain slab for a source block so that the
     * world-gen {@code SlabFeature} will place {@code slab} on top of / beside
     * naturally generated {@code source} blocks.
     *
     * <p>Intended to be called by addon mods during their own initialization
     * (e.g. a Fabric {@code ModInitializer} or a NeoForge mod constructor).
     * The lookup performed by the feature happens lazily at world-gen time, so
     * any mapping registered before the first chunk generates will take effect.
     *
     * <p>If {@code source} is a soil-type block whose slab should support the
     * grass/snow "on top" rendering and dirt-conversion behavior, register the
     * slab through {@link #registerSoilSlab(Block, Block)} instead.
     *
     * @param source the naturally generated full block (e.g. a modded dirt/sand/stone)
     * @param slab   the slab block to place; should extend
     *               {@link net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab}
     *               so the required {@code generated} blockstate property exists
     * @return {@code true} if this created a new mapping, {@code false} if it replaced one
     */
    public static boolean register(Block source, Block slab) {
        return SLAB_MAP.put(source, slab) == null;
    }

    /**
     * Like {@link #register(Block, Block)}, but also marks the slab as a soil slab,
     * meaning the feature will convert the block below to dirt before placing it and
     * fall back to a plain dirt slab when generating the top variant (matching the
     * behavior of the built-in grass/podzol/mycelium/path slabs).
     */
    public static boolean registerSoilSlab(Block source, Block slab) {
        boolean isNew = SLAB_MAP.put(source, slab) == null;
        ADDON_SOIL_SLABS.add(slab);
        return isNew;
    }

    /**
     * @return {@code true} if the given slab was registered as a soil slab,
     * either as a built-in ({@link #SOIL_SLAB_BLOCKS}) or via {@link #registerSoilSlab}.
     */
    public static boolean isSoilSlab(Block slab) {
        return SOIL_SLAB_BLOCKS.contains(slab) || ADDON_SOIL_SLABS.contains(slab);
    }

    /**
     * @return an unmodifiable view of the current source-block to slab mappings,
     * for inspection / debugging by addons.
     */
    public static Map<Block, Block> getMappings() {
        return java.util.Collections.unmodifiableMap(SLAB_MAP);
    }

    public static final Set<Block> SOIL_SLAB_BLOCKS = Set.of(
            ModBlocksRegistry.GRASS_SLAB.get(),
            ModBlocksRegistry.PODZOL_SLAB.get(),
            ModBlocksRegistry.MYCELIUM_SLAB.get(),
            ModBlocksRegistry.PATH_SLAB.get()
    );
}
