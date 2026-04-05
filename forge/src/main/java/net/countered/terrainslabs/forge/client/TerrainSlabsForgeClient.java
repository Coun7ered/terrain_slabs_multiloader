package net.countered.terrainslabs.forge.client;

import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TerrainSlabsForgeClient {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        registerRenderLayers();
        registerBlockColorProviders();
        registerItemColorProviders();
    }

    @SuppressWarnings("removal")
    public static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(
                ModBlocksRegistry.ICE_SLAB.get(), RenderType.translucent()
        );
        ItemBlockRenderTypes.setRenderLayer(
                ModBlocksRegistry.GRASS_SLAB.get(), RenderType.cutoutMipped()
        );
    }

    @SuppressWarnings("deprecation")
    public static void registerBlockColorProviders() {
        Minecraft.getInstance().getBlockColors().register(
                (state, world, pos, tintIndex) ->
                        world != null && pos != null
                                ? BiomeColors.getAverageGrassColor(world, pos)
                                : GrassColor.getDefaultColor(),
                ModBlocksRegistry.GRASS_SLAB.get()
        );
    }

    public static void registerItemColorProviders() {
        Minecraft.getInstance().getItemColors().register(
                (stack, tintIndex) ->
                        Minecraft.getInstance().getBlockColors()
                                .getColor(Blocks.GRASS_BLOCK.defaultBlockState(), null, null, tintIndex),
                ModBlocksRegistry.GRASS_SLAB.get()
        );
    }
}
