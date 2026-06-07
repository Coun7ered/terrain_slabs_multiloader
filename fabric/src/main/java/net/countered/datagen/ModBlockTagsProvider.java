package net.countered.datagen;

import net.countered.terrainslabs.block.ModBlockTags;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {

    public ModBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {

        this.builder(ModBlockTags.TERRACOTTA_SLABS).add(
                ModBlocksRegistry.TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.RED_TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.ORANGE_TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.LIGHT_GRAY_TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.WHITE_TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.BROWN_TERRACOTTA_SLAB.getKey(),
                ModBlocksRegistry.YELLOW_TERRACOTTA_SLAB.getKey()
        );

        this.builder(ModBlockTags.DIRT_SLABS).add(
                ModBlocksRegistry.DIRT_SLAB.getKey(),
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.MUD_SLAB.getKey(),
                ModBlocksRegistry.COARSE_SLAB.getKey(),
                ModBlocksRegistry.MYCELIUM_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey(),
                ModBlocksRegistry.ROOTED_DIRT_SLAB.getKey(),
                ModBlocksRegistry.MOSS_SLAB.getKey());

        this.builder(ModBlockTags.MOD_SAND_SLABS)
                .add(ModBlocksRegistry.SAND_SLAB.getKey())
                .add(ModBlocksRegistry.RED_SAND_SLAB.getKey());

        this.builder(ModBlockTags.MOD_BASE_STONE_SLABS_OVERWORLD)
                .add(ModBlocksRegistry.CUSTOM_STONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_GRANITE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_DIORITE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_ANDESITE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_TUFF_SLAB.getKey())
                .add(ModBlocksRegistry.DEEPSLATE_SLAB.getKey());

        this.builder(BlockTags.DRIPSTONE_REPLACEABLE)
                .addTag(ModBlockTags.MOD_BASE_STONE_SLABS_OVERWORLD);

        this.builder(BlockTags.MOSS_REPLACEABLE)
                .addTag(ModBlockTags.MOD_BASE_STONE_SLABS_OVERWORLD)
                .addTag(ModBlockTags.DIRT_SLABS);

        this.builder(BlockTags.LUSH_GROUND_REPLACEABLE)
                .addTag(BlockTags.MOSS_REPLACEABLE)
                .add(ModBlocksRegistry.CLAY_SLAB.getKey())
                .add(ModBlocksRegistry.GRAVEL_SLAB.getKey())
                .add(ModBlocksRegistry.SAND_SLAB.getKey());

        this.builder(BlockTags.SUPPORTS_DRY_VEGETATION)
                .addTag(ModBlockTags.MOD_SAND_SLABS)
                .addTag(ModBlockTags.TERRACOTTA_SLABS)
                .addTag(ModBlockTags.DIRT_SLABS)
                .add(ModBlocksRegistry.FARMLAND_SLAB.getKey());

        this.builder(BlockTags.SUPPORT_OVERRIDE_SNOW_LAYER).addTag(BlockTags.SLABS);

        this.builder(BlockTags.CANNOT_SUPPORT_SNOW_LAYER).add(
                ModBlocksRegistry.ICE_SLAB.getKey(),
                ModBlocksRegistry.PACKED_ICE_SLAB.getKey()
        );

        this.builder(BlockTags.CONVERTABLE_TO_MUD)
                .add(ModBlocksRegistry.DIRT_SLAB.getKey())
                .add(ModBlocksRegistry.COARSE_SLAB.getKey())
                .add(ModBlocksRegistry.ROOTED_DIRT_SLAB.getKey());

        this.builder(BlockTags.SLABS)
                .add(ModBlocksRegistry.DIRT_SLAB.getKey())
                .add(ModBlocksRegistry.MUD_SLAB.getKey())
                .add(ModBlocksRegistry.FARMLAND_SLAB.getKey())
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

        this.builder(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocksRegistry.DIRT_SLAB.getKey())
                .add(ModBlocksRegistry.MUD_SLAB.getKey())
                .add(ModBlocksRegistry.FARMLAND_SLAB.getKey())
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

        this.builder(BlockTags.MINEABLE_WITH_PICKAXE)
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

        this.builder(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocksRegistry.MOSS_SLAB.getKey());

        this.builder(BlockTags.SMELTS_TO_GLASS).add(
                ModBlocksRegistry.SAND_SLAB.getKey(),
                ModBlocksRegistry.RED_SAND_SLAB.getKey());

        this.builder(BlockTags.PARROTS_SPAWNABLE_ON)
                .add(ModBlocksRegistry.GRASS_SLAB.getKey());

        this.builder(BlockTags.ANIMALS_SPAWNABLE_ON)
                .add(ModBlocksRegistry.GRASS_SLAB.getKey());

        this.builder(BlockTags.VALID_SPAWN).add(
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey());

        this.builder(BlockTags.AXOLOTLS_SPAWNABLE_ON)
                .add(ModBlocksRegistry.CLAY_SLAB.getKey());

        this.builder(BlockTags.RABBITS_SPAWNABLE_ON).add(
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.SNOW_SLAB.getKey(),
                ModBlocksRegistry.SAND_SLAB.getKey());

        this.builder(BlockTags.GOATS_SPAWNABLE_ON).add(
                ModBlocksRegistry.CUSTOM_STONE_SLAB.getKey(),
                ModBlocksRegistry.SNOW_SLAB.getKey(),
                ModBlocksRegistry.PACKED_ICE_SLAB.getKey(),
                ModBlocksRegistry.GRAVEL_SLAB.getKey());

        this.builder(BlockTags.SNOW)
                .add(ModBlocksRegistry.SNOW_SLAB.getKey());

        this.builder(BlockTags.SCULK_REPLACEABLE)
                .add(ModBlocksRegistry.CUSTOM_STONE_SLAB.getKey())
                .add(ModBlocksRegistry.DIRT_SLAB.getKey())
                .add(ModBlocksRegistry.TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.SAND_SLAB.getKey(), ModBlocksRegistry.RED_SAND_SLAB.getKey())
                .add(ModBlocksRegistry.GRAVEL_SLAB.getKey())
                .add(ModBlocksRegistry.CLAY_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_RED_SANDSTONE_SLAB.getKey())
                .add(ModBlocksRegistry.CUSTOM_SANDSTONE_SLAB.getKey());


        this.builder(BlockTags.AZALEA_ROOT_REPLACEABLE)
                .add(ModBlocksRegistry.CUSTOM_STONE_SLAB.getKey())
                .add(ModBlocksRegistry.DIRT_SLAB.getKey())
                .add(ModBlocksRegistry.TERRACOTTA_SLAB.getKey())
                .add(ModBlocksRegistry.RED_SAND_SLAB.getKey())
                .add(ModBlocksRegistry.CLAY_SLAB.getKey())
                .add(ModBlocksRegistry.GRAVEL_SLAB.getKey())
                .add(ModBlocksRegistry.SAND_SLAB.getKey())
                .add(ModBlocksRegistry.SNOW_SLAB.getKey());

        this.builder(BlockTags.SNIFFER_DIGGABLE_BLOCK).add(
                ModBlocksRegistry.DIRT_SLAB.getKey(),
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey(),
                ModBlocksRegistry.COARSE_SLAB.getKey(),
                ModBlocksRegistry.MOSS_SLAB.getKey(),
                ModBlocksRegistry.MUD_SLAB.getKey());

        this.builder(BlockTags.WOLVES_SPAWNABLE_ON).add(
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.SNOW_SLAB.getKey(),
                ModBlocksRegistry.COARSE_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey());

        this.builder(BlockTags.FOXES_SPAWNABLE_ON).add(
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.SNOW_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey(),
                ModBlocksRegistry.COARSE_SLAB.getKey());

        this.builder(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)
                .add(ModBlocksRegistry.MUD_SLAB.getKey());

        this.builder(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH)
                .add(ModBlocksRegistry.MUD_SLAB.getKey());

        this.builder(BlockTags.FROGS_SPAWNABLE_ON).add(
                ModBlocksRegistry.GRASS_SLAB.getKey(),
                ModBlocksRegistry.MUD_SLAB.getKey());

        this.builder(BlockTags.SOUL_SPEED_BLOCKS).add(
                ModBlocksRegistry.SOUL_SAND_SLAB.getKey(),
                ModBlocksRegistry.SOUL_SOIL_SLAB.getKey());

        this.builder(BlockTags.INFINIBURN_OVERWORLD).add(
                ModBlocksRegistry.NETHERRACK_SLAB.getKey());

        this.builder(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT).add(
                ModBlocksRegistry.MYCELIUM_SLAB.getKey(),
                ModBlocksRegistry.PODZOL_SLAB.getKey(),
                ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.getKey(),
                ModBlocksRegistry.WARPED_NYLIUM_SLAB.getKey());

        this.builder(BlockTags.DRAGON_IMMUNE).add(
                ModBlocksRegistry.ENDSTONE_SLAB.getKey());

        this.builder(BlockTags.NYLIUM).add(
                ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.getKey(),
                ModBlocksRegistry.WARPED_NYLIUM_SLAB.getKey());
    }
}
