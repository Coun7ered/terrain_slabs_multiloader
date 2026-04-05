package net.countered.datagen;


import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;


public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        Map<Block, Block> slabs = new HashMap<>();
        fillSlabMap(slabs);

        slabs.forEach((base, slab) -> {
            TextureMapping textureMapping = TextureMapping.cube(base);

            ResourceLocation bottomModel = ModelTemplates.SLAB_BOTTOM.create(slab, textureMapping, generator.modelOutput);
            ResourceLocation topModel = ModelTemplates.SLAB_TOP.create(slab, textureMapping, generator.modelOutput);

            ResourceLocation fullBlockModel = ModelLocationUtils.getModelLocation(base);

            generator.blockStateOutput.accept(
                    BlockModelGenerators.createSlab(slab, bottomModel, topModel, fullBlockModel)
            );

            generator.delegateItemModel(slab, bottomModel);
        });
    }

    private void fillSlabMap(Map<Block, Block> map) {
        map.put(Blocks.STONE, ModBlocksRegistry.CUSTOM_STONE_SLAB.get());
        map.put(Blocks.TUFF, ModBlocksRegistry.CUSTOM_TUFF_SLAB.get());
        map.put(Blocks.ANDESITE, ModBlocksRegistry.CUSTOM_ANDESITE_SLAB.get());
        map.put(Blocks.DIORITE, ModBlocksRegistry.CUSTOM_DIORITE_SLAB.get());
        map.put(Blocks.GRANITE, ModBlocksRegistry.CUSTOM_GRANITE_SLAB.get());
        map.put(Blocks.GRAVEL, ModBlocksRegistry.GRAVEL_SLAB.get());
        map.put(Blocks.SAND, ModBlocksRegistry.SAND_SLAB.get());
        map.put(Blocks.RED_SAND, ModBlocksRegistry.RED_SAND_SLAB.get());
        map.put(Blocks.DIRT, ModBlocksRegistry.DIRT_SLAB.get());
        map.put(Blocks.MUD, ModBlocksRegistry.MUD_SLAB.get());
        map.put(Blocks.COARSE_DIRT, ModBlocksRegistry.COARSE_SLAB.get());
        map.put(Blocks.PACKED_ICE, ModBlocksRegistry.PACKED_ICE_SLAB.get());
        map.put(Blocks.CLAY, ModBlocksRegistry.CLAY_SLAB.get());
        map.put(Blocks.MOSS_BLOCK, ModBlocksRegistry.MOSS_SLAB.get());
        map.put(Blocks.CALCITE, ModBlocksRegistry.CALCITE_SLAB.get());
        map.put(Blocks.SMOOTH_BASALT, ModBlocksRegistry.SMOOTH_BASALT_SLAB.get());
        map.put(Blocks.LIGHT_BLUE_TERRACOTTA, ModBlocksRegistry.LIGHT_BLUE_TERRACOTTA_SLAB.get());
        map.put(Blocks.CYAN_TERRACOTTA, ModBlocksRegistry.CYAN_TERRACOTTA_SLAB.get());
        map.put(Blocks.COBBLESTONE, ModBlocksRegistry.CUSTOM_COBBLESTONE_SLAB.get());
        map.put(Blocks.MOSSY_COBBLESTONE, ModBlocksRegistry.CUSTOM_MOSSY_COBBLESTONE_SLAB.get());
        map.put(Blocks.COBBLED_DEEPSLATE, ModBlocksRegistry.CUSTOM_COBBLED_DEEPSLATE_SLAB.get());
        map.put(Blocks.ICE, ModBlocksRegistry.ICE_SLAB.get());
        map.put(Blocks.ROOTED_DIRT, ModBlocksRegistry.ROOTED_DIRT_SLAB.get());
        map.put(Blocks.PACKED_MUD, ModBlocksRegistry.PACKED_MUD_SLAB.get());
        map.put(Blocks.BLUE_ICE, ModBlocksRegistry.BLUE_ICE_SLAB.get());
        map.put(Blocks.BLACK_TERRACOTTA, ModBlocksRegistry.BLACK_TERRACOTTA_SLAB.get());
        map.put(Blocks.PRISMARINE, ModBlocksRegistry.CUSTOM_PRISMARINE_SLAB.get());
        map.put(Blocks.TERRACOTTA, ModBlocksRegistry.TERRACOTTA_SLAB.get());
        map.put(Blocks.RED_TERRACOTTA, ModBlocksRegistry.RED_TERRACOTTA_SLAB.get());
        map.put(Blocks.ORANGE_TERRACOTTA, ModBlocksRegistry.ORANGE_TERRACOTTA_SLAB.get());
        map.put(Blocks.LIGHT_GRAY_TERRACOTTA, ModBlocksRegistry.LIGHT_GRAY_TERRACOTTA_SLAB.get());
        map.put(Blocks.WHITE_TERRACOTTA, ModBlocksRegistry.WHITE_TERRACOTTA_SLAB.get());
        map.put(Blocks.BROWN_TERRACOTTA, ModBlocksRegistry.BROWN_TERRACOTTA_SLAB.get());
        map.put(Blocks.YELLOW_TERRACOTTA, ModBlocksRegistry.YELLOW_TERRACOTTA_SLAB.get());
        map.put(Blocks.SOUL_SAND, ModBlocksRegistry.SOUL_SAND_SLAB.get());
        map.put(Blocks.SOUL_SOIL, ModBlocksRegistry.SOUL_SOIL_SLAB.get());
        map.put(Blocks.NETHERRACK, ModBlocksRegistry.NETHERRACK_SLAB.get());
        map.put(Blocks.BLACKSTONE, ModBlocksRegistry.CUSTOM_BLACKSTONE_SLAB.get());
        map.put(Blocks.END_STONE, ModBlocksRegistry.ENDSTONE_SLAB.get());
    }


    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {

    }
}
