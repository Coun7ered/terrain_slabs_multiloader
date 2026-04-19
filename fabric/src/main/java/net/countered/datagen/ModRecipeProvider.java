package net.countered.datagen;



import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.DIRT_SLAB.get(), Ingredient.of(Blocks.DIRT))
                .unlockedBy("has_dirt_block", has(Blocks.DIRT))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.MUD_SLAB.get(), Ingredient.of(Blocks.MUD))
                .unlockedBy("has_mud_block", has(Blocks.MUD))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.COARSE_SLAB.get(), Ingredient.of(Blocks.COARSE_DIRT))
                .unlockedBy("has_coarse_dirt_block", has(Blocks.COARSE_DIRT))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.SNOW_SLAB.get(), Ingredient.of(Blocks.SNOW_BLOCK))
                .unlockedBy("has_snow_block", has(Blocks.SNOW_BLOCK))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.PACKED_ICE_SLAB.get(), Ingredient.of(Blocks.PACKED_ICE))
                .unlockedBy("has_packed_ice_block", has(Blocks.PACKED_ICE))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.DEEPSLATE_SLAB.get(), Ingredient.of(Blocks.DEEPSLATE))
                .unlockedBy("has_deepslate_block", has(Blocks.DEEPSLATE))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.CLAY_SLAB.get(), Ingredient.of(Blocks.CLAY))
                .unlockedBy("has_clay_block", has(Blocks.CLAY))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.MOSS_SLAB.get(), Ingredient.of(Blocks.MOSS_BLOCK))
                .unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.CUSTOM_TUFF_SLAB.get(), Ingredient.of(Blocks.TUFF))
                .unlockedBy("has_tuff_block", has(Blocks.TUFF))
                .save(exporter);

        // terralith
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.CALCITE_SLAB.get(), Ingredient.of(Blocks.CALCITE))
                .unlockedBy("has_calcite", has(Blocks.CALCITE))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.SMOOTH_BASALT_SLAB.get(), Ingredient.of(Blocks.SMOOTH_BASALT))
                .unlockedBy("has_smooth_basalt", has(Blocks.SMOOTH_BASALT))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.LIGHT_BLUE_TERRACOTTA_SLAB.get(), Ingredient.of(Blocks.LIGHT_BLUE_TERRACOTTA))
                .unlockedBy("has_light_blue_terracotta", has(Blocks.LIGHT_BLUE_TERRACOTTA))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.CYAN_TERRACOTTA_SLAB.get(), Ingredient.of(Blocks.CYAN_TERRACOTTA))
                .unlockedBy("has_cyan_terracotta", has(Blocks.CYAN_TERRACOTTA))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.ICE_SLAB.get(), Ingredient.of(Blocks.ICE))
                .unlockedBy("has_ice", has(Blocks.ICE))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.ROOTED_DIRT_SLAB.get(), Ingredient.of(Blocks.ROOTED_DIRT))
                .unlockedBy("has_rooted_dirt", has(Blocks.ROOTED_DIRT))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.PACKED_MUD_SLAB.get(), Ingredient.of(Blocks.PACKED_MUD))
                .unlockedBy("has_packed_mud", has(Blocks.PACKED_MUD))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.BLUE_ICE_SLAB.get(), Ingredient.of(Blocks.BLUE_ICE))
                .unlockedBy("has_blue_ice", has(Blocks.BLUE_ICE))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.BLACK_TERRACOTTA_SLAB.get(), Ingredient.of(Blocks.BLACK_TERRACOTTA))
                .unlockedBy("has_black_terracotta", has(Blocks.BLACK_TERRACOTTA))
                .save(exporter);

        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.GRASS_SLAB.get(), Ingredient.of(Blocks.GRASS_BLOCK))
                .unlockedBy("has_grass_block", has(Blocks.GRASS_BLOCK))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.MYCELIUM_SLAB.get(), Ingredient.of(Blocks.MYCELIUM))
                .unlockedBy("has_mycelium_block", has(Blocks.MYCELIUM))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.PODZOL_SLAB.get(), Ingredient.of(Blocks.PODZOL))
                .unlockedBy("has_podzol_block", has(Blocks.PODZOL))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.PATH_SLAB.get(), Ingredient.of(Blocks.DIRT_PATH))
                .unlockedBy("has_dirt_path_block", has(Blocks.DIRT_PATH))
                .save(exporter);

        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.GRAVEL_SLAB.get(), Ingredient.of(Blocks.GRAVEL))
                .unlockedBy("has_gravel_block", has(Blocks.GRAVEL))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.SAND_SLAB.get(), Ingredient.of(Blocks.SAND))
                .unlockedBy("has_sand_block", has(Blocks.SAND))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.RED_SAND_SLAB.get(), Ingredient.of(Blocks.RED_SAND))
                .unlockedBy("has_red_sand_block", has(Blocks.RED_SAND))
                .save(exporter);

        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.TERRACOTTA_SLAB.get(), Ingredient.of(Blocks.TERRACOTTA))
                .unlockedBy("has_terracotta_block", has(Blocks.TERRACOTTA))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.RED_TERRACOTTA_SLAB.get(), Ingredient.of(Blocks.RED_TERRACOTTA))
                .unlockedBy("has_red_terracotta_block", has(Blocks.RED_TERRACOTTA))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.ORANGE_TERRACOTTA_SLAB.get(), Ingredient.of(Blocks.ORANGE_TERRACOTTA))
                .unlockedBy("has_orange_terracotta_block", has(Blocks.ORANGE_TERRACOTTA))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.LIGHT_GRAY_TERRACOTTA_SLAB.get(), Ingredient.of(Blocks.LIGHT_GRAY_TERRACOTTA))
                .unlockedBy("has_light_gray_terracotta_block", has(Blocks.LIGHT_GRAY_TERRACOTTA))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.WHITE_TERRACOTTA_SLAB.get(), Ingredient.of(Blocks.WHITE_TERRACOTTA))
                .unlockedBy("has_white_terracotta_block", has(Blocks.WHITE_TERRACOTTA))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.BROWN_TERRACOTTA_SLAB.get(), Ingredient.of(Blocks.BROWN_TERRACOTTA))
                .unlockedBy("has_brown_terracotta_block", has(Blocks.BROWN_TERRACOTTA))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.YELLOW_TERRACOTTA_SLAB.get(), Ingredient.of(Blocks.YELLOW_TERRACOTTA))
                .unlockedBy("has_yellow_terracotta_block", has(Blocks.YELLOW_TERRACOTTA))
                .save(exporter);

        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.SOUL_SAND_SLAB.get(), Ingredient.of(Blocks.SOUL_SAND))
                .unlockedBy("has_soul_sand_block", has(Blocks.SOUL_SAND))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.SOUL_SOIL_SLAB.get(), Ingredient.of(Blocks.SOUL_SOIL))
                .unlockedBy("has_soul_soil_block", has(Blocks.SOUL_SOIL))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.NETHERRACK_SLAB.get(), Ingredient.of(Blocks.NETHERRACK))
                .unlockedBy("has_netherrack_block", has(Blocks.NETHERRACK))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.WARPED_NYLIUM_SLAB.get(), Ingredient.of(Blocks.WARPED_NYLIUM))
                .unlockedBy("has_warped_nylium_block", has(Blocks.WARPED_NYLIUM))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get(), Ingredient.of(Blocks.CRIMSON_NYLIUM))
                .unlockedBy("has_crimson_nylium_block", has(Blocks.CRIMSON_NYLIUM))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.BASALT_SLAB.get(), Ingredient.of(Blocks.BASALT))
                .unlockedBy("has_basalt_block", has(Blocks.BASALT))
                .save(exporter);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocksRegistry.ENDSTONE_SLAB.get(), Ingredient.of(Blocks.END_STONE))
                .unlockedBy("has_end_stone_block", has(Blocks.END_STONE))
                .save(exporter);
    }
}
