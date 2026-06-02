package net.countered.terrainslabs.registries;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.countered.terrainslabs.TerrainSlabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class ModItemsRegistry {
    private static final Logger LOGGER = LogManager.getLogger(TerrainSlabs.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(TerrainSlabs.MOD_ID, Registries.ITEM);

    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(TerrainSlabs.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> TERRAIN_SLABS_TAB = TABS.register(
            "terrain_slabs",
            () -> CreativeTabRegistry.create(
                    Component.translatable("itemGroup.terrain_slabs"),
                    () -> new ItemStack(ModItemsRegistry.GRASS_SLAB_ITEM.get())
            )
    );
    
    private static <T extends Item> RegistrySupplier<T> registerItem(
            String name,
            Function<Item.Properties, T> factory) {

        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                Identifier.parse(TerrainSlabs.MOD_ID + ":" + name)
        );
        return ITEMS.register(name, () -> factory.apply(
                new Item.Properties()
                        .setId(key)
                        .arch$tab(TERRAIN_SLABS_TAB)
        ));
    }

    // ITEMS
    public static final RegistrySupplier<Item> DIRT_SLAB_ITEM = registerItem(
            "dirt_slab",
            props -> new BlockItem(ModBlocksRegistry.DIRT_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> MUD_SLAB_ITEM = registerItem(
            "mud_slab",
            props -> new BlockItem(ModBlocksRegistry.MUD_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> FARMLAND_SLAB_ITEM = registerItem(
            "farmland_slab",
            props -> new BlockItem(ModBlocksRegistry.FARMLAND_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> COARSE_SLAB_ITEM = registerItem(
            "coarse_slab",
            props -> new BlockItem(ModBlocksRegistry.COARSE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> SNOW_SLAB_ITEM = registerItem(
            "snow_slab",
            props -> new BlockItem(ModBlocksRegistry.SNOW_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> PACKED_ICE_SLAB_ITEM = registerItem(
            "packed_ice_slab",
            props -> new BlockItem(ModBlocksRegistry.PACKED_ICE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> DEEPSLATE_SLAB_ITEM = registerItem(
            "deepslate_slab",
            props -> new BlockItem(ModBlocksRegistry.DEEPSLATE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CLAY_SLAB_ITEM = registerItem(
            "clay_slab",
            props -> new BlockItem(ModBlocksRegistry.CLAY_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> MOSS_SLAB_ITEM = registerItem(
            "moss_slab",
            props -> new BlockItem(ModBlocksRegistry.MOSS_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_TUFF_SLAB_ITEM = registerItem(
            "terrain_tuff_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_TUFF_SLAB.get(), props)
    );

    public static final RegistrySupplier<Item> GRASS_SLAB_ITEM = registerItem(
            "grass_slab",
            props -> new BlockItem(ModBlocksRegistry.GRASS_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> MYCELIUM_SLAB_ITEM = registerItem(
            "mycelium_slab",
            props -> new BlockItem(ModBlocksRegistry.MYCELIUM_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> PODZOL_SLAB_ITEM = registerItem(
            "podzol_slab",
            props -> new BlockItem(ModBlocksRegistry.PODZOL_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> PATH_SLAB_ITEM = registerItem(
            "path_slab",
            props -> new BlockItem(ModBlocksRegistry.PATH_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> GRAVEL_SLAB_ITEM = registerItem(
            "gravel_slab",
            props -> new BlockItem(ModBlocksRegistry.GRAVEL_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> SAND_SLAB_ITEM = registerItem(
            "sand_slab",
            props -> new BlockItem(ModBlocksRegistry.SAND_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> RED_SAND_SLAB_ITEM = registerItem(
            "red_sand_slab",
            props -> new BlockItem(ModBlocksRegistry.RED_SAND_SLAB.get(), props)
    );

    public static final RegistrySupplier<Item> TERRACOTTA_SLAB_ITEM = registerItem(
            "terracotta_slab",
            props -> new BlockItem(ModBlocksRegistry.TERRACOTTA_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> RED_TERRACOTTA_SLAB_ITEM = registerItem(
            "red_terracotta_slab",
            props -> new BlockItem(ModBlocksRegistry.RED_TERRACOTTA_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> ORANGE_TERRACOTTA_SLAB_ITEM = registerItem(
            "orange_terracotta_slab",
            props -> new BlockItem(ModBlocksRegistry.ORANGE_TERRACOTTA_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> LIGHT_GRAY_TERRACOTTA_SLAB_ITEM = registerItem(
            "light_gray_terracotta_slab",
            props -> new BlockItem(ModBlocksRegistry.LIGHT_GRAY_TERRACOTTA_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> WHITE_TERRACOTTA_SLAB_ITEM = registerItem(
            "white_terracotta_slab",
            props -> new BlockItem(ModBlocksRegistry.WHITE_TERRACOTTA_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> BROWN_TERRACOTTA_SLAB_ITEM = registerItem(
            "brown_terracotta_slab",
            props -> new BlockItem(ModBlocksRegistry.BROWN_TERRACOTTA_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> YELLOW_TERRACOTTA_SLAB_ITEM = registerItem(
            "yellow_terracotta_slab",
            props -> new BlockItem(ModBlocksRegistry.YELLOW_TERRACOTTA_SLAB.get(), props)
    );

    public static final RegistrySupplier<Item> CUSTOM_STONE_SLAB_ITEM = registerItem(
            "terrain_stone_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_STONE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_SANDSTONE_SLAB_ITEM = registerItem(
            "terrain_sandstone_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_SANDSTONE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_RED_SANDSTONE_SLAB_ITEM = registerItem(
            "terrain_red_sandstone_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_RED_SANDSTONE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_ANDESITE_SLAB_ITEM = registerItem(
            "terrain_andesite_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_ANDESITE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_DIORITE_SLAB_ITEM = registerItem(
            "terrain_diorite_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_DIORITE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_GRANITE_SLAB_ITEM = registerItem(
            "terrain_granite_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_GRANITE_SLAB.get(), props)
    );

    public static final RegistrySupplier<Item> SOUL_SAND_SLAB_ITEM = registerItem(
            "soul_sand_slab",
            props -> new BlockItem(ModBlocksRegistry.SOUL_SAND_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> SOUL_SOIL_SLAB_ITEM = registerItem(
            "soul_soil_slab",
            props -> new BlockItem(ModBlocksRegistry.SOUL_SOIL_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> NETHERRACK_SLAB_ITEM = registerItem(
            "netherrack_slab",
            props -> new BlockItem(ModBlocksRegistry.NETHERRACK_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> WARPED_NYLIUM_SLAB_ITEM = registerItem(
            "warped_nylium_slab",
            props -> new BlockItem(ModBlocksRegistry.WARPED_NYLIUM_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CRIMSON_NYLIUM_SLAB_ITEM = registerItem(
            "crimson_nylium_slab",
            props -> new BlockItem(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> BASALT_SLAB_ITEM = registerItem(
            "basalt_slab",
            props -> new BlockItem(ModBlocksRegistry.BASALT_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_BLACKSTONE_SLAB_ITEM = registerItem(
            "terrain_blackstone_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_BLACKSTONE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> ENDSTONE_SLAB_ITEM = registerItem(
            "endstone_slab",
            props -> new BlockItem(ModBlocksRegistry.ENDSTONE_SLAB.get(), props)
    );

    public static final RegistrySupplier<Item> CALCITE_SLAB_ITEM = registerItem(
            "calcite_slab",
            props -> new BlockItem(ModBlocksRegistry.CALCITE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> SMOOTH_BASALT_SLAB_ITEM = registerItem(
            "smooth_basalt_slab",
            props -> new BlockItem(ModBlocksRegistry.SMOOTH_BASALT_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> LIGHT_BLUE_TERRACOTTA_SLAB_ITEM = registerItem(
            "light_blue_terracotta_slab",
            props -> new BlockItem(ModBlocksRegistry.LIGHT_BLUE_TERRACOTTA_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CYAN_TERRACOTTA_SLAB_ITEM = registerItem(
            "cyan_terracotta_slab",
            props -> new BlockItem(ModBlocksRegistry.CYAN_TERRACOTTA_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_COBBLESTONE_SLAB_ITEM = registerItem(
            "terrain_cobblestone_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_COBBLESTONE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_MOSSY_COBBLESTONE_SLAB_ITEM = registerItem(
            "terrain_mossy_cobblestone_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_MOSSY_COBBLESTONE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_COBBLED_DEEPSLATE_SLAB_ITEM = registerItem(
            "terrain_cobbled_deepslate_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_COBBLED_DEEPSLATE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> ICE_SLAB_ITEM = registerItem(
            "ice_slab",
            props -> new BlockItem(ModBlocksRegistry.ICE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> ROOTED_DIRT_SLAB_ITEM = registerItem(
            "rooted_dirt_slab",
            props -> new BlockItem(ModBlocksRegistry.ROOTED_DIRT_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> PACKED_MUD_SLAB_ITEM = registerItem(
            "packed_mud_slab",
            props -> new BlockItem(ModBlocksRegistry.PACKED_MUD_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> BLUE_ICE_SLAB_ITEM = registerItem(
            "blue_ice_slab",
            props -> new BlockItem(ModBlocksRegistry.BLUE_ICE_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> BLACK_TERRACOTTA_SLAB_ITEM = registerItem(
            "black_terracotta_slab",
            props -> new BlockItem(ModBlocksRegistry.BLACK_TERRACOTTA_SLAB.get(), props)
    );
    public static final RegistrySupplier<Item> CUSTOM_PRISMARINE_SLAB_ITEM = registerItem(
            "terrain_prismarine_slab",
            props -> new BlockItem(ModBlocksRegistry.CUSTOM_PRISMARINE_SLAB.get(), props)
    );

    public static void registerModItems() {
        LOGGER.info("Registering Mod Blocks & Items for " + TerrainSlabs.MOD_ID);
        TABS.register();
        ITEMS.register();
    }
}
