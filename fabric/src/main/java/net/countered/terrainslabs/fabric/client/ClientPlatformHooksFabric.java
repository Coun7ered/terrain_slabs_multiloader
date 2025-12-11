package net.countered.terrainslabs.fabric.client;

import net.countered.platform.ClientPlatformHooks;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.block.ModBlocksRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;

public class ClientPlatformHooksFabric extends ClientPlatformHooks {
    @Override
    public void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocksRegistry.ICE_SLAB.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocksRegistry.GRASS_SLAB.get(), RenderType.cutoutMipped());
        //TODO ontop
    }

    @Override
    public void registerBlockColorProviders() {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
                        world != null && pos != null
                                ? BiomeColors.getAverageGrassColor(world, pos)
                                : GrassColor.getDefaultColor(),
                ModBlocksRegistry.GRASS_SLAB.get()
        );
    }

    @Override
    public void registerItemColorProviders() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) ->
                        Minecraft.getInstance().getBlockColors().getColor(Blocks.GRASS_BLOCK.defaultBlockState(), null, null, tintIndex),
                ModBlocksRegistry.GRASS_SLAB.get()
        );
    }

    @Override
    public void registerBuiltinResourcePacks() {
        ModContainer mod = FabricLoader.getInstance().getModContainer(TerrainSlabs.MOD_ID).orElseThrow();
        ResourceManagerHelper.registerBuiltinResourcePack(
                new ResourceLocation(TerrainSlabs.MOD_ID, "better_grass_slabs"),
                mod,
                ResourcePackActivationType.NORMAL
        );
    }
}
