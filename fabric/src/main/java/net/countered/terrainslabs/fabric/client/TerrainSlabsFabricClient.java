package net.countered.terrainslabs.fabric.client;

import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;

public final class TerrainSlabsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        registerRenderLayers();
        registerBlockColorProviders();
        registerItemColorProviders();
        registerBuiltinResourcePacks();
    }

    private void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocksRegistry.ICE_SLAB.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocksRegistry.GRASS_SLAB.get(), RenderType.cutoutMipped());
    }

    private void registerBlockColorProviders() {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
                        world != null && pos != null
                                ? Minecraft.getInstance().getBlockColors().getColor(
                                Blocks.GRASS_BLOCK.defaultBlockState(), world, pos, tintIndex)
                                : GrassColor.getDefaultColor(),
                ModBlocksRegistry.GRASS_SLAB.get()
        );
    }

    private void registerItemColorProviders() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) ->
                        Minecraft.getInstance().getBlockColors().getColor(Blocks.GRASS_BLOCK.defaultBlockState(), null, null, tintIndex),
                ModBlocksRegistry.GRASS_SLAB.get()
        );

    }

    private void registerBuiltinResourcePacks() {
        ModContainer mod = FabricLoader.getInstance().getModContainer(TerrainSlabs.MOD_ID).orElseThrow();
        ResourceManagerHelper.registerBuiltinResourcePack(
                new ResourceLocation(TerrainSlabs.MOD_ID, "better_grass_slabs"),
                mod,
                Component.literal("Better Grass Slabs"),
                ResourcePackActivationType.NORMAL
        );
    }
}
