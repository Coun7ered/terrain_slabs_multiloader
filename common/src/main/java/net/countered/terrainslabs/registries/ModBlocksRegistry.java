package net.countered.terrainslabs.registries;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.block.customslabs.CustomSlab;
import net.countered.terrainslabs.block.customslabs.GravityAffectedSlab;
import net.countered.terrainslabs.block.customslabs.netherslabs.NetherrackSlab;
import net.countered.terrainslabs.block.customslabs.netherslabs.NyliumSlab;
import net.countered.terrainslabs.block.customslabs.nonfullslabs.FarmlandSlab;
import net.countered.terrainslabs.block.customslabs.nonfullslabs.MudSlab;
import net.countered.terrainslabs.block.customslabs.nonfullslabs.PathSlab;
import net.countered.terrainslabs.block.customslabs.nonfullslabs.SoulSandSlab;
import net.countered.terrainslabs.block.customslabs.soilslabs.GrassSlab;
import net.countered.terrainslabs.block.customslabs.soilslabs.MyceliumSlab;
import net.countered.terrainslabs.block.customslabs.soilslabs.SoilSlab;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class ModBlocksRegistry {
    private static final Logger LOGGER = LogManager.getLogger(TerrainSlabs.MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(TerrainSlabs.MOD_ID, Registries.BLOCK);

    private static <T extends Block> RegistrySupplier<T> registerBlock(
            String name,
            BlockBehaviour.Properties props,
            Function<BlockBehaviour.Properties, T> factory) {

        ResourceKey<Block> key = ResourceKey.create(
                Registries.BLOCK,
                Identifier.parse(TerrainSlabs.MOD_ID + ":" + name)
        );
        return BLOCKS.register(name, () -> factory.apply(props.setId(key)));
    }

    // BLOCKS
    public static final RegistrySupplier<Block> DIRT_SLAB = registerBlock(
            "dirt_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT),
            CustomSlab::new
    );;
    public static final RegistrySupplier<Block> MUD_SLAB = registerBlock(
            "mud_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.MUD).noOcclusion(),
            MudSlab::new
    );
    public static final RegistrySupplier<Block> FARMLAND_SLAB = registerBlock(
            "farmland_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND),
            FarmlandSlab::new
    );
    public static final RegistrySupplier<Block> COARSE_SLAB = registerBlock(
            "coarse_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.COARSE_DIRT),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> SNOW_SLAB = registerBlock(
            "snow_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> PACKED_ICE_SLAB = registerBlock(
            "packed_ice_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.PACKED_ICE),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> DEEPSLATE_SLAB = registerBlock(
            "deepslate_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CLAY_SLAB = registerBlock(
            "clay_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> MOSS_SLAB = registerBlock(
            "moss_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_TUFF_SLAB = registerBlock(
            "terrain_tuff_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF),
            CustomSlab::new
    );

    public static final RegistrySupplier<Block> GRASS_SLAB = registerBlock(
            "grass_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK),
            GrassSlab::new
    );
    public static final RegistrySupplier<Block> MYCELIUM_SLAB = registerBlock(
            "mycelium_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.MYCELIUM),
            MyceliumSlab::new
    );
    public static final RegistrySupplier<Block> PODZOL_SLAB = registerBlock(
            "podzol_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.PODZOL),
            SoilSlab::new
    );
    public static final RegistrySupplier<Block> PATH_SLAB = registerBlock(
            "path_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT_PATH).noOcclusion(),
            PathSlab::new
    );

    public static final RegistrySupplier<Block> GRAVEL_SLAB = registerBlock(
            "gravel_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL),
            GravityAffectedSlab::new
    );
    public static final RegistrySupplier<Block> SAND_SLAB = registerBlock(
            "sand_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.SAND),
            GravityAffectedSlab::new
    );
    public static final RegistrySupplier<Block> RED_SAND_SLAB = registerBlock(
            "red_sand_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SAND),
            GravityAffectedSlab::new
    );

    public static final RegistrySupplier<Block> TERRACOTTA_SLAB = registerBlock(
            "terracotta_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> RED_TERRACOTTA_SLAB = registerBlock(
            "red_terracotta_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.RED_TERRACOTTA),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> ORANGE_TERRACOTTA_SLAB = registerBlock(
            "orange_terracotta_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_TERRACOTTA),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> LIGHT_GRAY_TERRACOTTA_SLAB = registerBlock(
            "light_gray_terracotta_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_TERRACOTTA),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> WHITE_TERRACOTTA_SLAB = registerBlock(
            "white_terracotta_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> BROWN_TERRACOTTA_SLAB = registerBlock(
            "brown_terracotta_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_TERRACOTTA),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> YELLOW_TERRACOTTA_SLAB = registerBlock(
            "yellow_terracotta_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_TERRACOTTA),
            CustomSlab::new
    );

    public static final RegistrySupplier<Block> CUSTOM_STONE_SLAB = registerBlock(
            "terrain_stone_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_SLAB),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_SANDSTONE_SLAB = registerBlock(
            "terrain_sandstone_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_RED_SANDSTONE_SLAB = registerBlock(
            "terrain_red_sandstone_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SANDSTONE_SLAB),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_ANDESITE_SLAB = registerBlock(
            "terrain_andesite_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE_SLAB),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_DIORITE_SLAB = registerBlock(
            "terrain_diorite_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE_SLAB),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_GRANITE_SLAB = registerBlock(
            "terrain_granite_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE_SLAB),
            CustomSlab::new
    );

    public static final RegistrySupplier<Block> SOUL_SAND_SLAB = registerBlock(
            "soul_sand_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SAND).noOcclusion(),
            SoulSandSlab::new
    );
    public static final RegistrySupplier<Block> SOUL_SOIL_SLAB = registerBlock(
            "soul_soil_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SOIL),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> NETHERRACK_SLAB = registerBlock(
            "netherrack_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK),
            NetherrackSlab::new
    );
    public static final RegistrySupplier<Block> WARPED_NYLIUM_SLAB = registerBlock(
            "warped_nylium_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_NYLIUM),
            NyliumSlab::new
    );
    public static final RegistrySupplier<Block> CRIMSON_NYLIUM_SLAB = registerBlock(
            "crimson_nylium_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_NYLIUM),
            NyliumSlab::new
    );
    public static final RegistrySupplier<Block> BASALT_SLAB = registerBlock(
            "basalt_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_BLACKSTONE_SLAB = registerBlock(
            "terrain_blackstone_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE_SLAB),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> ENDSTONE_SLAB = registerBlock(
            "endstone_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE),
            CustomSlab::new
    );

    // VANILLA BLOCKS USED BY TERRALITH
    public static final RegistrySupplier<Block> CALCITE_SLAB = registerBlock(
            "calcite_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> SMOOTH_BASALT_SLAB = registerBlock(
            "smooth_basalt_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_BASALT),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> LIGHT_BLUE_TERRACOTTA_SLAB = registerBlock(
            "light_blue_terracotta_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_TERRACOTTA),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CYAN_TERRACOTTA_SLAB = registerBlock(
            "cyan_terracotta_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_TERRACOTTA),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_COBBLESTONE_SLAB = registerBlock(
            "terrain_cobblestone_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_SLAB),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_MOSSY_COBBLESTONE_SLAB = registerBlock(
            "terrain_mossy_cobblestone_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.MOSSY_COBBLESTONE_SLAB),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_COBBLED_DEEPSLATE_SLAB = registerBlock(
            "terrain_cobbled_deepslate_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> ICE_SLAB = registerBlock(
            "ice_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.ICE),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> ROOTED_DIRT_SLAB = registerBlock(
            "rooted_dirt_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.ROOTED_DIRT),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> PACKED_MUD_SLAB = registerBlock(
            "packed_mud_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.PACKED_MUD),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> BLUE_ICE_SLAB = registerBlock(
            "blue_ice_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_ICE),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> BLACK_TERRACOTTA_SLAB = registerBlock(
            "black_terracotta_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_TERRACOTTA),
            CustomSlab::new
    );
    public static final RegistrySupplier<Block> CUSTOM_PRISMARINE_SLAB = registerBlock(
            "terrain_prismarine_slab",
            BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE_SLAB),
            CustomSlab::new
    );

    public static void registerModBlocks() {
        LOGGER.info("Registering Mod Blocks & Items for " + TerrainSlabs.MOD_ID);
        BLOCKS.register();
    }
}
