package net.countered.terrainslabs.neoforge.client;

import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class TerrainSlabsNeoForgeClient {

    public static void init(FMLClientSetupEvent event) {
        registerRenderLayers();
        registerBlockColorProviders();
    }

    @SuppressWarnings("deprecation")
    public static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocksRegistry.ICE_SLAB.get(), ChunkSectionLayer.TRANSLUCENT);
        ItemBlockRenderTypes.setRenderLayer(
                ModBlocksRegistry.GRASS_SLAB.get(), ChunkSectionLayer.CUTOUT
        );
    }

    @SuppressWarnings("deprecation")
    public static void registerBlockColorProviders() {
        Minecraft.getInstance().getBlockColors().register(
                (state, world, pos, tintIndex) ->
                        world != null && pos != null
                                ? Minecraft.getInstance().getBlockColors().getColor(
                                Blocks.GRASS_BLOCK.defaultBlockState(), world, pos, tintIndex)
                                : GrassColor.getDefaultColor(),
                ModBlocksRegistry.GRASS_SLAB.get()
        );
    }
}
