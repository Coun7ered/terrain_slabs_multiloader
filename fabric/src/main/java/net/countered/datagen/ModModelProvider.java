package net.countered.datagen;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.block.customslabs.soilslabs.SoilSlab;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.LinkedHashMap;
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

        createCustomTextureSlabs(generators);
        createVanillaRedirectedSlabs(generators);
        createNyliumSlabs(generators);
        createSoilSlabs(generators);
        createPathSlab(generators);
        createFarmlandSlab(generators);
    }

    private void createSimpleSlab(BlockModelGenerators generator, Block base, Block slab) {
        TextureMapping textureMapping = TextureMapping.cube(base);

        Identifier bottomId = ModelTemplates.SLAB_BOTTOM.create(slab, textureMapping, generator.modelOutput);
        Identifier topId = ModelTemplates.SLAB_TOP.create(slab, textureMapping, generator.modelOutput);
        Identifier fullBlockId = ModelLocationUtils.getModelLocation(base);

        registerSlabBlockState(generator, slab, bottomId, topId, fullBlockId);
        generator.registerSimpleItemModel(slab, bottomId);
    }

    private void createCustomTextureSlab(BlockModelGenerators generator, Block slab, Block fullBlock, TextureMapping textureMapping) {
        Identifier bottomId = ModelTemplates.SLAB_BOTTOM.create(slab, textureMapping, generator.modelOutput);
        Identifier topId = ModelTemplates.SLAB_TOP.create(slab, textureMapping, generator.modelOutput);
        Identifier fullBlockId = ModelLocationUtils.getModelLocation(fullBlock);

        registerSlabBlockState(generator, slab, bottomId, topId, fullBlockId);
        generator.registerSimpleItemModel(slab, bottomId);
    }

    private void createCustomTextureSlabs(BlockModelGenerators generator) {
        createCustomTextureSlab(generator, ModBlocksRegistry.SNOW_SLAB.get(), Blocks.SNOW_BLOCK, TextureMapping.cube(Blocks.SNOW));

        TextureMapping deepslateTextures = new TextureMapping()
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.DEEPSLATE))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.DEEPSLATE, "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.DEEPSLATE));
        createCustomTextureSlab(generator, ModBlocksRegistry.DEEPSLATE_SLAB.get(), Blocks.DEEPSLATE, deepslateTextures);

        TextureMapping basaltTextures = new TextureMapping()
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.BASALT, "_top"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.BASALT, "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.BASALT, "_side"));
        createCustomTextureSlab(generator, ModBlocksRegistry.BASALT_SLAB.get(), Blocks.BASALT, basaltTextures);
    }

    private void createVanillaRedirectedSlabs(BlockModelGenerators generator) {
        createVanillaRedirectedSlab(generator, ModBlocksRegistry.CUSTOM_SANDSTONE_SLAB.get(), Blocks.SANDSTONE_SLAB, Blocks.SANDSTONE);
        createVanillaRedirectedSlab(generator, ModBlocksRegistry.CUSTOM_RED_SANDSTONE_SLAB.get(), Blocks.RED_SANDSTONE_SLAB, Blocks.RED_SANDSTONE);
    }

    private void createVanillaRedirectedSlab(BlockModelGenerators generator, Block customSlab, Block vanillaSlab, Block fullBlock) {
        Identifier bottomId = ModelLocationUtils.getModelLocation(vanillaSlab);
        Identifier topId = ModelLocationUtils.getModelLocation(vanillaSlab, "_top");
        Identifier fullBlockId = ModelLocationUtils.getModelLocation(fullBlock);

        registerSlabBlockState(generator, customSlab, bottomId, topId, fullBlockId);
        generator.registerSimpleItemModel(customSlab, bottomId);
    }

    private void createNyliumSlabs(BlockModelGenerators generator) {
        createNyliumSlab(generator, ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get(), Blocks.CRIMSON_NYLIUM);
        createNyliumSlab(generator, ModBlocksRegistry.WARPED_NYLIUM_SLAB.get(), Blocks.WARPED_NYLIUM);
    }

    private void createNyliumSlab(BlockModelGenerators generator, Block slab, Block base) {
        Identifier netherrackTex = TextureMapping.getBlockTexture(Blocks.NETHERRACK).sprite();
        Identifier topTex = TextureMapping.getBlockTexture(base).sprite();
        Identifier sideTex = TextureMapping.getBlockTexture(base, "_side").sprite();

        Identifier bottomId = ModelLocationUtils.getModelLocation(slab);
        Map<String, Identifier> textures = orderedTextures(
                "bottom", netherrackTex,
                "side", sideTex,
                "top", topTex
        );
        emitSlabElementModel(generator, bottomId, textures, 0, 8, 0, 8, false, false, false);

        TextureMapping topMapping = new TextureMapping()
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.NETHERRACK))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(base))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(base, "_side"));
        Identifier topId = ModelTemplates.SLAB_TOP.create(slab, topMapping, generator.modelOutput);
        Identifier fullBlockId = ModelLocationUtils.getModelLocation(base);

        registerSlabBlockState(generator, slab, bottomId, topId, fullBlockId);
        generator.registerSimpleItemModel(slab, bottomId);
    }

    private void createSoilSlabs(BlockModelGenerators generator) {
        Identifier dirtTex = TextureMapping.getBlockTexture(Blocks.DIRT).sprite();
        Identifier snowTex = TextureMapping.getBlockTexture(Blocks.SNOW).sprite();
        Identifier grassSnowSideTex = TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_snow").sprite();

        Block grassSlab = ModBlocksRegistry.GRASS_SLAB.get();
        Identifier snowBottomId = ModelLocationUtils.getModelLocation(grassSlab, "_snow_bottom");
        Identifier snowTopId = ModelLocationUtils.getModelLocation(grassSlab, "_snow_top");
        Identifier snowFullId = ModelLocationUtils.getModelLocation(Blocks.GRASS_BLOCK, "_snow");

        Map<String, Identifier> snowTextures = orderedTextures(
                "particle", dirtTex,
                "bottom", dirtTex,
                "top", snowTex,
                "side", grassSnowSideTex
        );
        emitSlabElementModel(generator, snowBottomId, snowTextures, 0, 8, 0, 8, false, false, false);
        emitSlabElementModel(generator, snowTopId, snowTextures, 8, 16, 0, 8, true, false, false);

        MultiVariant snowBottomVariants = BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(snowBottomId));
        MultiVariant snowTopVariants = BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(snowTopId));
        MultiVariant snowFullVariant = BlockModelGenerators.plainVariant(snowFullId);

        // Grass Slab
        Identifier grassBottomId = ModelLocationUtils.getModelLocation(grassSlab, "_bottom");
        Identifier grassTopId = ModelLocationUtils.getModelLocation(grassSlab, "_top");
        Map<String, Identifier> grassTextures = orderedTextures(
                "particle", dirtTex,
                "bottom", dirtTex,
                "top", TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_top").sprite(),
                "side", TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_side").sprite(),
                "overlay", TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_side_overlay").sprite()
        );
        emitSlabElementModel(generator, grassBottomId, grassTextures, 0, 8, 0, 8, false, true, true);
        emitSlabElementModel(generator, grassTopId, grassTextures, 8, 16, 0, 8, true, true, true);
        registerSoilSlabBlockState(
                generator,
                grassSlab,
                grassBottomId,
                grassTopId,
                ModelLocationUtils.getModelLocation(Blocks.GRASS_BLOCK),
                snowBottomVariants,
                snowTopVariants,
                snowFullVariant
        );
        generator.registerSimpleTintedItemModel(grassSlab, grassBottomId, new GrassColorSource());

        // Mycelium Slab
        Block myceliumSlab = ModBlocksRegistry.MYCELIUM_SLAB.get();
        Identifier myceliumBottomId = ModelLocationUtils.getModelLocation(myceliumSlab, "_bottom");
        Identifier myceliumTopId = ModelLocationUtils.getModelLocation(myceliumSlab, "_top");
        Map<String, Identifier> myceliumTextures = orderedTextures(
                "bottom", dirtTex,
                "top", TextureMapping.getBlockTexture(Blocks.MYCELIUM, "_top").sprite(),
                "side", TextureMapping.getBlockTexture(Blocks.MYCELIUM, "_side").sprite()
        );
        emitSlabElementModel(generator, myceliumBottomId, myceliumTextures, 0, 8, 0, 8, false, false, false);
        emitSlabElementModel(generator, myceliumTopId, myceliumTextures, 8, 16, 0, 8, true, false, false);
        registerSoilSlabBlockState(
                generator,
                myceliumSlab,
                myceliumBottomId,
                myceliumTopId,
                ModelLocationUtils.getModelLocation(Blocks.MYCELIUM),
                snowBottomVariants,
                snowTopVariants,
                snowFullVariant
        );
        generator.registerSimpleItemModel(myceliumSlab, myceliumBottomId);

        // Podzol Slab
        Block podzolSlab = ModBlocksRegistry.PODZOL_SLAB.get();
        Identifier podzolBottomId = ModelLocationUtils.getModelLocation(podzolSlab, "_bottom");
        Identifier podzolTopId = ModelLocationUtils.getModelLocation(podzolSlab, "_top");
        Map<String, Identifier> podzolTextures = orderedTextures(
                "bottom", dirtTex,
                "top", TextureMapping.getBlockTexture(Blocks.PODZOL, "_top").sprite(),
                "side", TextureMapping.getBlockTexture(Blocks.PODZOL, "_side").sprite()
        );
        emitSlabElementModel(generator, podzolBottomId, podzolTextures, 0, 8, 0, 8, false, false, false);
        emitSlabElementModel(generator, podzolTopId, podzolTextures, 8, 16, 0, 8, true, false, false);
        registerSoilSlabBlockState(
                generator,
                podzolSlab,
                podzolBottomId,
                podzolTopId,
                ModelLocationUtils.getModelLocation(Blocks.PODZOL),
                snowBottomVariants,
                snowTopVariants,
                snowFullVariant
        );
        generator.registerSimpleItemModel(podzolSlab, podzolBottomId);
    }

    private void createPathSlab(BlockModelGenerators generator) {
        Block pathSlab = ModBlocksRegistry.PATH_SLAB.get();
        Identifier dirtTex = TextureMapping.getBlockTexture(Blocks.DIRT).sprite();
        Identifier bottomId = ModelLocationUtils.getModelLocation(pathSlab, "_bottom");
        Identifier topId = ModelLocationUtils.getModelLocation(pathSlab, "_top");
        Identifier fullId = ModelLocationUtils.getModelLocation(Blocks.DIRT_PATH);

        Map<String, Identifier> pathTextures = orderedTextures(
                "particle", dirtTex,
                "top", TextureMapping.getBlockTexture(Blocks.DIRT_PATH, "_top").sprite(),
                "side", TextureMapping.getBlockTexture(Blocks.DIRT_PATH, "_side").sprite(),
                "bottom", dirtTex
        );
        emitSlabElementModel(generator, bottomId, pathTextures, 0, 7, 1, 8, false, false, false);
        emitSlabElementModel(generator, topId, pathTextures, 8, 15, 1, 8, true, false, false);

        generator.blockStateOutput.accept(
                BlockModelGenerators.createSlab(
                        pathSlab,
                        BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(bottomId)),
                        BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(topId)),
                        BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(fullId))
                )
        );
        generator.registerSimpleItemModel(pathSlab, bottomId);
    }

    private void createFarmlandSlab(BlockModelGenerators generator) {
        Block farmlandSlab = ModBlocksRegistry.FARMLAND_SLAB.get();
        Identifier dirtTex = TextureMapping.getBlockTexture(Blocks.DIRT).sprite();

        Identifier dryBottomId = ModelLocationUtils.getModelLocation(farmlandSlab, "_bottom");
        Identifier dryTopId = ModelLocationUtils.getModelLocation(farmlandSlab, "_top");
        Identifier dryFullId = ModelLocationUtils.getModelLocation(Blocks.FARMLAND);

        Identifier moistBottomId = Identifier.fromNamespaceAndPath(TerrainSlabs.MOD_ID, "block/farmland_moist_slab_bottom");
        Identifier moistTopId = Identifier.fromNamespaceAndPath(TerrainSlabs.MOD_ID, "block/farmland_moist_slab_top");
        Identifier moistFullId = ModelLocationUtils.getModelLocation(Blocks.FARMLAND, "_moist");

        Map<String, Identifier> dryTextures = orderedTextures(
                "particle", dirtTex,
                "top", TextureMapping.getBlockTexture(Blocks.FARMLAND).sprite(),
                "side", dirtTex,
                "bottom", dirtTex
        );
        Map<String, Identifier> moistTextures = orderedTextures(
                "particle", dirtTex,
                "top", TextureMapping.getBlockTexture(Blocks.FARMLAND, "_moist").sprite(),
                "side", dirtTex,
                "bottom", dirtTex
        );

        emitSlabElementModel(generator, dryBottomId, dryTextures, 0, 7, 1, 8, false, false, false);
        emitSlabElementModel(generator, dryTopId, dryTextures, 8, 15, 1, 8, true, false, false);
        emitSlabElementModel(generator, moistBottomId, moistTextures, 0, 7, 1, 8, false, false, false);
        emitSlabElementModel(generator, moistTopId, moistTextures, 8, 15, 1, 8, true, false, false);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(farmlandSlab)
                        .with(PropertyDispatch.initial(BlockStateProperties.SLAB_TYPE, BlockStateProperties.MOISTURE)
                                .generate((type, moisture) -> {
                                    boolean moist = moisture >= 7;
                                    return BlockModelGenerators.plainVariant(switch (type) {
                                        case BOTTOM -> moist ? moistBottomId : dryBottomId;
                                        case TOP -> moist ? moistTopId : dryTopId;
                                        case DOUBLE -> moist ? moistFullId : dryFullId;
                                    });
                                }))
        );
        generator.registerSimpleItemModel(farmlandSlab, dryBottomId);
    }

    private void registerSoilSlabBlockState(
            BlockModelGenerators generator,
            Block slab,
            Identifier bottomId,
            Identifier topId,
            Identifier fullId,
            MultiVariant snowBottom,
            MultiVariant snowTop,
            MultiVariant snowFull
    ) {
        MultiVariant bottomVariants = BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(bottomId));
        MultiVariant topVariants = BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(topId));
        MultiVariant fullVariants = BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(fullId));

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(slab)
                        .with(PropertyDispatch.initial(BlockStateProperties.SLAB_TYPE, SoilSlab.SNOWY)
                                .select(SlabType.BOTTOM, false, bottomVariants)
                                .select(SlabType.TOP, false, topVariants)
                                .select(SlabType.DOUBLE, false, fullVariants)
                                .select(SlabType.BOTTOM, true, snowBottom)
                                .select(SlabType.TOP, true, snowTop)
                                .select(SlabType.DOUBLE, true, snowFull))
        );
    }

    private void emitSlabElementModel(
            BlockModelGenerators generator,
            Identifier modelId,
            Map<String, Identifier> textures,
            int fromY,
            int toY,
            int sideUvMinY,
            int sideUvMaxY,
            boolean isTopHalf,
            boolean tintTop,
            boolean hasOverlay
    ) {
        generator.modelOutput.accept(modelId, () -> {
            JsonObject root = new JsonObject();
            root.addProperty("parent", "minecraft:block/slab");

            JsonObject texturesObj = new JsonObject();
            textures.forEach((slot, texId) -> texturesObj.addProperty(slot, texId.toString()));
            root.add("textures", texturesObj);

            JsonArray elements = new JsonArray();

            JsonObject baseElement = new JsonObject();
            baseElement.add("from", intArray(0, fromY, 0));
            baseElement.add("to", intArray(16, toY, 16));

            JsonObject baseFaces = new JsonObject();
            baseFaces.add("down", face(0, 0, 16, 16, "#bottom", isTopHalf ? null : "down", false));
            baseFaces.add("up", face(0, 0, 16, 16, "#top", isTopHalf ? "up" : null, tintTop));
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                baseFaces.add(dir.getSerializedName(), face(0, sideUvMinY, 16, sideUvMaxY, "#side", dir.getSerializedName(), false));
            }
            baseElement.add("faces", baseFaces);
            elements.add(baseElement);

            if (hasOverlay) {
                JsonObject overlayElement = new JsonObject();
                overlayElement.add("from", intArray(0, fromY, 0));
                overlayElement.add("to", intArray(16, toY, 16));

                JsonObject overlayFaces = new JsonObject();
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    overlayFaces.add(dir.getSerializedName(), face(0, sideUvMinY, 16, sideUvMaxY, "#overlay", dir.getSerializedName(), true));
                }
                overlayElement.add("faces", overlayFaces);
                elements.add(overlayElement);
            }

            root.add("elements", elements);
            return root;
        });
    }

    private static JsonObject face(int u1, int v1, int u2, int v2, String texture, String cullface, boolean tinted) {
        JsonObject obj = new JsonObject();
        obj.add("uv", intArray(u1, v1, u2, v2));
        obj.addProperty("texture", texture);
        if (cullface != null) {
            obj.addProperty("cullface", cullface);
        }
        if (tinted) {
            obj.addProperty("tintindex", 0);
        }
        return obj;
    }

    private static JsonArray intArray(int... values) {
        JsonArray arr = new JsonArray();
        for (int v : values) {
            arr.add(v);
        }
        return arr;
    }

    private static Map<String, Identifier> orderedTextures(Object... pairs) {
        Map<String, Identifier> map = new LinkedHashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put((String) pairs[i], (Identifier) pairs[i + 1]);
        }
        return map;
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
            Map.entry(Blocks.CINNABAR, ModBlocksRegistry.CUSTOM_CINNABAR_SLAB.get()),
            Map.entry(Blocks.SULFUR, ModBlocksRegistry.CUSTOM_SULFUR_SLAB.get()),
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
