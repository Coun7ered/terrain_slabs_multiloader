package net.countered.terrainslabs.registries;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.block.customslabs.soilslabs.GrassSlab;
import net.countered.terrainslabs.block.customslabs.soilslabs.MyceliumSlab;
import net.countered.terrainslabs.block.customslabs.soilslabs.PathSlab;
import net.countered.terrainslabs.block.customslabs.soilslabs.PodzolSlab;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.block.customslabs.specialslabs.GravityAffectedSlab;
import net.countered.terrainslabs.block.customslabs.specialslabs.MudSlab;
import net.countered.terrainslabs.block.customslabs.specialslabs.dimensions.NetherrackSlab;
import net.countered.terrainslabs.block.customslabs.specialslabs.dimensions.NyliumSlab;
import net.countered.terrainslabs.block.customslabs.specialslabs.dimensions.SoulSandSlab;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("UnstableApiUsage")
public class ModBlocksRegistry {
    private static final Logger LOGGER = LogManager.getLogger(TerrainSlabs.MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(TerrainSlabs.MOD_ID, Registries.BLOCK);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(TerrainSlabs.MOD_ID, Registries.ITEM);

    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(TerrainSlabs.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> TERRAIN_SLABS_TAB = TABS.register(
            "terrain_slabs",
            () -> CreativeTabRegistry.create(
                    Component.translatable("itemGroup.terrain_slabs"),
                    () -> new ItemStack(ModBlocksRegistry.GRASS_SLAB_ITEM.get())
            )
    );

    // BLOCKS
    public static final RegistrySupplier<Block> DIRT_SLAB = BLOCKS.register("dirt_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.DIRT)));
    public static final RegistrySupplier<Block> MUD_SLAB = BLOCKS.register("mud_slab",
            () -> new MudSlab(BlockBehaviour.Properties.copy(Blocks.MUD).noOcclusion()));
    public static final RegistrySupplier<Block> COARSE_SLAB = BLOCKS.register("coarse_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.COARSE_DIRT)));
    public static final RegistrySupplier<Block> SNOW_SLAB = BLOCKS.register("snow_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK)));
    public static final RegistrySupplier<Block> PACKED_ICE_SLAB = BLOCKS.register("packed_ice_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.PACKED_ICE)));
    public static final RegistrySupplier<Block> DEEPSLATE_SLAB = BLOCKS.register("deepslate_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final RegistrySupplier<Block> CLAY_SLAB = BLOCKS.register("clay_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.CLAY)));
    public static final RegistrySupplier<Block> MOSS_SLAB = BLOCKS.register("moss_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK)));
    public static final RegistrySupplier<Block> CUSTOM_TUFF_SLAB = BLOCKS.register("terrain_tuff_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.TUFF)));

    public static final RegistrySupplier<Block> GRASS_SLAB = BLOCKS.register("grass_slab",
            () -> new GrassSlab(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)));
    public static final RegistrySupplier<Block> MYCELIUM_SLAB = BLOCKS.register("mycelium_slab",
            () -> new MyceliumSlab(BlockBehaviour.Properties.copy(Blocks.MYCELIUM)));
    public static final RegistrySupplier<Block> PODZOL_SLAB = BLOCKS.register("podzol_slab",
            () -> new PodzolSlab(BlockBehaviour.Properties.copy(Blocks.PODZOL)));
    public static final RegistrySupplier<Block> PATH_SLAB = BLOCKS.register("path_slab",
            () -> new PathSlab(BlockBehaviour.Properties.copy(Blocks.DIRT_PATH).noOcclusion()));

    public static final RegistrySupplier<Block> GRAVEL_SLAB = BLOCKS.register("gravel_slab",
            () -> new GravityAffectedSlab(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
    public static final RegistrySupplier<Block> SAND_SLAB = BLOCKS.register("sand_slab",
            () -> new GravityAffectedSlab(BlockBehaviour.Properties.copy(Blocks.SAND)));
    public static final RegistrySupplier<Block> RED_SAND_SLAB = BLOCKS.register("red_sand_slab",
            () -> new GravityAffectedSlab(BlockBehaviour.Properties.copy(Blocks.RED_SAND)));

    public static final RegistrySupplier<Block> TERRACOTTA_SLAB = BLOCKS.register("terracotta_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)));
    public static final RegistrySupplier<Block> RED_TERRACOTTA_SLAB = BLOCKS.register("red_terracotta_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA)));
    public static final RegistrySupplier<Block> ORANGE_TERRACOTTA_SLAB = BLOCKS.register("orange_terracotta_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.ORANGE_TERRACOTTA)));
    public static final RegistrySupplier<Block> LIGHT_GRAY_TERRACOTTA_SLAB = BLOCKS.register("light_gray_terracotta_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_TERRACOTTA)));
    public static final RegistrySupplier<Block> WHITE_TERRACOTTA_SLAB = BLOCKS.register("white_terracotta_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.WHITE_TERRACOTTA)));
    public static final RegistrySupplier<Block> BROWN_TERRACOTTA_SLAB = BLOCKS.register("brown_terracotta_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.BROWN_TERRACOTTA)));
    public static final RegistrySupplier<Block> YELLOW_TERRACOTTA_SLAB = BLOCKS.register("yellow_terracotta_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.YELLOW_TERRACOTTA)));

    public static final RegistrySupplier<Block> CUSTOM_STONE_SLAB = BLOCKS.register("terrain_stone_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistrySupplier<Block> CUSTOM_SANDSTONE_SLAB = BLOCKS.register("terrain_sandstone_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.SANDSTONE_SLAB)));
    public static final RegistrySupplier<Block> CUSTOM_RED_SANDSTONE_SLAB = BLOCKS.register("terrain_red_sandstone_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE_SLAB)));
    public static final RegistrySupplier<Block> CUSTOM_ANDESITE_SLAB = BLOCKS.register("terrain_andesite_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.ANDESITE_SLAB)));
    public static final RegistrySupplier<Block> CUSTOM_DIORITE_SLAB = BLOCKS.register("terrain_diorite_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.DIORITE_SLAB)));
    public static final RegistrySupplier<Block> CUSTOM_GRANITE_SLAB = BLOCKS.register("terrain_granite_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.GRANITE_SLAB)));

    public static final RegistrySupplier<Block> SOUL_SAND_SLAB = BLOCKS.register("soul_sand_slab",
            () -> new SoulSandSlab(BlockBehaviour.Properties.copy(Blocks.SOUL_SAND).noOcclusion()));
    public static final RegistrySupplier<Block> SOUL_SOIL_SLAB = BLOCKS.register("soul_soil_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.SOUL_SOIL)));
    public static final RegistrySupplier<Block> NETHERRACK_SLAB = BLOCKS.register("netherrack_slab",
            () -> new NetherrackSlab(BlockBehaviour.Properties.copy(Blocks.NETHERRACK)));
    public static final RegistrySupplier<Block> WARPED_NYLIUM_SLAB = BLOCKS.register("warped_nylium_slab",
            () -> new NyliumSlab(BlockBehaviour.Properties.copy(Blocks.WARPED_NYLIUM)));
    public static final RegistrySupplier<Block> CRIMSON_NYLIUM_SLAB = BLOCKS.register("crimson_nylium_slab",
            () -> new NyliumSlab(BlockBehaviour.Properties.copy(Blocks.CRIMSON_NYLIUM)));
    public static final RegistrySupplier<Block> BASALT_SLAB = BLOCKS.register("basalt_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.BASALT)));
    public static final RegistrySupplier<Block> CUSTOM_BLACKSTONE_SLAB = BLOCKS.register("terrain_blackstone_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE_SLAB)));
    public static final RegistrySupplier<Block> ENDSTONE_SLAB = BLOCKS.register("endstone_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.END_STONE)));

    // VANILLA BLOCKS USED BY TERRALITH
    public static final RegistrySupplier<Block> CALCITE_SLAB = BLOCKS.register("calcite_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.CALCITE)));
    public static final RegistrySupplier<Block> SMOOTH_BASALT_SLAB = BLOCKS.register("smooth_basalt_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.SMOOTH_BASALT)));
    public static final RegistrySupplier<Block> LIGHT_BLUE_TERRACOTTA_SLAB = BLOCKS.register("light_blue_terracotta_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.LIGHT_BLUE_TERRACOTTA)));
    public static final RegistrySupplier<Block> CYAN_TERRACOTTA_SLAB = BLOCKS.register("cyan_terracotta_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.CYAN_TERRACOTTA)));
    public static final RegistrySupplier<Block> CUSTOM_COBBLESTONE_SLAB = BLOCKS.register("terrain_cobblestone_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE_SLAB)));
    public static final RegistrySupplier<Block> CUSTOM_MOSSY_COBBLESTONE_SLAB = BLOCKS.register("terrain_mossy_cobblestone_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.MOSSY_COBBLESTONE_SLAB)));
    public static final RegistrySupplier<Block> CUSTOM_COBBLED_DEEPSLATE_SLAB = BLOCKS.register("terrain_cobbled_deepslate_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> ICE_SLAB = BLOCKS.register("ice_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.ICE)));
    public static final RegistrySupplier<Block> ROOTED_DIRT_SLAB = BLOCKS.register("rooted_dirt_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.ROOTED_DIRT)));
    public static final RegistrySupplier<Block> PACKED_MUD_SLAB = BLOCKS.register("packed_mud_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.PACKED_MUD)));
    public static final RegistrySupplier<Block> BLUE_ICE_SLAB = BLOCKS.register("blue_ice_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.BLUE_ICE)));
    public static final RegistrySupplier<Block> BLACK_TERRACOTTA_SLAB = BLOCKS.register("black_terracotta_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.BLACK_TERRACOTTA)));
    public static final RegistrySupplier<Block> CUSTOM_PRISMARINE_SLAB = BLOCKS.register("terrain_prismarine_slab",
            () -> new CustomSlab(BlockBehaviour.Properties.copy(Blocks.PRISMARINE_SLAB)));

    // ITEMS
    public static final RegistrySupplier<Item> DIRT_SLAB_ITEM = ITEMS.register("dirt_slab",
            () -> new BlockItem(DIRT_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> MUD_SLAB_ITEM = ITEMS.register("mud_slab",
            () -> new BlockItem(MUD_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> COARSE_SLAB_ITEM = ITEMS.register("coarse_slab",
            () -> new BlockItem(COARSE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> SNOW_SLAB_ITEM = ITEMS.register("snow_slab",
            () -> new BlockItem(SNOW_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> PACKED_ICE_SLAB_ITEM = ITEMS.register("packed_ice_slab",
            () -> new BlockItem(PACKED_ICE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> DEEPSLATE_SLAB_ITEM = ITEMS.register("deepslate_slab",
            () -> new BlockItem(DEEPSLATE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CLAY_SLAB_ITEM = ITEMS.register("clay_slab",
            () -> new BlockItem(CLAY_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> MOSS_SLAB_ITEM = ITEMS.register("moss_slab",
            () -> new BlockItem(MOSS_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_TUFF_SLAB_ITEM = ITEMS.register("terrain_tuff_slab",
            () -> new BlockItem(CUSTOM_TUFF_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));

    public static final RegistrySupplier<Item> GRASS_SLAB_ITEM = ITEMS.register("grass_slab",
            () -> new BlockItem(GRASS_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> MYCELIUM_SLAB_ITEM = ITEMS.register("mycelium_slab",
            () -> new BlockItem(MYCELIUM_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> PODZOL_SLAB_ITEM = ITEMS.register("podzol_slab",
            () -> new BlockItem(PODZOL_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> PATH_SLAB_ITEM = ITEMS.register("path_slab",
            () -> new BlockItem(PATH_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));

    public static final RegistrySupplier<Item> GRAVEL_SLAB_ITEM = ITEMS.register("gravel_slab",
            () -> new BlockItem(GRAVEL_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> SAND_SLAB_ITEM = ITEMS.register("sand_slab",
            () -> new BlockItem(SAND_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> RED_SAND_SLAB_ITEM = ITEMS.register("red_sand_slab",
            () -> new BlockItem(RED_SAND_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));

    public static final RegistrySupplier<Item> TERRACOTTA_SLAB_ITEM = ITEMS.register("terracotta_slab",
            () -> new BlockItem(TERRACOTTA_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> RED_TERRACOTTA_SLAB_ITEM = ITEMS.register("red_terracotta_slab",
            () -> new BlockItem(RED_TERRACOTTA_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> ORANGE_TERRACOTTA_SLAB_ITEM = ITEMS.register("orange_terracotta_slab",
            () -> new BlockItem(ORANGE_TERRACOTTA_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> LIGHT_GRAY_TERRACOTTA_SLAB_ITEM = ITEMS.register("light_gray_terracotta_slab",
            () -> new BlockItem(LIGHT_GRAY_TERRACOTTA_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> WHITE_TERRACOTTA_SLAB_ITEM = ITEMS.register("white_terracotta_slab",
            () -> new BlockItem(WHITE_TERRACOTTA_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> BROWN_TERRACOTTA_SLAB_ITEM = ITEMS.register("brown_terracotta_slab",
            () -> new BlockItem(BROWN_TERRACOTTA_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> YELLOW_TERRACOTTA_SLAB_ITEM = ITEMS.register("yellow_terracotta_slab",
            () -> new BlockItem(YELLOW_TERRACOTTA_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));

    public static final RegistrySupplier<Item> CUSTOM_STONE_SLAB_ITEM = ITEMS.register("terrain_stone_slab",
            () -> new BlockItem(CUSTOM_STONE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_SANDSTONE_SLAB_ITEM = ITEMS.register("terrain_sandstone_slab",
            () -> new BlockItem(CUSTOM_SANDSTONE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_RED_SANDSTONE_SLAB_ITEM = ITEMS.register("terrain_red_sandstone_slab",
            () -> new BlockItem(CUSTOM_RED_SANDSTONE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_ANDESITE_SLAB_ITEM = ITEMS.register("terrain_andesite_slab",
            () -> new BlockItem(CUSTOM_ANDESITE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_DIORITE_SLAB_ITEM = ITEMS.register("terrain_diorite_slab",
            () -> new BlockItem(CUSTOM_DIORITE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_GRANITE_SLAB_ITEM = ITEMS.register("terrain_granite_slab",
            () -> new BlockItem(CUSTOM_GRANITE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));

    public static final RegistrySupplier<Item> SOUL_SAND_SLAB_ITEM = ITEMS.register("soul_sand_slab",
            () -> new BlockItem(SOUL_SAND_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> SOUL_SOIL_SLAB_ITEM = ITEMS.register("soul_soil_slab",
            () -> new BlockItem(SOUL_SOIL_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> NETHERRACK_SLAB_ITEM = ITEMS.register("netherrack_slab",
            () -> new BlockItem(NETHERRACK_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> WARPED_NYLIUM_SLAB_ITEM = ITEMS.register("warped_nylium_slab",
            () -> new BlockItem(WARPED_NYLIUM_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CRIMSON_NYLIUM_SLAB_ITEM = ITEMS.register("crimson_nylium_slab",
            () -> new BlockItem(CRIMSON_NYLIUM_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> BASALT_SLAB_ITEM = ITEMS.register("basalt_slab",
            () -> new BlockItem(BASALT_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_BLACKSTONE_SLAB_ITEM = ITEMS.register("terrain_blackstone_slab",
            () -> new BlockItem(CUSTOM_BLACKSTONE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> ENDSTONE_SLAB_ITEM = ITEMS.register("endstone_slab",
            () -> new BlockItem(ENDSTONE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));

    public static final RegistrySupplier<Item> CALCITE_SLAB_ITEM = ITEMS.register("calcite_slab",
            () -> new BlockItem(CALCITE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> SMOOTH_BASALT_SLAB_ITEM = ITEMS.register("smooth_basalt_slab",
            () -> new BlockItem(SMOOTH_BASALT_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> LIGHT_BLUE_TERRACOTTA_SLAB_ITEM = ITEMS.register("light_blue_terracotta_slab",
            () -> new BlockItem(LIGHT_BLUE_TERRACOTTA_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CYAN_TERRACOTTA_SLAB_ITEM = ITEMS.register("cyan_terracotta_slab",
            () -> new BlockItem(CYAN_TERRACOTTA_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_COBBLESTONE_SLAB_ITEM = ITEMS.register("terrain_cobblestone_slab",
            () -> new BlockItem(CUSTOM_COBBLESTONE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_MOSSY_COBBLESTONE_SLAB_ITEM = ITEMS.register("terrain_mossy_cobblestone_slab",
            () -> new BlockItem(CUSTOM_MOSSY_COBBLESTONE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_COBBLED_DEEPSLATE_SLAB_ITEM = ITEMS.register("terrain_cobbled_deepslate_slab",
            () -> new BlockItem(CUSTOM_COBBLED_DEEPSLATE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> ICE_SLAB_ITEM = ITEMS.register("ice_slab",
            () -> new BlockItem(ICE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> ROOTED_DIRT_SLAB_ITEM = ITEMS.register("rooted_dirt_slab",
            () -> new BlockItem(ROOTED_DIRT_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> PACKED_MUD_SLAB_ITEM = ITEMS.register("packed_mud_slab",
            () -> new BlockItem(PACKED_MUD_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> BLUE_ICE_SLAB_ITEM = ITEMS.register("blue_ice_slab",
            () -> new BlockItem(BLUE_ICE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> BLACK_TERRACOTTA_SLAB_ITEM = ITEMS.register("black_terracotta_slab",
            () -> new BlockItem(BLACK_TERRACOTTA_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));
    public static final RegistrySupplier<Item> CUSTOM_PRISMARINE_SLAB_ITEM = ITEMS.register("terrain_prismarine_slab",
            () -> new BlockItem(CUSTOM_PRISMARINE_SLAB.get(), new Item.Properties().arch$tab(TERRAIN_SLABS_TAB)));

    public static void registerModBlocks() {
        LOGGER.info("Registering Mod Blocks & Items for " + TerrainSlabs.MOD_ID);
        TABS.register();
        BLOCKS.register();
        ITEMS.register();
    }
}
