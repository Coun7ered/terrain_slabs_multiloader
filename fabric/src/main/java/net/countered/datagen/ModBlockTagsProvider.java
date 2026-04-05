package net.countered.datagen;

import net.countered.terrainslabs.block.ModBlockTags;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        this.getOrCreateTagBuilder(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON).addTag(BlockTags.SLABS);

        this.getOrCreateTagBuilder(BlockTags.DEAD_BUSH_MAY_PLACE_ON)
                .addTag(ModBlockTags.DIRT_SLABS)
                .add(ModBlocksRegistry.SAND_SLAB.getKey(), ModBlocksRegistry.RED_SAND_SLAB.getKey())
                .addTag(ModBlockTags.TERRACOTTA_SLABS);

        this.getOrCreateTagBuilder(ModBlockTags.TERRACOTTA_SLABS).add(
                ModBlocksRegistry.TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.RED_TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.ORANGE_TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.LIGHT_GRAY_TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.WHITE_TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.BROWN_TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.YELLOW_TERRACOTTA_SLAB.getKey()
        );

        this.getOrCreateTagBuilder(ModBlockTags.DIRT_SLABS).add(
                ModBlocksRegistry.DIRT_SLAB.getKey(),
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.MUD_SLAB.getKey(),
                ModBlocksRegistry.COARSE_SLAB.getKey(),
                ModBlocksRegistry.MYCELIUM_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey(),
                ModBlocksRegistry.ROOTED_DIRT_SLAB.getKey(),
                ModBlocksRegistry.MOSS_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.SLABS)
                .add(ModBlocksRegistry.DIRT_SLAB.getKey())
                .add(ModBlocksRegistry.MUD_SLAB.getKey())
                .add(ModBlocksRegistry.COARSE_SLAB.getKey())
                .add(ModBlocksRegistry.SNOW_SLAB.getKey())
                .add(ModBlocksRegistry.PACKED_ICE_SLAB.getKey())
                .add(ModBlocksRegistry.DEEPSLATE_SLAB.getKey())
                .add(ModBlocksRegistry.CLAY_SLAB.getKey())
                .add(ModBlocksRegistry.MOSS_SLAB.getKey())

                //terralith compat
                .add(ModBlocksRegistry.CALCITE_SLAB.getKey())
                .add(ModBlocksRegistry.SMOOTH_BASALT_SLAB.getKey())
                .add(ModBlocksRegistry.LIGHT_BLUE_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.CYAN_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_COBBLESTONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_MOSSY_COBBLESTONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_COBBLED_DEEPSLATE_SLAB.getKey())
                .add(ModBlocksRegistry.ICE_SLAB.getKey())
                .add(ModBlocksRegistry.ROOTED_DIRT_SLAB.getKey())
                .add(ModBlocksRegistry.PACKED_MUD_SLAB.getKey())
                .add(ModBlocksRegistry.BLUE_ICE_SLAB.getKey())
                .add(ModBlocksRegistry.BLACK_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_PRISMARINE_SLAB.getKey())

                .add(ModBlocksRegistry.GRASS_SLAB.getKey())
                .add(ModBlocksRegistry.MYCELIUM_SLAB.getKey())
                .add(ModBlocksRegistry.PODZOL_SLAB.getKey())
                .add(ModBlocksRegistry.PATH_SLAB.getKey())

                .add(ModBlocksRegistry.GRAVEL_SLAB.getKey())
                .add(ModBlocksRegistry.SAND_SLAB.getKey())
                .add(ModBlocksRegistry.RED_SAND_SLAB.getKey())

                .add(ModBlocksRegistry.TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.RED_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.ORANGE_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.LIGHT_GRAY_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.WHITE_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.BROWN_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.YELLOW_TERRACOTTA_SLAB.getKey())

                .add(ModBlocksRegistry.CUSTOM_TUFF_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_STONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_SANDSTONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_RED_SANDSTONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_DIORITE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_ANDESITE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_GRANITE_SLAB.getKey())

                .add(ModBlocksRegistry.SOUL_SAND_SLAB.getKey())
                .add(ModBlocksRegistry.SOUL_SOIL_SLAB.getKey())
                .add(ModBlocksRegistry.NETHERRACK_SLAB.getKey())
                .add(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.getKey())
                .add(ModBlocksRegistry.WARPED_NYLIUM_SLAB.getKey())
                .add(ModBlocksRegistry.BASALT_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_BLACKSTONE_SLAB.getKey())
                .add(ModBlocksRegistry.ENDSTONE_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocksRegistry.DIRT_SLAB.getKey())
                .add(ModBlocksRegistry.MUD_SLAB.getKey())
                .add(ModBlocksRegistry.COARSE_SLAB.getKey())
                .add(ModBlocksRegistry.SNOW_SLAB.getKey())
                .add(ModBlocksRegistry.CLAY_SLAB.getKey())
                .add(ModBlocksRegistry.GRASS_SLAB.getKey())
                .add(ModBlocksRegistry.MYCELIUM_SLAB.getKey())
                .add(ModBlocksRegistry.PODZOL_SLAB.getKey())
                .add(ModBlocksRegistry.PATH_SLAB.getKey())
                .add(ModBlocksRegistry.GRAVEL_SLAB.getKey())
                .add(ModBlocksRegistry.SAND_SLAB.getKey())
                .add(ModBlocksRegistry.RED_SAND_SLAB.getKey())

                .add(ModBlocksRegistry.SOUL_SAND_SLAB.getKey())
                .add(ModBlocksRegistry.SOUL_SOIL_SLAB.getKey())

                //terralith
                .add(ModBlocksRegistry.ROOTED_DIRT_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocksRegistry.PACKED_ICE_SLAB.getKey())
                .add(ModBlocksRegistry.DEEPSLATE_SLAB.getKey())
                .add(ModBlocksRegistry.TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.RED_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.ORANGE_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.LIGHT_GRAY_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.WHITE_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.BROWN_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.YELLOW_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_TUFF_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_GRANITE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_ANDESITE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_DIORITE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_STONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_RED_SANDSTONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_SANDSTONE_SLAB.getKey())
                //terralith
                .add(ModBlocksRegistry.CALCITE_SLAB.getKey())
                .add(ModBlocksRegistry.SMOOTH_BASALT_SLAB.getKey())
                .add(ModBlocksRegistry.LIGHT_BLUE_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.CYAN_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_COBBLESTONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_MOSSY_COBBLESTONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_COBBLED_DEEPSLATE_SLAB.getKey())
                .add(ModBlocksRegistry.ICE_SLAB.getKey())
                .add(ModBlocksRegistry.PACKED_MUD_SLAB.getKey())
                .add(ModBlocksRegistry.BLUE_ICE_SLAB.getKey())
                .add(ModBlocksRegistry.BLACK_TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_PRISMARINE_SLAB.getKey())

                .add(ModBlocksRegistry.NETHERRACK_SLAB.getKey())
                .add(ModBlocksRegistry.WARPED_NYLIUM_SLAB.getKey())
                .add(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_BLACKSTONE_SLAB.getKey())
                .add(ModBlocksRegistry.BASALT_SLAB.getKey())
                .add(ModBlocksRegistry.ENDSTONE_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocksRegistry.MOSS_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.SMELTS_TO_GLASS).add(
                ModBlocksRegistry.SAND_SLAB.getKey(),
                ModBlocksRegistry.RED_SAND_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.PARROTS_SPAWNABLE_ON)
                .add(ModBlocksRegistry.GRASS_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.ANIMALS_SPAWNABLE_ON)
                .add(ModBlocksRegistry.GRASS_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.VALID_SPAWN).add(
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.AXOLOTLS_SPAWNABLE_ON)
                .add(ModBlocksRegistry.CLAY_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.RABBITS_SPAWNABLE_ON).add(
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.SNOW_SLAB.getKey(),
                ModBlocksRegistry.SAND_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.GOATS_SPAWNABLE_ON).add(
                ModBlocksRegistry.CUSTOM_STONE_SLAB.getKey(),
                ModBlocksRegistry.SNOW_SLAB.getKey(),
                ModBlocksRegistry.PACKED_ICE_SLAB.getKey(),
                ModBlocksRegistry.GRAVEL_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.SNOW)
                .add(ModBlocksRegistry.SNOW_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.SCULK_REPLACEABLE)
                .add(ModBlocksRegistry.CUSTOM_STONE_SLAB.getKey())
                .add(ModBlocksRegistry.DIRT_SLAB.getKey())
                .add(ModBlocksRegistry.TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.SAND_SLAB.getKey(), ModBlocksRegistry.RED_SAND_SLAB.getKey())
                .add(ModBlocksRegistry.GRAVEL_SLAB.getKey())
                .add(ModBlocksRegistry.CLAY_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_RED_SANDSTONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_SANDSTONE_SLAB.getKey());


        this.getOrCreateTagBuilder(BlockTags.AZALEA_ROOT_REPLACEABLE)
                .add(ModBlocksRegistry.CUSTOM_STONE_SLAB.getKey())
                .add(ModBlocksRegistry.DIRT_SLAB.getKey())
                .add(ModBlocksRegistry.TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.RED_SAND_SLAB.getKey())
                .add(ModBlocksRegistry.CLAY_SLAB.getKey())
                .add(ModBlocksRegistry.GRAVEL_SLAB.getKey())
                .add(ModBlocksRegistry.SAND_SLAB.getKey())
                .add(ModBlocksRegistry.SNOW_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.SNIFFER_DIGGABLE_BLOCK).add(
                ModBlocksRegistry.DIRT_SLAB.getKey(),
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey(),
                ModBlocksRegistry.COARSE_SLAB.getKey(),
                ModBlocksRegistry.MOSS_SLAB.getKey(),
                ModBlocksRegistry.MUD_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.WOLVES_SPAWNABLE_ON).add(
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.SNOW_SLAB.getKey(),
                ModBlocksRegistry.COARSE_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.FOXES_SPAWNABLE_ON).add(
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.SNOW_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey(),
                ModBlocksRegistry.COARSE_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)
                .add(ModBlocksRegistry.MUD_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH)
                .add(ModBlocksRegistry.MUD_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.FROGS_SPAWNABLE_ON).add(
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.MUD_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.SOUL_SPEED_BLOCKS).add(
                ModBlocksRegistry.SOUL_SAND_SLAB.getKey(),
                ModBlocksRegistry.SOUL_SOIL_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.INFINIBURN_OVERWORLD).add(
                ModBlocksRegistry.NETHERRACK_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.MUSHROOM_GROW_BLOCK).add(
                ModBlocksRegistry.MYCELIUM_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey(),
                ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.getKey(),
                ModBlocksRegistry.WARPED_NYLIUM_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.DRAGON_IMMUNE).add(
                ModBlocksRegistry.ENDSTONE_SLAB.getKey());

        this.getOrCreateTagBuilder(BlockTags.NYLIUM).add(
                ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.getKey(),
                ModBlocksRegistry.WARPED_NYLIUM_SLAB.getKey());
    }
}
