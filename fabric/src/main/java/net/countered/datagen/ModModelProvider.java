package net.countered.datagen;


import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;


public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        cubedSlabs.forEach((base, slab) -> {
            createSimpleSlab(generators, base, slab);
        });
    }

    private void createSimpleSlab(BlockModelGenerators generator, Block base, Block slab) {
        TextureMapping textureMapping = TextureMapping.cube(base);

        Identifier bottomId = ModelTemplates.SLAB_BOTTOM.create(slab, textureMapping, generator.modelOutput);
        Identifier topId = ModelTemplates.SLAB_TOP.create(slab, textureMapping, generator.modelOutput);
        Identifier fullBlockId = ModelLocationUtils.getModelLocation(base);

        registerSlabBlockState(generator, slab, bottomId, topId, fullBlockId);
        generator.registerSimpleItemModel(slab, bottomId);
    }

    private void registerSlabBlockState(BlockModelGenerators generator, Block slab, Identifier bottom, Identifier top, Identifier doubleBlock) {
        generator.blockStateOutput.accept(
                BlockModelGenerators.createSlab(
                        slab,
                        BlockModelGenerators.plainVariant(bottom),
                        BlockModelGenerators.plainVariant(top),
                        BlockModelGenerators.plainVariant(doubleBlock)
                )
        );
    }

    private static final Map<Block, Block> cubedSlabs = Map.ofEntries(
            Map.entry(Blocks.STONE, ModBlocksRegistry.CUSTOM_STONE_SLAB.get()),
            Map.entry(Blocks.TUFF, ModBlocksRegistry.CUSTOM_TUFF_SLAB.get()),
            Map.entry(Blocks.ANDESITE, ModBlocksRegistry.CUSTOM_ANDESITE_SLAB.get()),
            Map.entry(Blocks.DIORITE, ModBlocksRegistry.CUSTOM_DIORITE_SLAB.get()),
            Map.entry(Blocks.GRANITE, ModBlocksRegistry.CUSTOM_GRANITE_SLAB.get()),
            Map.entry(Blocks.GRAVEL, ModBlocksRegistry.GRAVEL_SLAB.get()),
            Map.entry(Blocks.SAND, ModBlocksRegistry.SAND_SLAB.get()),
            Map.entry(Blocks.RED_SAND, ModBlocksRegistry.RED_SAND_SLAB.get()),
            Map.entry(Blocks.DIRT, ModBlocksRegistry.DIRT_SLAB.get()),
            Map.entry(Blocks.MUD, ModBlocksRegistry.MUD_SLAB.get()),
            Map.entry(Blocks.COARSE_DIRT, ModBlocksRegistry.COARSE_SLAB.get()),
            Map.entry(Blocks.PACKED_ICE, ModBlocksRegistry.PACKED_ICE_SLAB.get()),
            Map.entry(Blocks.CLAY, ModBlocksRegistry.CLAY_SLAB.get()),
            Map.entry(Blocks.MOSS_BLOCK, ModBlocksRegistry.MOSS_SLAB.get()),
            Map.entry(Blocks.CALCITE, ModBlocksRegistry.CALCITE_SLAB.get()),
            Map.entry(Blocks.SMOOTH_BASALT, ModBlocksRegistry.SMOOTH_BASALT_SLAB.get()),
            Map.entry(Blocks.DYED_TERRACOTTA.lightBlue(), ModBlocksRegistry.LIGHT_BLUE_TERRACOTTA_SLAB.get()),
            Map.entry(Blocks.DYED_TERRACOTTA.cyan(), ModBlocksRegistry.CYAN_TERRACOTTA_SLAB.get()),
            Map.entry(Blocks.COBBLESTONE, ModBlocksRegistry.CUSTOM_COBBLESTONE_SLAB.get()),
            Map.entry(Blocks.MOSSY_COBBLESTONE, ModBlocksRegistry.CUSTOM_MOSSY_COBBLESTONE_SLAB.get()),
            Map.entry(Blocks.COBBLED_DEEPSLATE, ModBlocksRegistry.CUSTOM_COBBLED_DEEPSLATE_SLAB.get()),
            Map.entry(Blocks.ICE, ModBlocksRegistry.ICE_SLAB.get()),
            Map.entry(Blocks.ROOTED_DIRT, ModBlocksRegistry.ROOTED_DIRT_SLAB.get()),
            Map.entry(Blocks.PACKED_MUD, ModBlocksRegistry.PACKED_MUD_SLAB.get()),
            Map.entry(Blocks.BLUE_ICE, ModBlocksRegistry.BLUE_ICE_SLAB.get()),
            Map.entry(Blocks.DYED_TERRACOTTA.black(), ModBlocksRegistry.BLACK_TERRACOTTA_SLAB.get()),
            Map.entry(Blocks.PRISMARINE, ModBlocksRegistry.CUSTOM_PRISMARINE_SLAB.get()),
            Map.entry(Blocks.TERRACOTTA, ModBlocksRegistry.TERRACOTTA_SLAB.get()),
            Map.entry(Blocks.DYED_TERRACOTTA.red(), ModBlocksRegistry.RED_TERRACOTTA_SLAB.get()),
            Map.entry(Blocks.DYED_TERRACOTTA.orange(), ModBlocksRegistry.ORANGE_TERRACOTTA_SLAB.get()),
            Map.entry(Blocks.DYED_TERRACOTTA.lightGray(), ModBlocksRegistry.LIGHT_GRAY_TERRACOTTA_SLAB.get()),
            Map.entry(Blocks.DYED_TERRACOTTA.white(), ModBlocksRegistry.WHITE_TERRACOTTA_SLAB.get()),
            Map.entry(Blocks.DYED_TERRACOTTA.brown(), ModBlocksRegistry.BROWN_TERRACOTTA_SLAB.get()),
            Map.entry(Blocks.DYED_TERRACOTTA.yellow(), ModBlocksRegistry.YELLOW_TERRACOTTA_SLAB.get()),
            Map.entry(Blocks.SOUL_SAND, ModBlocksRegistry.SOUL_SAND_SLAB.get()),
            Map.entry(Blocks.SOUL_SOIL, ModBlocksRegistry.SOUL_SOIL_SLAB.get()),
            Map.entry(Blocks.NETHERRACK, ModBlocksRegistry.NETHERRACK_SLAB.get()),
            Map.entry(Blocks.BLACKSTONE, ModBlocksRegistry.CUSTOM_BLACKSTONE_SLAB.get()),
            Map.entry(Blocks.END_STONE, ModBlocksRegistry.ENDSTONE_SLAB.get())
    );


    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {}
}
